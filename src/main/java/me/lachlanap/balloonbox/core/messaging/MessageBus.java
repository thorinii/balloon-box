package me.lachlanap.balloonbox.core.messaging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author lachlan
 */
public class MessageBus {

    private final List<MessageListener> listeners;

    public MessageBus() {
        listeners = new ArrayList<>();
    }

    public void addMessageListener(MessageListener listener) {
        listeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener) {
        listeners.remove(listener);
    }


    public void disconnectTransientListeners() {
        for (Iterator<MessageListener> it = listeners.iterator(); it.hasNext();) {
            MessageListener listener = it.next();
            if (listener.disconnectTransientListeners())
                it.remove();
        }
    }


    public void collectedBalloon() {
        for (MessageListener listener : listeners)
            listener.collectedBalloon();
    }

    public void collectedBattery() {
        for (MessageListener listener : listeners)
            listener.collectedBattery();
    }

    public void collectedLife() {
        for (MessageListener listener : listeners)
            listener.collectedLife();
    }

    public void died() {
        for (MessageListener listener : listeners)
            listener.died();
    }

}
