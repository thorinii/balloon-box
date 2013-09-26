package me.lachlanap.balloonbox.core.screen.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.lachlanap.lct.Constant;

/**
 *
 * @author lachlan
 */
public class Background {

    @Constant(name = "Y Shift", constraints = "-1000,1000")
    public static float Y_SHIFT = -750f;
    @Constant(name = "Parallax Speed", constraints = "0,1")
    public static float PARALLAX_SPEED = 0.6f;
    @Constant(name = "Draw Background Image")
    public static boolean DRAW_BACKGROUND_IMAGE = true;
    //
    private final TextureBook textureBook;
    private final Vector2 spawnPoint;

    public Background(TextureBook textureBook, Vector2 spawnPoint) {
        this.textureBook = textureBook;
        this.spawnPoint = spawnPoint;
    }

    public void draw(SpriteBatch batch, Vector2 viewportCentre) {
        Texture background = textureBook.getBackground1();

        final int bWidth = background.getWidth();

        Vector2 basePosition = new Vector2();
        basePosition.x = viewportCentre.x * PARALLAX_SPEED;
        basePosition.y = viewportCentre.y;

        basePosition.x = ((int) (basePosition.x) % bWidth - bWidth);
        basePosition.y = basePosition.y + spawnPoint.y * LevelScreen.PIXELS_IN_A_METRE;

        basePosition.y += Y_SHIFT;
        basePosition.y *= PARALLAX_SPEED;

        Texture top = textureBook.getBackground1Top();
        Texture bottom = textureBook.getBackground1Bottom();

        if (basePosition.y < 0)
            batch.draw(top, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        else
            batch.draw(bottom, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (DRAW_BACKGROUND_IMAGE) {
            while (basePosition.x < -bWidth)
                basePosition.x += bWidth;
            while (basePosition.x <= Gdx.graphics.getWidth()) {
                batch.draw(background, basePosition.x, basePosition.y);
                basePosition.x += bWidth;
            }
        }
    }
}
