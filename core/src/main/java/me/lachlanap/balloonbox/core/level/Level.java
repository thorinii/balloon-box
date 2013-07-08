package me.lachlanap.balloonbox.core.level;

import me.lachlanap.balloonbox.core.level.physics.WorldContactHandler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;
import me.lachlanap.balloonbox.core.level.physics.impl.BalloonContactHandler;

/**
 *
 * @author lachlan
 */
public class Level {

    private final World world;
    private final WorldContactHandler worldContactHandler;
    private final List<Entity> entities;
    private Entity boxis;

    public Level(Vector2 gravity) {
        world = new World(gravity, true);
        worldContactHandler = new WorldContactHandler(this);
        setupWorld();

        entities = new ArrayList<>();

        addBoxis();

        for (int i = -15; i < 15; i++) {
            addEntity(EntityFactory.makeBrick(new Vector2(i * .6f, 0)));
        }

        for (int i = -15; i < 15; i++) {
            addEntity(EntityFactory.makeBalloon(new Vector2(i * .6f, 1)));
        }
    }

    private void setupWorld() {
        world.setContactListener(worldContactHandler);

        worldContactHandler.addContactHandler(new BalloonContactHandler());
    }

    public void addBoxis() {
        boxis = EntityFactory.makeBoxis(new Vector2(1, 2));
        addEntity(boxis);
    }

    public void addEntity(Entity entity) {
        entity.attachToWorld(world);
        entities.add(entity);
    }

    public Entity getBoxis() {
        return boxis;
    }

    public List<Entity> getEntities() {
        return entities;
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
