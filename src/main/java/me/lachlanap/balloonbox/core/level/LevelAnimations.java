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

    public static final Animation ACID = new AnimationBuilder() {
        {
            pushStep(INFINITE, image("acid/acid.png"));
        }
    }.build();

    private LevelAnimations() {
    }
}
