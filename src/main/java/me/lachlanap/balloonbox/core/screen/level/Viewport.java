package me.lachlanap.balloonbox.core.screen.level;

import com.badlogic.gdx.math.Vector2;
import me.lachlanap.balloonbox.core.level.Entity;
import me.lachlanap.lct.Constant;

/**
 *
 * @author lachlan
 */
public class Viewport {

    @Constant(name = "X Margin", constraints = "0,500")
    public static float X_MARGIN = 300f;
    @Constant(name = "Y Margin", constraints = "0,500")
    public static float Y_MARGIN = 230f;
    //
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

    /**
     * Follows the given entity with {@link X_MARGIN} and {@link Y_MARGIN} border around the screen.
     *
     * If null is passed as the entity, this will have no effect.
     * @param entity
     */
    public void follow(Entity entity) {
        if (entity == null)
            return;

        Vector2 epos = entity.getPosition();

        if (-centre.x > epos.x * LevelScreen.PIXELS_IN_A_METRE - X_MARGIN) {
            centre.x = -(epos.x * LevelScreen.PIXELS_IN_A_METRE - X_MARGIN);
        }
        if (-centre.x < (epos.x * LevelScreen.PIXELS_IN_A_METRE + X_MARGIN - size.x)) {
            centre.x = -(epos.x * LevelScreen.PIXELS_IN_A_METRE + X_MARGIN - size.x);
        }

        if (-centre.y > epos.y * LevelScreen.PIXELS_IN_A_METRE - Y_MARGIN) {
            centre.y = -(epos.y * LevelScreen.PIXELS_IN_A_METRE - Y_MARGIN);
        }
        if (-centre.y < (epos.y * LevelScreen.PIXELS_IN_A_METRE + Y_MARGIN - size.y)) {
            centre.y = -(epos.y * LevelScreen.PIXELS_IN_A_METRE + Y_MARGIN - size.y);
        }
    }
}
