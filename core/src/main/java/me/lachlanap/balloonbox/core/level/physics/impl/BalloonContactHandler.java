package me.lachlanap.balloonbox.core.level.physics.impl;

import me.lachlanap.balloonbox.core.level.Entity;
import me.lachlanap.balloonbox.core.level.EntityType;
import me.lachlanap.balloonbox.core.level.Score;
import me.lachlanap.balloonbox.core.level.physics.EntityContantHandler;

/**
 *
 * @author lachlan
 */
public class BalloonContactHandler implements EntityContantHandler {

    private final Score score;

    public BalloonContactHandler(Score score) {
        this.score = score;
    }

    @Override
    public void handleContact(Entity a, Entity b) {
        if (a.getType() != EntityType.BALLOON && b.getType() != EntityType.BALLOON)
            return;

        Entity balloon = (a.getType() == EntityType.BALLOON) ? a : b;
        Entity other = (a.getType() != EntityType.BALLOON) ? a : b;

        if (other.getType() == EntityType.BOXIS) {
            score.collectBalloon();
            balloon.markForKill();
        }
    }
}