package me.lachlanap.balloonbox.core.level;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import me.lachlanap.balloonbox.core.level.controller.KeyboardController;
import me.lachlanap.balloonbox.core.level.controller.TimedSelfDestructController;
import me.lachlanap.balloonbox.core.level.physics.*;
import me.lachlanap.balloonbox.core.level.physics.SensorManager.Sensor;
import me.lachlanap.balloonbox.core.level.physics.impl.BalloonCollisionHandler;
import me.lachlanap.balloonbox.core.level.physics.impl.BatteryCollisionHandler;
import me.lachlanap.balloonbox.core.level.physics.impl.SpikeCollisionHandler;
import me.lachlanap.balloonbox.core.messaging.MessageBus;
import me.lachlanap.balloonbox.core.perf.PerformanceMonitor;
import me.lachlanap.lct.Constant;

/**
 *
 * @author lachlan
 */
public class Level {

    public static final Logger LOG = Logger.getLogger(Level.class.getName());
    public static final float FLUID_DRAG = 0.17f;
    @Constant(name = "Exit Fan Override")
    public static boolean EXIT_FAN_OVERRIDE = false;
    public static final float EXIT_SENSOR_WIDTH = 0.2f;
    public static final float EXIT_SENSOR_HEIGHT = 1.5f;
    public static final float EXIT_SCALE = 0.027f;
    public static final float MAX_EXIT_SUCTION = 0.035f;
    public static final float X_EXIT_SCALE = 0.01f;
    //
    private final MessageBus messageBus;
    private final StaticLevelData staticLevelData;
    private final List<Entity> entities;
    private final Score score;
    private final World world;
    private final SensorManager sensorManager;
    private final Sensor exitFanSensor;
    private final TimerManager timerManager;
    private final Sensor[] acidSensors;
    private final LevelAnimationController levelAnimationController;
    //
    private PerformanceMonitor performanceMonitor = null;
    private boolean gameover;
    private Entity boxis;

    public static class StaticLevelData {

        public static final float GRID_SCALE = 0.2f;
        public static final float BOUNDS_PADDING = 5f;
        //
        public final boolean[][] brickMap;
        public final Vector2 spawnPoint;
        public final Vector2 exitPoint;
        public final List<Vector2> balloons;
        public final List<Vector2> batteries;
        public final List<Vector2> spikes;
        public final List<Rectangle> acids;
        public final Rectangle bounds;

        public StaticLevelData(boolean[][] brickMap, Vector2 spawnPoint, Vector2 exitPoint,
                               List<Vector2> balloons, List<Vector2> batteries, List<Vector2> spikes,
                               List<Rectangle> acids) {
            this.brickMap = brickMap;
            this.spawnPoint = spawnPoint;
            this.exitPoint = exitPoint;

            this.balloons = balloons;
            this.batteries = batteries;
            this.spikes = spikes;

            this.acids = acids;

            bounds = new Rectangle();

            bounds.x = -BOUNDS_PADDING;
            bounds.y = -BOUNDS_PADDING;
            bounds.width = brickMap.length * GRID_SCALE + 2 * BOUNDS_PADDING;
            bounds.height = brickMap[0].length * GRID_SCALE + 2 * BOUNDS_PADDING;

            spawnPoint.scl(GRID_SCALE);
            exitPoint.scl(GRID_SCALE);

            for (Vector2 balloon : balloons)
                balloon.scl(GRID_SCALE);
            for (Vector2 battery : batteries)
                battery.scl(GRID_SCALE);
            for (Vector2 spike : spikes)
                spike.scl(GRID_SCALE);

            for (Rectangle acid : acids) {
                acid.x *= GRID_SCALE;
                acid.y *= GRID_SCALE;
                acid.width *= GRID_SCALE;
                acid.height *= GRID_SCALE;
            }
        }
    }

    public Level(MessageBus messageBus,
                 Vector2 gravity,
                 StaticLevelData staticLevelData) {
        this.messageBus = messageBus;
        entities = new ArrayList<>();
        score = new Score(messageBus);

        this.staticLevelData = staticLevelData;

        world = new World(gravity, true);

        sensorManager = new SensorManager(this);
        exitFanSensor = sensorManager.createSensor("exit-fan",
                                                   staticLevelData.exitPoint.cpy().add(0, EXIT_SENSOR_HEIGHT / 2),
                                                   new Vector2(EXIT_SENSOR_WIDTH / 2, EXIT_SENSOR_HEIGHT / 2));

        timerManager = new TimerManager();

        acidSensors = new Sensor[staticLevelData.acids.size()];

        setupWorld();

        addBoxis();

        int i = 0;
        for (Rectangle acidBounds : staticLevelData.acids) {
            acidSensors[i] = sensorManager.createSensor("acid-" + i,
                                                        new Vector2(acidBounds.x + acidBounds.width / 2,
                                                                    acidBounds.y + acidBounds.height / 2),
                                                        new Vector2(acidBounds.width / 2,
                                                                    acidBounds.height / 2));
            i++;
        }

        levelAnimationController = new LevelAnimationController();

        gameover = false;
    }

