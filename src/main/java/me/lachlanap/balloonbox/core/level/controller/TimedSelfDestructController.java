package me.lachlanap.balloonbox.core.level.controller;

import me.lachlanap.balloonbox.core.messaging.MessageBus;

/**
 *
 * @author lachlan
 */
public class TimedSelfDestructController extends Controller {

    private final long timeToRunOn;
    private final MessageBus messageBus;
    private boolean executed = false;

    public TimedSelfDestructController(float time) {
        this.timeToRunOn = System.currentTimeMillis() + (long) (time * 1000);
        this.messageBus = null;
    }

    public TimedSelfDestructController(float time, MessageBus messageBus) {
        this.timeToRunOn = System.currentTimeMillis() + (long) (time * 1000);
        this.messageBus = messageBus;
    }

    @Override
    public void update(float tpf) {
        if (executed)
            return;

        if (System.currentTimeMillis() >= timeToRunOn) {
            if (messageBus != null)
                messageBus.died();
            else
                entity.markForKill();

            executed = true;
        }
    }
}
