/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.balloonbox.core.level;

import com.badlogic.gdx.math.Vector2;
import me.lachlanap.balloonbox.core.level.animation.Animation;

/**
 *
 * @author lachlan
 */
public class LevelAnimationController {

    private float currentAnimationTimer;
    private long lastTime;

    public void update() {
        long now = System.nanoTime();
        float tpf = (now - lastTime) / (1f * 1000 * 1000 * 1000);
        if (lastTime == 0)
            tpf = 0;
        lastTime = now;

        currentAnimationTimer += tpf;
    }

    public Animation.Step getStepOf(Animation animation, Vector2 hash) {
        if (animation.isInfinite())
            return animation.getStep(0);
        else {
            float time = currentAnimationTimer % animation.getTotalTime();
            return animation.getStep(time);
        }
    }

    /**
     * Returns a linearly scrolling value based on the hash.
     */
    public float getLinearValue(float hash, float min, float max, float rate) {
        float range = max - min;

        float rateValue = (currentAnimationTimer + hash * hash * hash) / rate;
        rateValue = rateValue - ((int) rateValue);

        float value = rateValue * range;
        return value + min;
    }
}