    private void setupWorld() {
        LOG.info("Generating level geometry...");
        Box2DFactory.createLevelGeometry(world, staticLevelData);

        Box2DFactory.createPipe(world, staticLevelData.spawnPoint);
        Box2DFactory.createPipe(world, staticLevelData.exitPoint);

        EntityCollisionContactHandler entityCollisionContactHandler = new EntityCollisionContactHandler(this);
        entityCollisionContactHandler.addContactHandler(new BalloonCollisionHandler(messageBus));
        entityCollisionContactHandler.addContactHandler(new BatteryCollisionHandler(messageBus));
        entityCollisionContactHandler.addContactHandler(new SpikeCollisionHandler(messageBus));

        world.setContactListener(new DelegatingContactListener(
                entityCollisionContactHandler,
                new OnGroundContactHandler(this),
                sensorManager));

        LOG.info("Blowing up balloons...");
        for (Vector2 balloon : staticLevelData.balloons) {
            addEntity(EntityFactory.makeBalloon(balloon));
        }

        LOG.info("Charging batteries...");
        for (Vector2 battery : staticLevelData.batteries) {
            addEntity(EntityFactory.makeBattery(battery));
        }

        LOG.info("Stacking knives...");
        for (Vector2 spike : staticLevelData.spikes) {
            addEntity(EntityFactory.makeSpike(spike.cpy().add(0, EntityFactory.SPIKES_SIZE.y / 2f)));
        }
    }

    public void setPerformanceMonitor(PerformanceMonitor performanceMonitor) {
        this.performanceMonitor = performanceMonitor;
    }

    public void addBoxis() {
        boxis = EntityFactory.makeBoxis(staticLevelData.spawnPoint.cpy().add(0, 1f));
        addEntity(boxis);
    }

    public void addEntity(Entity entity) {
        entity.attachToWorld(world);
        entities.add(entity);
    }

    public StaticLevelData getStaticLevelData() {
        return staticLevelData;
    }

    public World getWorld() {
        return world;
    }

    public Entity getBoxis() {
        return boxis;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Score getScore() {
        return score;
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public LevelAnimationController getLevelAnimationController() {
        return levelAnimationController;
    }

    public boolean isGameover() {
        return gameover;
    }

    public void update() {
        performanceMonitor.begin("update");

        timerManager.update(1 / 60f);
        levelAnimationController.update();

        performanceMonitor.begin("update.box2d");
        world.step(1 / 60f, 8, 3);
        performanceMonitor.end("update.box2d");

        if (boxis != null && !staticLevelData.bounds.contains(boxis.getPosition())) {
            messageBus.died();
        }

        score.update(boxis, timerManager, new CreateBoxisTask());
        if (score.getLives() <= 0)
            gameover = true;

        performanceMonitor.begin("update.removing-dead");
        removeDeadEntities();
        performanceMonitor.end("update.removing-dead");

        if (!gameover
            && (score.getBatteries() >= staticLevelData.batteries.size() || EXIT_FAN_OVERRIDE)) {
            doExitFan();
        }
        doAcid();

        performanceMonitor.end("update");
    }

    private void removeDeadEntities() {
        List<Entity> needKilling = new ArrayList<>();
        for (Entity e : entities) {
            e.update(1 / 60f);

            if (e.isMarkedForKill() || !staticLevelData.bounds.contains(e.getPosition())) {
                needKilling.add(e);
                e.detachFromWorld(world);

                if (e == boxis)
                    boxis = null;
            }
        }

        entities.removeAll(needKilling);
    }

    private void doExitFan() {
        for (Entity entity : exitFanSensor.getTouchingEntities()) {
            Body b = entity.getBody();

            Vector2 dist = staticLevelData.exitPoint.cpy().sub(b.getPosition());
            dist.y += 0.7;

            Vector2 impulse = dist.cpy();
            impulse.x *= X_EXIT_SCALE;
            impulse.y = EXIT_SCALE;

            impulse.scl(1 / dist.len2());
            impulse.y = Math.min(impulse.y, MAX_EXIT_SUCTION);

            b.applyLinearImpulse(impulse, b.getPosition(), true);

            if (dist.y < -0.3f) {
                b.setType(BodyDef.BodyType.StaticBody);
                b.setTransform(
                        staticLevelData.exitPoint.cpy().add(0, 1f),
                        0);

                if (entity == boxis)
                    gameover = true;
            }
        }
    }

    private void doAcid() {
        for (Sensor acid : acidSensors) {
            float aLeft = acid.getPosition().x - acid.getExtents().x;
            float aTop = acid.getPosition().y - acid.getExtents().y;
            float aRight = aLeft + acid.getExtents().x * 2;
            float aBottom = aTop + acid.getExtents().y * 2;

            for (Entity e : acid.getTouchingEntities()) {
                Vector2 p = e.getPosition();
                Vector2 s = e.getSize();
                float bLeft = p.x - s.x / 2;
                float bTop = p.y - s.y / 2;
                float bRight = p.x + s.x;
                float bBottom = p.y + s.y;

                float cLeft = Math.max(aLeft, bLeft);
                float cTop = Math.max(aTop, bTop);
                float cRight = Math.min(aRight, bRight);
                float cBottom = Math.min(aBottom, bBottom);

                float width = cRight - cLeft;
                float height = cBottom - cTop;

                float area = width * height;

                Vector2 centreOfIntersection = new Vector2((cLeft + cRight) / 2, +(cTop + cBottom) / 2);

                e.getBody().applyForce(world.getGravity().cpy().scl(-area * 1.1f), centreOfIntersection, true);

                Vector2 drag = e.getBody().getLinearVelocity().cpy();
                drag.scl(drag.len2());
                e.getBody().applyForce(
                        drag.scl(-FLUID_DRAG),
                        centreOfIntersection,
                        true);

                if (!e.hasController(KeyboardController.class))
                    continue;

                e.removeController(KeyboardController.class);
                e.addController(new TimedSelfDestructController(2, messageBus));
                e.getBody().setFixedRotation(false);
            }
        }
    }

    private class CreateBoxisTask implements Runnable {

        @Override
        public void run() {
            addBoxis();
        }
    }
}
