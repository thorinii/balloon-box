package me.lachlanap.balloonbox.core.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;
import me.lachlanap.balloonbox.core.perf.PerformanceMonitor;
import me.lachlanap.balloonbox.core.level.physics.Box2DFactory;
import me.lachlanap.balloonbox.core.level.physics.WorldContactHandler;
import me.lachlanap.balloonbox.core.level.physics.impl.BalloonCollisionHandler;
import me.lachlanap.lct.Constant;

/**
 *
 * @author lachlan
 */
public class Level {

    @Constant(name = "Exit Scale", constraints = "0,0.5")
    public static float EXIT_SCALE = .013f;
    @Constant(name = "Max Exit Suction", constraints = "0,1")
    public static float MAX_EXIT_SUCTION = 0.035f;
    @Constant(name = " X Exit Scale", constraints = "0,1")
    public static float X_EXIT_SCALE = 0.05f;
    public static final float EXIT_SENSOR_WIDTH = 0.2f;
    public static final float EXIT_SENSOR_HEIGHT = 2f;
    private final StaticLevelData staticLevelData;
    private final List<Entity> entities;
    private final Score score;
    private final World world;
    private final WorldContactHandler worldContactHandler;
    private PerformanceMonitor performanceMonitor = null;
    private boolean gameover;
    private Entity boxis;

    public static class StaticLevelData {

        public static final float GRID_SCALE = 0.2f;
        public final boolean[][] brickMap;
        public final Vector2 spawnPoint;
        public final Vector2 exitPoint;
        public final List<Vector2> balloons;

        public StaticLevelData(boolean[][] brickMap, Vector2 spawnPoint, Vector2 exitPoint,
                List<Vector2> balloons) {
            this.brickMap = brickMap;
            this.balloons = balloons;
            this.spawnPoint = spawnPoint;
            this.exitPoint = exitPoint;

            spawnPoint.scl(GRID_SCALE);
            exitPoint.scl(GRID_SCALE);
            for (Vector2 balloon : balloons) {
                balloon.scl(GRID_SCALE);
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
        worldContactHandler = new WorldContactHandler(this);
        setupWorld();


        addBoxis();

        gameover = false;
    }

    private void setupWorld() {
        System.out.println("Generating level geometry...");
        Box2DFactory.createLevelGeometry(world, staticLevelData);

        world.setContactListener(worldContactHandler);
        worldContactHandler.addContactHandler(new BalloonCollisionHandler(score));


        /* Add an exit sensor */
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(EXIT_SENSOR_WIDTH / 2, EXIT_SENSOR_HEIGHT / 2, new Vector2(0, EXIT_SENSOR_HEIGHT / 2), 0);

        Body exitFanSensorBody = Box2DFactory.createSensor(world, staticLevelData.exitPoint, shape);
        worldContactHandler.setExitFanSensor(exitFanSensorBody);


        System.out.println("Generating balloons...");
        for (Vector2 balloon : staticLevelData.balloons) {
            addEntity(EntityFactory.makeBalloon(balloon));
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

    public boolean isGameover() {
        return gameover;
    }

    public void update() {
        performanceMonitor.begin("update");

        performanceMonitor.begin("update.box2d");
        world.step(1 / 60f, 8, 3);
        performanceMonitor.end("update.box2d");


        performanceMonitor.begin("update.removing-dead");
        List<Entity> needKilling = new ArrayList<>();
        for (Entity e : entities) {
            e.update();

            if (e.isMarkedForKill()) {
                needKilling.add(e);
                e.detachFromWorld(world);
            }
        }

        entities.removeAll(needKilling);
        performanceMonitor.end("update.removing-dead");


        if (!gameover && worldContactHandler.isExitFanOn()) {
            Body boxisBody = boxis.getBody();

            Vector2 dist = staticLevelData.exitPoint.cpy().sub(boxisBody.getPosition());
            dist.y += 0.7;

            Vector2 impulse = dist.cpy();
            impulse.x *= X_EXIT_SCALE;
            impulse.y = EXIT_SCALE;

            impulse.scl(1 / dist.len2());
            impulse.y = Math.min(impulse.y, MAX_EXIT_SUCTION);

            boxisBody.applyLinearImpulse(impulse, boxisBody.getPosition(), true);

            if (dist.y < -0.3f) {
                boxisBody.setType(BodyDef.BodyType.StaticBody);
                boxisBody.setTransform(
                        staticLevelData.exitPoint.cpy().add(0, 1f),
                        0);

                gameover = true;
            }
        }

        performanceMonitor.end("update");
    }
}
