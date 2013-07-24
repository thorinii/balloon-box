package me.lachlanap.balloonbox.core.screen.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import me.lachlanap.balloonbox.core.level.Entity;
import me.lachlanap.balloonbox.core.level.EntityType;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.level.Level.StaticLevelData;
import me.lachlanap.balloonbox.core.level.Score;

/**
 *
 * @author lachlan
 */
public class LevelScreen implements Screen {

    public static final float PIXELS_IN_A_METRE = 240f;
    private final Level level;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final TextureBook textureBook;
    private final BitmapFont font;
    private final Box2DDebugRenderer debugRenderer;
    private float timeSinceLastUpdate;

    public LevelScreen(Level level) {
        this.level = level;

        viewport = new Viewport(new Vector2(1080, 720));

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        debugRenderer = new Box2DDebugRenderer();

        textureBook = new TextureBook();
        textureBook.load();

        font = new BitmapFont(Gdx.files.internal("font/font.fnt"), false);
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

        renderEntities(viewportCentre);
        renderPipes(viewportCentre);
        renderScore();

        batch.end();

        renderBricks(viewportCentre);

        debugRenderer.render(level.getWorld(), batch.getProjectionMatrix());
    }

    private void renderBricks(Vector2 viewportCentre) {
        boolean[][] brickMap = level.getStaticLevelData().brickMap;

        Texture texture = textureBook.getEntityTexture(EntityType.BLOCK);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        for (int i = 0; i < brickMap.length; i++) {
            for (int j = brickMap[0].length - 1; j >= 0; j--) {
                if (!brickMap[i][j])
                    continue;

                /* batch.draw(texture,
                 i * PIXELS_IN_A_METRE / GRID_SCALE - texture.getWidth() / 2 + viewportCentre.x,
                 j * PIXELS_IN_A_METRE / GRID_SCALE - texture.getHeight() / 2 + viewportCentre.y,
                 texture.getWidth() / 2, texture.getHeight() / 2,
                 texture.getWidth(), texture.getHeight(),
                 1, 1,
                 0,
                 0, 0, texture.getWidth(), texture.getHeight(),
                 false, false);*/
                shapeRenderer.rect(i * PIXELS_IN_A_METRE * StaticLevelData.GRID_SCALE + viewportCentre.x,
                                   j * PIXELS_IN_A_METRE * StaticLevelData.GRID_SCALE + viewportCentre.y,
                                   PIXELS_IN_A_METRE * StaticLevelData.GRID_SCALE,
                                   PIXELS_IN_A_METRE * StaticLevelData.GRID_SCALE);
            }
        }
        shapeRenderer.end();
    }

    private void renderEntities(Vector2 viewportCentre) {
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
    }

    private void renderPipes(Vector2 viewportCentre) {
        Texture entryPipe = textureBook.getEntryPipeTexture();
        Vector2 entryPipePosition = level.getStaticLevelData().spawnPoint;
        batch.draw(entryPipe,
                   entryPipePosition.x * PIXELS_IN_A_METRE - entryPipe.getWidth() / 2 + viewportCentre.x,
                   entryPipePosition.y * PIXELS_IN_A_METRE + 200f + viewportCentre.y,
                   entryPipe.getWidth() / 2, entryPipe.getHeight() / 2,
                   entryPipe.getWidth(), entryPipe.getHeight(),
                   1, 1,
                   0,
                   0, 0, entryPipe.getWidth(), entryPipe.getHeight(),
                   false, false);


        Texture exitPipe = textureBook.getEntryPipeTexture();
        Vector2 exitPipePosition = level.getStaticLevelData().exitPoint;
        batch.draw(exitPipe,
                   exitPipePosition.x * PIXELS_IN_A_METRE - exitPipe.getWidth() / 2 + viewportCentre.x,
                   exitPipePosition.y * PIXELS_IN_A_METRE + 200f + viewportCentre.y,
                   exitPipe.getWidth() / 2, exitPipe.getHeight() / 2,
                   exitPipe.getWidth(), exitPipe.getHeight(),
                   1, 1,
                   0,
                   0, 0, exitPipe.getWidth(), exitPipe.getHeight(),
                   false, false);
    }

    private void renderScore() {
        Score score = level.getScore();

        font.draw(batch, String.valueOf(score.getBalloons()), 1000, 680);
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