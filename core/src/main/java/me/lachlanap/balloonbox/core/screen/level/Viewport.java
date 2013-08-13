package me.lachlanap.balloonbox.core.screen.level;

import com.badlogic.gdx.math.Vector2;
import me.lachlanap.balloonbox.core.level.Entity;

/**
 *
 * @author lachlan
 */
public class Viewport {

    public static final float MARGIN = 200f;
    private final Vector2 size;
    private final Vector2 centre;

    public Viewport(Vector2 size) {
        this.size = size;
        centre = new Vector2(size.x / 2, size.y / 2);
    }

    public Vector2 getSize() {
        return size;
    }

    public Vector2 getCentre() {
        return centre;
    }

    public void centreOn(Entity entity) {
        Vector2 epos = entity.getPosition();

        centre.x = -epos.x * LevelScreen.PIXELS_IN_A_METRE + size.x / 2;
        centre.y = -epos.y * LevelScreen.PIXELS_IN_A_METRE + size.y / 2;
    }

    public void follow(Entity entity) {
        Vector2 epos = entity.getPosition();

        if (-centre.x > epos.x * LevelScreen.PIXELS_IN_A_METRE - MARGIN) {
            centre.x = -(epos.x * LevelScreen.PIXELS_IN_A_METRE - MARGIN);
        }
        if (-centre.x < (epos.x * LevelScreen.PIXELS_IN_A_METRE + MARGIN - size.x)) {
            centre.x = -(epos.x * LevelScreen.PIXELS_IN_A_METRE + MARGIN - size.x);
        }

        if (-centre.y > epos.y * LevelScreen.PIXELS_IN_A_METRE - MARGIN) {
            centre.y = -(epos.y * LevelScreen.PIXELS_IN_A_METRE - MARGIN);
        }
        if (-centre.y < (epos.y * LevelScreen.PIXELS_IN_A_METRE + MARGIN - size.y)) {
            centre.y = -(epos.y * LevelScreen.PIXELS_IN_A_METRE + MARGIN - size.y);
        }
    }
}
