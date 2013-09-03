package me.lachlanap.balloonbox.core.level.physics;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author lachlan
 */
public class DelegatingContactListener implements ContactListener {

    private final List<ContactListener> listeners;

    public DelegatingContactListener(ContactListener... startWith) {
        listeners = new ArrayList<>(Arrays.asList(startWith));
    }

    public void addListener(ContactListener listener) {
        listeners.add(listener);
    }

    @Override
    public void beginContact(Contact contact) {
        for (ContactListener listener : listeners)
            listener.beginContact(contact);
    }

    @Override
    public void endContact(Contact contact) {
        for (ContactListener listener : listeners)
            listener.endContact(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        for (ContactListener listener : listeners)
            listener.preSolve(contact, oldManifold);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        for (ContactListener listener : listeners)
            listener.postSolve(contact, impulse);
    }
}
