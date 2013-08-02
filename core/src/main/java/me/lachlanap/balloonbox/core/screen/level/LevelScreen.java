package me.lachlanap.balloonbox.core.screen.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import me.lachlanap.balloonbox.core.BalloonBoxGame;
import me.lachlanap.balloonbox.core.PerformanceMonitor;
import me.lachlanap.balloonbox.core.PerformanceMonitor.StopWatch;
import me.lachlanap.balloonbox.core.level.EndOfLevelInfo;
import me.lachlanap.balloonbox.core.level.Entity;
import me.lachlanap.balloonbox.core.level.EntityType;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.level.Level.StaticLevelData;
import me.lachlanap.balloonbox.core.level.Score;
import me.lachlanap.balloonbox.core.screen.AbstractScreen;

/**
 *
 * @author lachlan
 */
public class LevelScreen extends AbstractScreen {

    public static final float PIXELS_IN_A_METRE = 240f;
    private final Level level;
    private final PerformanceMonitor performanceMonitor;
    private final Viewport viewport;
    private final TextureBook textureBook;
    private float timeSinceLastUpdate;
    private float activeTime;

    public LevelScreen(BalloonBoxGame game, Level level) {
        super(game);
        this.level = level;

        performanceMonitor = new PerformanceMonitor();
        level.setPerformanceMonitor(performanceMonitor);

        viewport = new Viewport(new Vector2(1080, 720));

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


        performanceMonitor.begin("Render");

        viewport.follow(level.getBoxis());
        Vector2 viewportCentre = viewport.getCentre();

        performanceMonitor.begin("Render - Entities");
        batch.begin();
        renderEntities(viewportCentre);
        batch.end();
        performanceMonitor.end("Render - Entities");

        batch.begin();
        renderPipes(viewportCentre);
        renderBricks(viewportCentre);
        renderScore();
        batch.end();

        performanceMonitor.end("Render");

        if (false)
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
                //if (i * PIXELS_IN_A_METRE > viewportCentre.x)
                //    continue;
                //if (j * PIXELS_IN_A_METRE > viewportCentre.y)
                //    continue;

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

        shapeRenderer.end();


        List<StopWatch> stopWatches = new ArrayList<>(performanceMonitor.getData());
        Collections.sort(stopWatches, new Comparator<StopWatch>() {
            @Override
            public int compare(StopWatch o1, StopWatch o2) {
                return o1.name.compareTo(o2.name);
            }
        });


        batch.begin();

        float total = 0;
        for (StopWatch watch : stopWatches) {
            total += watch.avg;
        }

        NumberFormat per = NumberFormat.getNumberInstance();
        per.setMinimumIntegerDigits(2);
        per.setMinimumFractionDigits(1);
        per.setMaximumFractionDigits(1);
        NumberFormat time = NumberFormat.getNumberInstance();
        time.setMinimumIntegerDigits(4);
        time.setMinimumFractionDigits(0);
        time.setMaximumFractionDigits(0);

        int i = 0;
        for (StopWatch watch : stopWatches) {
            fontBig.draw(batch,
                         per.format(100f * watch.avg / total) + "% : "
                    + time.format(1000000 * watch.time) + "us : "
                    + watch.name,
                         10, 30 + i * 30);
            i++;
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