package me.lachlanap.balloonbox.core.level.physics.impl;

import me.lachlanap.balloonbox.core.level.Entity;
import me.lachlanap.balloonbox.core.level.EntityType;
import me.lachlanap.balloonbox.core.level.physics.EntityCollisionHandler;
import me.lachlanap.balloonbox.core.messaging.MessageBus;

/**
 *
 * @author lachlan
 */
public class BalloonCollisionHandler implements EntityCollisionHandler {

    private final MessageBus messageBus;

    public BalloonCollisionHandler(MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    @Override
    public void handleContact(Entity a, Entity b) {
        if (a.getType() != EntityType.BALLOON && b.getType() != EntityType.BALLOON)
            return;

        Entity balloon = (a.getType() == EntityType.BALLOON) ? a : b;
        Entity other = (a.getType() != EntityType.BALLOON) ? a : b;

        if (other.getType() == EntityType.BOXIS) {
            messageBus.collectedBalloon();
            balloon.markForKill();
        }
    }
}
