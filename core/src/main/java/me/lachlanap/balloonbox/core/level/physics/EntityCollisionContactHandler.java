package me.lachlanap.balloonbox.core.level.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import java.util.ArrayList;
import java.util.List;
import me.lachlanap.balloonbox.core.level.Entity;
import me.lachlanap.balloonbox.core.level.Level;

/**
 *
 * @author lachlan
 */
public class EntityCollisionContactHandler implements ContactListener {

    private final Level level;
    private final List<EntityCollisionHandler> handlers = new ArrayList<>();

    public EntityCollisionContactHandler(final Level level) {
        this.level = level;
    }

    public void addContactHandler(EntityCollisionHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getBody() == fb.getBody() || !contact.isEnabled())
            return;

        List<Entity> collided = findEntities(fa, fb);
        if (collided.size() == 2) {
            Entity a = collided.get(0);
            Entity b = collided.get(1);

            if (!a.isMarkedForKill() && !b.isMarkedForKill())
                for (EntityCollisionHandler handler : handlers)
                    handler.handleContact(a, b);
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        List<Entity> entities = findEntities(fa, fb);
        for (Entity e : entities) {
            if (e.isMarkedForKill())
                contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private List<Entity> findEntities(Fixture... fixtures) {
        List<Entity> entities = new ArrayList<>();

        for (Fixture f : fixtures)
            for (Entity e : level.getEntities())
                if (f.getBody() == e.getBody())
                    entities.add(e);

        return entities;
    }
}
