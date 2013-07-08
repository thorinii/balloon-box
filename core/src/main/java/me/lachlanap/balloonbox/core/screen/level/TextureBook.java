package me.lachlanap.balloonbox.core.screen.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.util.EnumMap;
import me.lachlanap.balloonbox.core.level.EntityType;

/**
 *
 * @author lachlan
 */
public class TextureBook {

    private final EnumMap<EntityType, Texture> entityTextures;

    public TextureBook() {
        entityTextures = new EnumMap<>(EntityType.class);
    }

    public void load() {
        entityTextures.put(EntityType.BOXIS, new Texture(Gdx.files.internal("box/box.png")));
        entityTextures.put(EntityType.BLOCK, new Texture(Gdx.files.internal("wall/bricks.png")));
        entityTextures.put(EntityType.BALLOON, new Texture(Gdx.files.internal("balloon/balloon.png")));
    }

    public Texture getEntityTexture(EntityType type) {
        return entityTextures.get(type);
    }
}
