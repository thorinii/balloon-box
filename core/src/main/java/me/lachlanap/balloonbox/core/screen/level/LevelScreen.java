/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.balloonbox.core.screen.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.lachlanap.balloonbox.core.level.Level;

/**
 *
 * @author lachlan
 */
public class LevelScreen implements Screen {

    private final Level level;
    private final SpriteBatch batch;
    private final Texture boxTexture;
    private final Texture brickTexture;

    public LevelScreen(Level level) {
        this.level = level;
        batch = new SpriteBatch();
        boxTexture = new Texture(Gdx.files.internal("box/box.png"));
        brickTexture = new Texture(Gdx.files.internal("wall/brick.png"));
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(brickTexture, 200,200);
        batch.draw(boxTexture, 100, 100);
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