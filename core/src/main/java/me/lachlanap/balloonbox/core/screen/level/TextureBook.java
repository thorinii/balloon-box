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
    private Texture entryPipeTexture;
    private Texture exitPipeTexture;

    public TextureBook() {
        entityTextures = new EnumMap<>(EntityType.class);
    }

    public void load() {
        entityTextures.put(EntityType.BOXIS, new Texture(Gdx.files.internal("box/box.png")));
        entityTextures.put(EntityType.BLOCK, new Texture(Gdx.files.internal("wall/brick.png")));
        entityTextures.put(EntityType.BALLOON, new Texture(Gdx.files.internal("balloon/balloon.png")));

        entryPipeTexture = new Texture(Gdx.files.internal("pipes/entry.png"));
        exitPipeTexture = new Texture(Gdx.files.internal("pipes/entry.png"));
    }

    public Texture getEntityTexture(EntityType type) {
        return entityTextures.get(type);
    }

    public Texture getEntryPipeTexture() {
        return entryPipeTexture;
    }

    public Texture getExitPipeTexture() {
        return exitPipeTexture;
    }
}
