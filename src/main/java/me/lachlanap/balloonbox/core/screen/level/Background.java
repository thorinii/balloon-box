package me.lachlanap.balloonbox.core.screen.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author lachlan
 */
public class Background {

    public static final float BASE_Y_SHIFT = -750f;
    //
    private static final float[] PARALLAX_SPEEDS = {
        0.3f, 0.5f, 0.7f, 0.75f
    };
    private static final int[] PARALLAX_WIDTH_SCALE = {
        1, 5, 13, 13
    };
    private static final int[] PARALLAX_HEIGHT_SHIFT = {
        0, 1000, 2000, 3000
    };
    //
    private final TextureBook textureBook;
    private final Vector2 spawnPoint;

    public Background(TextureBook textureBook, Vector2 spawnPoint) {
        this.textureBook = textureBook;
        this.spawnPoint = spawnPoint;
    }

    public void draw(SpriteBatch batch, Vector2 viewportCentre) {
        drawBase(viewportCentre, batch);
        drawImages(viewportCentre, batch);
    }

    private void drawBase(Vector2 viewportCentre, SpriteBatch batch) {
        float y = viewportCentre.y;
        y += spawnPoint.y * LevelScreen.PIXELS_IN_A_METRE;
        y += BASE_Y_SHIFT;
        y *= PARALLAX_SPEEDS[0];

        Texture top = textureBook.getBackgroundTop();
        Texture bottom = textureBook.getBackgroundBottom();

        if (y < 0)
            batch.draw(top, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        else
            batch.draw(bottom, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    private void drawImages(Vector2 viewportCentre, SpriteBatch batch) {
        for (int i = 0; i < PARALLAX_SPEEDS.length; i++) {
            Texture background = textureBook.getBackground(i);
            float parallaxSpeed = PARALLAX_SPEEDS[i];

            final int bWidth = background.getWidth() * PARALLAX_WIDTH_SCALE[i];

            Vector2 basePosition = new Vector2();
            basePosition.x = viewportCentre.x * parallaxSpeed;
            basePosition.y = viewportCentre.y;

            basePosition.x = ((int) (basePosition.x) % bWidth - bWidth);
            basePosition.y += spawnPoint.y * LevelScreen.PIXELS_IN_A_METRE;

            basePosition.y += BASE_Y_SHIFT + PARALLAX_HEIGHT_SHIFT[i];
            basePosition.y *= parallaxSpeed;

            while (basePosition.x < -bWidth)
                basePosition.x += bWidth;

            while (basePosition.x <= Gdx.graphics.getWidth()) {
                batch.draw(background, basePosition.x, basePosition.y);
                basePosition.x += bWidth;
            }
        }
    }
}
