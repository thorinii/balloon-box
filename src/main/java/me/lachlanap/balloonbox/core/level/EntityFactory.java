package me.lachlanap.balloonbox.core.level;

import com.badlogic.gdx.math.Vector2;
import me.lachlanap.balloonbox.core.level.controller.KeyboardController;

/**
 *
 * @author lachlan
 */
public class EntityFactory {

    public static final Vector2 BOXIS_SIZE = new Vector2(.3f, .3f);
    public static final Vector2 BRICK_SIZE = new Vector2(.6f, .2f);
    public static final Vector2 BALLOON_SIZE = new Vector2(.256f, .64f);
    public static final Vector2 BATTERY_SIZE = new Vector2(.3f, .14f);
    public static final Vector2 SPIKES_SIZE = new Vector2(.2f, 1f);

    public static Entity makeBoxis(Vector2 pos) {
        Entity boxis = new Entity(false, pos, BOXIS_SIZE, EntityType.BOXIS, true, new EntityAnimationController());
        boxis.addController(new KeyboardController());

        return boxis;
    }

    public static Entity makeBox(Vector2 pos) {
        return new Entity(false, pos, BOXIS_SIZE, EntityType.BOXIS, true, new EntityAnimationController());
    }

    public static Entity makeBrick(Vector2 pos) {
        return new Entity(true, pos, BRICK_SIZE, EntityType.BLOCK, false, new EntityAnimationController());
    }

    public static Entity makeBalloon(Vector2 pos) {
        return new Entity(true, pos, BALLOON_SIZE, EntityType.BALLOON, false, new EntityAnimationController());
    }

    public static Entity makeBattery(Vector2 pos) {
        return new Entity(true, pos, BATTERY_SIZE, EntityType.BATTERY, false, new EntityAnimationController());
    }

    public static Entity makeSpike(Vector2 pos) {
        return new Entity(true, pos, SPIKES_SIZE, EntityType.SPIKES, false, new EntityAnimationController());
    }
}
