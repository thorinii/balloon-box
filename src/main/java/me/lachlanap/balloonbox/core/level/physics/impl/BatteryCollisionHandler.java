package me.lachlanap.balloonbox.core.level.physics.impl;

import me.lachlanap.balloonbox.core.level.Entity;
import me.lachlanap.balloonbox.core.level.EntityType;
import me.lachlanap.balloonbox.core.level.physics.EntityCollisionHandler;
import me.lachlanap.balloonbox.core.messaging.MessageBus;

/**
 *
 * @author lachlan
 */
public class BatteryCollisionHandler implements EntityCollisionHandler {

    private final MessageBus messageBus;

    public BatteryCollisionHandler(MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    @Override
    public void handleContact(Entity a, Entity b) {
        if (a.getType() != EntityType.BATTERY && b.getType() != EntityType.BATTERY)
            return;

        Entity battery = (a.getType() == EntityType.BATTERY) ? a : b;
        Entity other = (a.getType() != EntityType.BATTERY) ? a : b;

        if (other.getType() == EntityType.BOXIS) {
            messageBus.collectedBattery();
            battery.markForKill();
        }
    }
}
