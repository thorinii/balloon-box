/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.balloonbox.core.screen.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import me.lachlanap.balloonbox.core.level.Entity;
import me.lachlanap.balloonbox.core.level.animation.Animation;

import static me.lachlanap.balloonbox.core.screen.level.LevelScreen.PIXELS_IN_A_METRE;

/**
 *
 * @author lachlan
 */
public class AnimatedEntityRenderer {

    private final TextureBook textureBook;
    private final SpriteBatch batch;
    private final Viewport viewport;

    public AnimatedEntityRenderer(TextureBook textureBook, SpriteBatch batch, Viewport viewport) {
        this.textureBook = textureBook;
        this.batch = batch;
        this.viewport = viewport;
    }

    public void render(Entity e) {
        Vector2 viewportCentre = viewport.getCentre();
        Vector2 centre = e.getPosition();

        if (e.getAnimationController().isActive())
            renderAnimatedEntity(e, centre, viewportCentre);
        else
            renderUnanimatedEntity(e, centre, viewportCentre);
    }

    private void renderAnimatedEntity(Entity e, Vector2 centre, Vector2 viewportCentre) {
        Animation.Step step = e.getAnimationController().getCurrentStep();

        for (Animation.Image image : step.images) {
            Texture texture = textureBook.getEntityAnimationTexture(image);
            drawTexture(texture, centre, viewportCentre, e);
        }

    }

    private void renderUnanimatedEntity(Entity e, Vector2 centre, Vector2 viewportCentre) {
        Texture texture = textureBook.getEntityTexture(e.getType());

        drawTexture(texture, centre, viewportCentre, e);
    }

    private void drawTexture(Texture texture, Vector2 centre, Vector2 viewportCentre, Entity e) {
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
