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
public class WorldContactHandler implements ContactListener {

    private final Level level;
    private final List<EntityCollisionHandler> handlers = new ArrayList<>();
    private Body exitFanSensorBody;
    private boolean exitFanOn;

    public WorldContactHandler(final Level level) {
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

        Entity a = null;
        Entity b = null;
        for (Entity e : level.getEntities()) {
            if (e.getBody() == fa.getBody())
                a = e;
            if (e.getBody() == fb.getBody())
                b = e;
        }

        if (a != null && b != null && !a.isMarkedForKill() && !b.isMarkedForKill())
            for (EntityCollisionHandler handler : handlers)
                handler.handleContact(a, b);

        Body boxisBody = level.getBoxis().getBody();
        if (fa.getBody() == boxisBody || fb.getBody() == boxisBody) {
            Body other = (fa.getBody() == boxisBody) ? fb.getBody() : fa.getBody();

            if (level.getBoxis().checkGroundSensor(contact)) {
                if ((a != null && !a.canJumpOn()) || (b != null && !b.canJumpOn()))
                    return;

                level.getBoxis().addOnGround();
            }

            if (other == exitFanSensorBody)
                exitFanOn = true;

        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.isTouching())
            return;

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Entity a = null;
        Entity b = null;
        for (Entity e : level.getEntities()) {
            if (e.getBody() == fa.getBody())
                a = e;
            if (e.getBody() == fb.getBody())
                b = e;
        }

        if (a == null && b == null)
            return;

        Body boxisBody = level.getBoxis().getBody();
        if (fa.getBody() == boxisBody || fb.getBody() == boxisBody) {
            Body other = (fa.getBody() == boxisBody) ? fb.getBody() : fa.getBody();

            if (level.getBoxis().checkGroundSensor(contact)) {
                level.getBoxis().takeOnGround();
            }

            if (other == exitFanSensorBody)
                exitFanOn = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Entity a = null;
        Entity b = null;
        for (Entity e : level.getEntities()) {
            if (e.getBody() == fa.getBody())
                a = e;
            if (e.getBody() == fb.getBody())
                b = e;
        }

        if (a == null || b == null)
            return;

        if (a.isMarkedForKill() || b.isMarkedForKill())
            contact.setEnabled(false);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public void setExitFanSensor(Body exitFanSensorBody) {
        this.exitFanSensorBody = exitFanSensorBody;
    }

    public boolean isExitFanOn() {
        return exitFanOn;
    }
}
