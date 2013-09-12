package me.lachlanap.balloonbox.core.level.physics.impl;

import me.lachlanap.balloonbox.core.level.Entity;
import me.lachlanap.balloonbox.core.level.EntityType;
import me.lachlanap.balloonbox.core.level.Score;
import me.lachlanap.balloonbox.core.level.physics.EntityCollisionHandler;

/**
 *
 * @author lachlan
 */
public class BatteryCollisionHandler implements EntityCollisionHandler {

    private final Score score;

    public BatteryCollisionHandler(Score score) {
        this.score = score;
    }

    @Override
    public void handleContact(Entity a, Entity b) {
        if (a.getType() != EntityType.BATTERY && b.getType() != EntityType.BATTERY)
            return;

        Entity battery = (a.getType() == EntityType.BATTERY) ? a : b;
        Entity other = (a.getType() != EntityType.BATTERY) ? a : b;

        if (other.getType() == EntityType.BOXIS) {
            score.collectBattery();
            battery.markForKill();
        }
    }
}
