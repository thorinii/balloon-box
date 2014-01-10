/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.balloonbox.core.level.animation;

import java.util.ArrayList;
import java.util.List;
import me.lachlanap.balloonbox.core.level.animation.Animation.Image;
import me.lachlanap.balloonbox.core.level.animation.Animation.Step;

/**
 *
 * @author lachlan
 */
public class AnimationBuilder {

    public static final float INFINITE = 0;

    private final List<Step> steps;

    public AnimationBuilder() {
        this.steps = new ArrayList<>();
    }

    public void pushStep(float length, Image... images) {
        steps.add(new Step(images, length));
    }

    public Image image(String name) {
        return new Image(name);
    }

    public Animation build() {
        return new Animation(steps);
    }
}
