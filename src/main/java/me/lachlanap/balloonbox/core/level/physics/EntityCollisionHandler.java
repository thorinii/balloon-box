package me.lachlanap.balloonbox.core.level.physics;

import me.lachlanap.balloonbox.core.level.Entity;

/**
 *
 * @author lachlan
 */
public interface EntityCollisionHandler {

    public void handleContact(Entity a, Entity b);
}
