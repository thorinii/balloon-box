package me.lachlanap.balloonbox.core.messaging;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.lachlanap.balloonbox.core.level.EndOfLevelInfo;

/**
 *
 * @author lachlan
 */
public class MessageBus {

    private final List<MessageListener> listeners;

    public MessageBus() {
        listeners = new CopyOnWriteArrayList<>();
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


    public void endOfLevel(EndOfLevelInfo info) {
        for (MessageListener listener : listeners)
            listener.endOfLevel(info);
    }

    public void nextLevel() {
        for (MessageListener listener : listeners)
            listener.nextLevel();
    }

    public void restartLevel() {
        for (MessageListener listener : listeners)
            listener.restartLevel();
    }

    public void exitLevel() {
        for (MessageListener listener : listeners)
            listener.exitLevel();
    }


    public void toggleDevTools() {
        for (MessageListener listener : listeners)
            listener.toggleDevTools();
    }
}
