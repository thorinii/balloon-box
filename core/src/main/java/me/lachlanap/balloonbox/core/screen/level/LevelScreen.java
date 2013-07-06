package me.lachlanap.balloonbox.core.screen.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.lachlanap.balloonbox.core.level.Entity;
import me.lachlanap.balloonbox.core.level.Level;

/**
 *
 * @author lachlan
 */
public class LevelScreen implements Screen {

    public static final float PIXELS_IN_A_METRE = 250f;
    private final Level level;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private final TextureBook textureBook;
    private float timeSinceLastUpdate;

    public LevelScreen(Level level) {
        this.level = level;

        viewport = new Viewport(new Vector2(1080, 720));

        batch = new SpriteBatch();
        
        textureBook = new TextureBook();
        textureBook.load();
    }

    @Override
    public void render(float tpf) {
        if (timeSinceLastUpdate < 1 / 60f)
            timeSinceLastUpdate += tpf;
        else {
            level.update();
            timeSinceLastUpdate = 0;
        }

        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        batch.begin();

        viewport.follow(level.getBoxis());
        Vector2 viewportCentre = viewport.getCentre();

        for (Entity e : level.getEntities()) {
            Vector2 centre = e.getPosition();
            Texture texture = textureBook.getEntityTexture(e.getType());

            batch.draw(texture,
                       centre.x * PIXELS_IN_A_METRE - texture.getWidth() / 2 + viewportCentre.x,
                       centre.y * PIXELS_IN_A_METRE - texture.getHeight() / 2 + viewportCentre.y,
                       texture.getWidth() / 2, texture.getHeight() / 2,
                       texture.getWidth(), texture.getHeight(),
                       1, 1,
                       e.getAngle(),
                       0, 0, texture.getWidth(), texture.getHeight(),
                       false, false);
        }
        batch.end();
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void dispose() {
    }
}