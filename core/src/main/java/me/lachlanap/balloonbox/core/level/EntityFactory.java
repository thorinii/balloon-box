package me.lachlanap.balloonbox.core.level;

import com.badlogic.gdx.math.Vector2;
import me.lachlanap.balloonbox.core.level.controller.KeyboardController;

/**
 *
 * @author lachlan
 */
public class EntityFactory {

    public static Entity makeBoxis(Vector2 pos) {
        Entity boxis = new Entity(false, pos, new Vector2(.15f, .15f), EntityType.BOXIS, true);
        boxis.addController(new KeyboardController());

        return boxis;
    }

    public static Entity makeBrick(Vector2 pos) {
        return new Entity(true, pos, new Vector2(.3f, .1f), EntityType.BLOCK, false);
    }
}
