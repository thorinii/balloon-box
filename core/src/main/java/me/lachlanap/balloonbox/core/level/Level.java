package me.lachlanap.balloonbox.core.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lachlan
 */
public class Level {

    private final World world;
    private final List<Entity> entities;
    private Entity boxis;

    public Level(Vector2 gravity) {
        world = new World(gravity, true);
        setupWorld();

        entities = new ArrayList<>();

        addBoxis();

        for (int i = -15; i < 15; i++) {
            addEntity(EntityFactory.makeBrick(new Vector2(i * .6f, 0)));
        }
    }

    private void setupWorld() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();

                Body boxisBody = boxis.getBody();

                if (a.getBody() == boxisBody || b.getBody() == boxisBody) {
                    if (contact.isTouching() && boxis.checkGroundSensor(contact))
                        boxis.setOnGround(true);
                }
            }

            @Override
            public void endContact(Contact contact) {
                if (contact.isTouching())
                    return;

                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();

                Body boxisBody = boxis.getBody();

                if (a.getBody() == boxisBody || b.getBody() == boxisBody) {
                    if (boxis.checkGroundSensor(contact))
                        boxis.setOnGround(false);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    public void addBoxis() {
        boxis = EntityFactory.makeBoxis(new Vector2(1, 1));
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

        for (Entity e : entities)
            e.update();
    }
}
