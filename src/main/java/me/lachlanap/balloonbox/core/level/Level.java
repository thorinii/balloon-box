package me.lachlanap.balloonbox.core.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import me.lachlanap.balloonbox.core.level.physics.*;
import me.lachlanap.balloonbox.core.level.physics.SensorManager.Sensor;
import me.lachlanap.balloonbox.core.level.physics.impl.BalloonCollisionHandler;
import me.lachlanap.balloonbox.core.level.physics.impl.BatteryCollisionHandler;
import me.lachlanap.balloonbox.core.perf.PerformanceMonitor;
import me.lachlanap.lct.Constant;

/**
 *
 * @author lachlan
 */
public class Level {

    public static final Logger LOG = Logger.getLogger(Level.class.getName());
    @Constant(name = "Exit Scale", constraints = "0,0.5")
    public static float EXIT_SCALE = .013f;
    @Constant(name = "Max Exit Suction", constraints = "0,1")
    public static float MAX_EXIT_SUCTION = 0.035f;
    @Constant(name = " X Exit Scale", constraints = "0,1")
    public static float X_EXIT_SCALE = 0.05f;
    @Constant(name = "Exit Fan Override")
    public static boolean EXIT_FAN_OVERRIDE = false;
    public static final float EXIT_SENSOR_WIDTH = 0.2f;
    public static final float EXIT_SENSOR_HEIGHT = 1.5f;
    //
    private final StaticLevelData staticLevelData;
    private final List<Entity> entities;
    private final Score score;
    private final World world;
    private final SensorManager sensorManager;
    private final Sensor exitFanSensor;
    private final TimerManager timerManager;
    //
    private PerformanceMonitor performanceMonitor = null;
    private boolean gameover;
    private Entity boxis;

    public static class StaticLevelData {

        public static final float GRID_SCALE = 0.2f;
        public final boolean[][] brickMap;
        public final Vector2 spawnPoint;
        public final Vector2 exitPoint;
        public final List<Vector2> balloons;
        public final List<Vector2> batteries;

        public StaticLevelData(boolean[][] brickMap, Vector2 spawnPoint, Vector2 exitPoint,
                List<Vector2> balloons, List<Vector2> batteries) {
            this.brickMap = brickMap;
            this.balloons = balloons;
            this.spawnPoint = spawnPoint;
            this.exitPoint = exitPoint;
            this.batteries = batteries;

            spawnPoint.scl(GRID_SCALE);
            exitPoint.scl(GRID_SCALE);
            for (Vector2 balloon : balloons) {
                balloon.scl(GRID_SCALE);
            }
            for (Vector2 battery : batteries) {
                battery.scl(GRID_SCALE);
            }
        }
    }

    public Level(
            Vector2 gravity,
            StaticLevelData staticLevelData) {
        entities = new ArrayList<>();
        score = new Score();

        this.staticLevelData = staticLevelData;

        world = new World(gravity, true);

        sensorManager = new SensorManager(this);
        exitFanSensor = sensorManager.createSensor("exit-fan",
                                                   staticLevelData.exitPoint.cpy().add(0, EXIT_SENSOR_HEIGHT / 2),
                                                   new Vector2(EXIT_SENSOR_WIDTH / 2, EXIT_SENSOR_HEIGHT / 2));

        timerManager = new TimerManager();

        setupWorld();


        addBoxis();

        gameover = false;
    }

    private void setupWorld() {
        LOG.info("Generating level geometry...");
        Box2DFactory.createLevelGeometry(world, staticLevelData);

        Box2DFactory.createPipe(world, staticLevelData.spawnPoint);
        Box2DFactory.createPipe(world, staticLevelData.exitPoint);

        EntityCollisionContactHandler entityCollisionContactHandler = new EntityCollisionContactHandler(this);
        entityCollisionContactHandler.addContactHandler(new BalloonCollisionHandler(score));
        entityCollisionContactHandler.addContactHandler(new BatteryCollisionHandler(score));

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

    public boolean isGameover() {
        return gameover;
    }

    public void update() {
        performanceMonitor.begin("update");

        timerManager.update(1 / 60f);

        performanceMonitor.begin("update.box2d");
        world.step(1 / 60f, 8, 3);
        performanceMonitor.end("update.box2d");

        score.update(boxis, timerManager, new CreateBoxisTask());

        performanceMonitor.begin("update.removing-dead");
        removeDeadEntities();
        performanceMonitor.end("update.removing-dead");

        if (!gameover
                && (score.getBatteries() >= staticLevelData.batteries.size() || EXIT_FAN_OVERRIDE)) {
            doExitFan();
        }

        performanceMonitor.end("update");
    }

    private void removeDeadEntities() {
        List<Entity> needKilling = new ArrayList<>();
        for (Entity e : entities) {
            e.update();

            if (e.isMarkedForKill()) {
                needKilling.add(e);
                e.detachFromWorld(world);

                if (boxis == e)
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

    private class CreateBoxisTask implements Runnable {

        @Override
        public void run() {
            addBoxis();
        }
    }
}
