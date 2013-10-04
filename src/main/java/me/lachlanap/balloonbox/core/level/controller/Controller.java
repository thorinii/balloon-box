package me.lachlanap.balloonbox.core.level.controller;

import me.lachlanap.balloonbox.core.level.Entity;

/**
 *
 * @author lachlan
 */
public abstract class Controller {

    protected Entity entity;

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public abstract void update();
}
