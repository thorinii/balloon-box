package me.lachlanap.balloonbox.core.level;

import me.lachlanap.balloonbox.core.level.physics.WorldContactHandler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;
import me.lachlanap.balloonbox.core.level.physics.GridToChainFactory;
import me.lachlanap.balloonbox.core.level.physics.impl.BalloonContactHandler;

/**
 *
 * @author lachlan
 */
public class Level {

    private final List<Entity> entities;
    private final Score score;
    private final World world;
    private final WorldContactHandler worldContactHandler;
    private final boolean[][] brickMap;
    private Entity boxis;

    public Level(Vector2 gravity, boolean[][] brickMap) {
        entities = new ArrayList<>();
        score = new Score();

        this.brickMap = brickMap;

        world = new World(gravity, true);
        worldContactHandler = new WorldContactHandler(this);
        setupWorld(GridToChainFactory.makeChainShapes(brickMap, 0.1f));

        addBoxis();

        /*for (int i = -15; i < 15; i++) {
         addEntity(EntityFactory.makeBrick(new Vector2(i * .6f, 0)));
         }*/

        for (int i = -15; i < 15; i++) {
            addEntity(EntityFactory.makeBalloon(new Vector2(i * .6f, 1)));
        }
    }

    private void setupWorld(ChainShape[] brickGeometry) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(0, 0);
        def.awake = true;

        Body bricks = world.createBody(def);

        for (ChainShape shape : brickGeometry) {
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 1;
            fixtureDef.restitution = 0.1f;
            fixtureDef.friction = 0.1f;
            fixtureDef.shape = shape;

            bricks.createFixture(fixtureDef);
        }

        world.setContactListener(worldContactHandler);
        worldContactHandler.addContactHandler(new BalloonContactHandler(score));

    }

    public void addBoxis() {
        boxis = EntityFactory.makeBoxis(new Vector2(7, 3));
        addEntity(boxis);
    }

    public void addEntity(Entity entity) {
        entity.attachToWorld(world);
        entities.add(entity);
    }

    public boolean[][] getBrickMap() {
        return brickMap;
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

    public void update() {
        world.step(1 / 60f, 8, 3);

        List<Entity> needKilling = new ArrayList<>();
        for (Entity e : entities) {
            e.update();

            if (e.isMarkedForKill())
                needKilling.add(e);
        }

        for (Entity e : needKilling) {
            e.detachFromWorld(world);
            entities.remove(e);
        }
    }
}
