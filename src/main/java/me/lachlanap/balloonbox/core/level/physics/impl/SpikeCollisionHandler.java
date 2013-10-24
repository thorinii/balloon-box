package me.lachlanap.balloonbox.core.level.physics.impl;

import com.badlogic.gdx.Gdx;
import me.lachlanap.balloonbox.core.level.Entity;
import me.lachlanap.balloonbox.core.level.EntityType;
import me.lachlanap.balloonbox.core.level.Score;
import me.lachlanap.balloonbox.core.level.controller.KeyboardController;
import me.lachlanap.balloonbox.core.level.controller.TimedSelfDestructController;
import me.lachlanap.balloonbox.core.level.physics.EntityCollisionHandler;

/**
 *
 * @author lachlan
 */
public class SpikeCollisionHandler implements EntityCollisionHandler {

    private final Score score;

    public SpikeCollisionHandler(Score score) {
        this.score = score;
    }

    @Override
    public void handleContact(Entity a, Entity b) {
        if (a.getType() != EntityType.SPIKES && b.getType() != EntityType.SPIKES)
            return;

        final Entity spike = (a.getType() == EntityType.SPIKES) ? a : b;
        final Entity boxis = (a.getType() != EntityType.SPIKES) ? a : b;

        if (boxis.getType() == EntityType.BOXIS) {
            if (!boxis.hasController(KeyboardController.class))
                return;

            boxis.removeController(KeyboardController.class);
            boxis.addController(new TimedSelfDestructController(2, score));

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    boxis.getBody().setFixedRotation(false);
                    boxis.getBody().setActive(false);
                }
            });
        }
    }
}
