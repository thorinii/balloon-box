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
            case BALLOON:
                return BALLOON;
            case BATTERY:
                return BATTERY;
            case BOXIS:
                return BOXIS_IDLE;
            case SPIKES:
                return SPIKES;
        }

        return null;
    }

    public static float hashShiftFor(Entity e) {
        long hash;
        switch (e.getType()) {
            case BALLOON:
            case BATTERY:
            case SPIKES:
                hash = e.hashCode();
                return ((hash ^ 237287) % 3000) / 7000f;
            default:
                return 0f;
        }
    }

    public static final Animation BALLOON = new AnimationBuilder() {
        {
            /*pushStep(.2f, image("balloon/balloon-1.png"));
             pushStep(.2f, image("balloon/balloon-2.png"));
             pushStep(.2f, image("balloon/balloon-3.png"));
             pushStep(.2f, image("balloon/balloon-4.png"));
             pushStep(.2f, image("balloon/balloon-3.png"));
             pushStep(.2f, image("balloon/balloon-2.png"));*/
            pushStep(INFINITE, image("balloon/balloon-drawn.png"));
        }
    }.build();

    public static final Animation BATTERY = new AnimationBuilder() {
        {
            pushStep(.2f, image("battery/battery-1.png"));
            pushStep(.2f, image("battery/battery-3.png"));
            pushStep(.2f, image("battery/battery-4.png"));
            pushStep(.2f, image("battery/battery-2.png"));
        }
    }.build();

    public static final Animation BOXIS_IDLE = new AnimationBuilder() {
        {
            pushStep(2, image("box/box-centre.png"));
            pushStep(.05f, image("box/box-blink-1.png"));
            pushStep(.2f, image("box/box-blink-2.png"));
        }
    }.build();

    public static final Animation BOXIS_SLIDE_LEFT = new AnimationBuilder() {
        {
            pushStep(.2f, image("box/box-left.png"));
        }
    }.build();

    public static final Animation BOXIS_SLIDE_RIGHT = new AnimationBuilder() {
        {
            pushStep(.2f, image("box/box-right.png"));
        }
    }.build();

    public static final Animation BOXIS_DEAD = new AnimationBuilder() {
        {
            pushStep(INFINITE, image("box/box.png"));
        }
    }.build();


    public static final Animation SPIKES = new AnimationBuilder() {
        {
            pushStep(INFINITE, image("spikes/spikes.png"));
            //pushStep(.1f, image("spikes/spikes-sparkle-1.png"));
            //pushStep(.4f, image("spikes/spikes.png"));
            //pushStep(.1f, image("spikes/spikes-sparkle-2.png"));
        }
    }.build();


    private EntityAnimations() {
    }
}
