package me.lachlanap.balloonbox.core.level.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import me.lachlanap.balloonbox.core.level.EntityAnimations;

/**
 *
 * @author lachlan
 */
public class KeyboardController extends Controller {

    @Override
    public void update(float tpf) {
        // Going Left
        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            entity.addForce(-0.9f, 0);
            entity.setAnimation(EntityAnimations.BOXIS_SLIDE_LEFT);
        }

        // Going Right
        if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            entity.addForce(0.9f, 0);
            entity.setAnimation(EntityAnimations.BOXIS_SLIDE_RIGHT);
        }

        if (entity.isOnGround())
            if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.SPACE)) {
                entity.shoveY(3.4f);
            }
        if (Gdx.input.isKeyPressed(Keys.F)) {
            entity.addForce(0, 2.7f);
        }

        if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
            entity.addForce(0, -1.5f);
        }
    }
}
