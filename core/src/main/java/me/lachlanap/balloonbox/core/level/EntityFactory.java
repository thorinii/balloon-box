package me.lachlanap.balloonbox.core.level;

import com.badlogic.gdx.math.Vector2;
import me.lachlanap.balloonbox.core.level.controller.KeyboardController;

/**
 *
 * @author lachlan
 */
public class EntityFactory {

    private static final Vector2 BOXIS_EXTENTS = new Vector2(.3f, .3f);
    private static final Vector2 BRICK_EXTENTS = new Vector2(.6f, .2f);
    private static final Vector2 BALLOON_EXTENTS = new Vector2(.256f, .64f);

    public static Entity makeBoxis(Vector2 pos) {
        Entity boxis = new Entity(false, pos, BOXIS_EXTENTS, EntityType.BOXIS, true);
        boxis.addController(new KeyboardController());

        return boxis;
    }

    public static Entity makeBrick(Vector2 pos) {
        return new Entity(true, pos, BRICK_EXTENTS, EntityType.BLOCK, false);
    }

    public static Entity makeBalloon(Vector2 pos) {
        return new Entity(true, pos, BALLOON_EXTENTS, EntityType.BALLOON, false);
    }
}
