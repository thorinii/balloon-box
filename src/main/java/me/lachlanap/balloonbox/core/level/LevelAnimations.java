/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.balloonbox.core.level;

import me.lachlanap.balloonbox.core.level.animation.Animation;
import me.lachlanap.balloonbox.core.level.animation.AnimationBuilder;

import static me.lachlanap.balloonbox.core.level.animation.AnimationBuilder.INFINITE;

/**
 *
 * @author lachlan
 */
public class LevelAnimations {

    public static final Animation EXIT_FAN_PLATE_OFF = new AnimationBuilder() {
        {
            pushStep(INFINITE, image("pipes/plate-off.png"));
        }
    }.build();

    public static final Animation EXIT_FAN_PLATE_ON = new AnimationBuilder() {
        {
            pushStep(.06f, image("pipes/plate-on-1.png"));
            pushStep(.06f, image("pipes/plate-on-2.png"));
            pushStep(.06f, image("pipes/plate-on-3.png"));
            pushStep(.06f, image("pipes/plate-on-2.png"));
        }
    }.build();

    private LevelAnimations() {
    }
}
