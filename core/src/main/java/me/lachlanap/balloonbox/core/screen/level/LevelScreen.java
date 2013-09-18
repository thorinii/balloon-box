package me.lachlanap.balloonbox.core.screen.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.lachlanap.balloonbox.core.BalloonBoxGame;
import me.lachlanap.balloonbox.core.level.EndOfLevelInfo;
import me.lachlanap.balloonbox.core.level.Entity;
import me.lachlanap.balloonbox.core.level.EntityType;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.level.Level.StaticLevelData;
import me.lachlanap.balloonbox.core.level.Score;
import me.lachlanap.balloonbox.core.level.physics.SensorManager.Sensor;
import me.lachlanap.balloonbox.core.perf.PerformanceMonitor;
import me.lachlanap.balloonbox.core.screen.AbstractScreen;
import me.lachlanap.lct.Constant;

/**
 *
 * @author lachlan
 */
public class LevelScreen extends AbstractScreen {

    public static final float PIXELS_IN_A_METRE = 240f;
    @Constant(name = "Debug")
    public static boolean DEBUG = false;
    private final Level level;
    private final PerformanceMonitor performanceMonitor;
    private final Viewport viewport;
    private final TextureBook textureBook;
    private float timeSinceLastUpdate;
    private float activeTime;

    public LevelScreen(BalloonBoxGame game, Level level, PerformanceMonitor performanceMonitor) {
        super(game);
        this.level = level;

        this.performanceMonitor = performanceMonitor;
        level.setPerformanceMonitor(performanceMonitor);

        viewport = new Viewport(new Vector2(1080, 720));
        viewport.centreOn(level.getBoxis());

        textureBook = new TextureBook();
        textureBook.load();

        activeTime = 0;
    }

    @Override
    public void render(float tpf) {
        activeTime += tpf;
        if (timeSinceLastUpdate < 1 / 60f)
            timeSinceLastUpdate += tpf;
        else {
            level.update();
            timeSinceLastUpdate = 0;
        }

        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);


        performanceMonitor.begin("render");

        viewport.follow(level.getBoxis());
        Vector2 viewportCentre = viewport.getCentre();

        performanceMonitor.begin("render.entities");
        batch.begin();
        renderEntities(viewportCentre);
        batch.end();
        performanceMonitor.end("render.entities");

        performanceMonitor.begin("render.misc");
        batch.begin();
        renderPipes(viewportCentre);
        renderBricks(viewportCentre);
        renderScore();
        batch.end();
        performanceMonitor.end("render.misc");

        performanceMonitor.end("render");

        if (DEBUG)
            renderDebug(viewportCentre);

        if (level.isGameover())
            game.gotoEoLScreen(EndOfLevelInfo.fromLevel(
                    level,
                    activeTime));
    }

    private void renderBricks(Vector2 viewportCentre) {
        boolean[][] brickMap = level.getStaticLevelData().brickMap;

        Texture texture = textureBook.getEntityTexture(EntityType.BLOCK);

        for (int i = 0; i < brickMap.length; i++) {
            for (int j = brickMap[0].length - 1; j >= 0; j--) {
                if (!brickMap[i][j])
                    continue;

                batch.draw(texture,
                           i * PIXELS_IN_A_METRE * StaticLevelData.GRID_SCALE + viewportCentre.x,
                           j * PIXELS_IN_A_METRE * StaticLevelData.GRID_SCALE + viewportCentre.y,
                           texture.getWidth() / 2, texture.getHeight() / 2,
                           PIXELS_IN_A_METRE * StaticLevelData.GRID_SCALE,
                           PIXELS_IN_A_METRE * StaticLevelData.GRID_SCALE,
                           1, 1,
                           0,
                           0, 0, texture.getWidth(), texture.getHeight(),
                           false, false);
            }
        }
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

        fontBig.draw(batch, score.getBatteries() + "/" + level.getStaticLevelData().batteries.size(), 900, 680);
        fontBig.draw(batch, String.valueOf(score.getBalloons()), 1000, 680);
    }

    private void renderDebug(Vector2 viewportCentre) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(.1f, .3f, .8f, 1);
        for (Entity e : level.getEntities()) {
            Vector2 centre = e.getPosition();
            Vector2 size = e.getSize();

            shapeRenderer.rect((centre.x - size.x / 2) * PIXELS_IN_A_METRE + viewportCentre.x,
                               (centre.y - size.y / 2) * PIXELS_IN_A_METRE + viewportCentre.y,
                               size.x * PIXELS_IN_A_METRE,
                               size.y * PIXELS_IN_A_METRE);
        }

        shapeRenderer.setColor(.1f, .8f, .2f, 1);
        for (Sensor s : level.getSensorManager().getSensors()) {
            Vector2 centre = s.getPosition();
            Vector2 size = s.getExtents();

            shapeRenderer.rect((centre.x - size.x) * PIXELS_IN_A_METRE + viewportCentre.x,
                               (centre.y - size.y) * PIXELS_IN_A_METRE + viewportCentre.y,
                               size.x * 2 * PIXELS_IN_A_METRE,
                               size.y * 2 * PIXELS_IN_A_METRE);
        }


        shapeRenderer.end();
    }
}
