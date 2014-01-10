/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.balloonbox.core.level;

import me.lachlanap.balloonbox.core.level.animation.Animation;
import me.lachlanap.balloonbox.core.level.animation.AnimationBuilder;

/**
 *
 * @author lachlan
 */
public class EntityAnimations {

    public static Animation defaultAnimationFor(EntityType type) {
        switch (type) {
            case BOXIS:
                return BOXIS_IDLE;
        }

        return null;
    }

    public static final Animation BOXIS_IDLE = new AnimationBuilder() {
        {
            pushStep(2, image("box/box.png"), image("box/box-eye-centre.png"));
            pushStep(0.3f, image("box/box.png"), image("box/box-eye-blink.png"));
        }
    }.build();


    private EntityAnimations() {
    }
}
