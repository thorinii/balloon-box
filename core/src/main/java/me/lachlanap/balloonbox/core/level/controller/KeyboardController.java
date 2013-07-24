package me.lachlanap.balloonbox.core.level.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 *
 * @author lachlan
 */
public class KeyboardController extends Controller {

    @Override
    public void update() {
        // Going Left
        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            entity.addForce(-0.9f, 0);
        }

        // Going Right
        if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            entity.addForce(0.9f, 0);
        }

        if (entity.isOnGround())
            if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.SPACE)) {
                entity.shoveY(2f);
            }
        if (Gdx.input.isKeyPressed(Keys.F)) {
            entity.addForce(0, 2.7f);
        }

        if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
            entity.addForce(0, -1.5f);
        }
    }
}
