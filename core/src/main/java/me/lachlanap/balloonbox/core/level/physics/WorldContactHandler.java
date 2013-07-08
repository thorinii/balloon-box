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
    private final List<EntityContantHandler> handlers = new ArrayList<>();

    public WorldContactHandler(final Level level) {
        this.level = level;
    }

    public void addContactHandler(EntityContantHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Body boxisBody = level.getBoxis().getBody();
        if (fa.getBody() == boxisBody || fb.getBody() == boxisBody)
            if (contact.isTouching() && level.getBoxis().checkGroundSensor(contact))
                level.getBoxis().setOnGround(true);

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

        for (EntityContantHandler handler : handlers) {
            handler.handleContact(a, b);
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.isTouching())
            return;

        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        Body boxisBody = level.getBoxis().getBody();
        if (a.getBody() == boxisBody || b.getBody() == boxisBody)
            if (level.getBoxis().checkGroundSensor(contact))
                level.getBoxis().setOnGround(false);
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
}
