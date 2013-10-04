package me.lachlanap.balloonbox.core.level.physics;

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
public class OnGroundContactHandler implements ContactListener {

    private final Level level;

    public OnGroundContactHandler(final Level level) {
        this.level = level;
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

            if (a.isMarkedForKill() || b.isMarkedForKill())
                return;

            if (a.checkGroundSensor(contact)) {
                if (b.canJumpOn())
                    a.addOnGround();
            } else if (b.checkGroundSensor(contact)) {
                if (a.canJumpOn())
                    b.addOnGround();
            }
        } else if (collided.size() == 1) {
            Entity e = collided.get(0);

            if (e.checkGroundSensor(contact)) {
                e.addOnGround();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.isTouching())
            return;

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        List<Entity> collided = findEntities(fa, fb);

        if (collided.size() == 2) {
            Entity a = collided.get(0);
            Entity b = collided.get(1);

            if (a.checkGroundSensor(contact)) {
                if (b.canJumpOn())
                    a.takeOnGround();
            } else if (b.checkGroundSensor(contact)) {
                if (a.canJumpOn())
                    b.takeOnGround();
            }
        } else if (collided.size() == 1) {
            Entity e = collided.get(0);

            if (e.checkGroundSensor(contact)) {
                e.takeOnGround();
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
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
