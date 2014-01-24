/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.balloonbox.core.level;

import me.lachlanap.balloonbox.core.level.animation.Animation;

/**
 *
 * @author lachlan
 */
public class EntityAnimationController {

    private Animation defaultAnimation;

    private Animation current;
    private float shift;
    private float currentAnimationTimer;

    /**
     * The last considered time, for tpf purposes.
     */
    private long lastTime;

    private boolean doShift;

    public void setDefaultAnimation(Animation newDefault) {
        defaultAnimation = newDefault;
        current = defaultAnimation;
    }

    public void setShift(float shift) {
        this.shift = shift;
        doShift = true;
    }

    public void resetAnimation() {
        setAnimation(defaultAnimation);
    }

    public void setAnimation(Animation animation) {
        current = animation;
        currentAnimationTimer = 0;
    }

    public void update() {
        if (current == null)
            return;

        long now = System.nanoTime();
        float tpf = (now - lastTime) / (1f * 1000 * 1000 * 1000);
        lastTime = now;

        currentAnimationTimer += tpf + ((doShift) ? shift / 10f : 0);
        doShift = false;

        if (!current.isInfinite()
            && currentAnimationTimer >= current.getTotalTime() + shift)
            resetAnimation();
    }

    public boolean isActive() {
        return current != null;
    }

    public Animation getCurrent() {
        return current;
    }

    public Animation.Step getCurrentStep() {
        return current.getStep(currentAnimationTimer);
    }
}
