package me.lachlanap.balloonbox.core.screen.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import me.lachlanap.balloonbox.core.level.EntityType;
import me.lachlanap.balloonbox.core.level.animation.Animation;

/**
 *
 * @author lachlan
 */
public class TextureBook {

    private final EnumMap<EntityType, Texture> entityTextures;
    private final Map<String, Texture> entityAnimationTextures;
    //
    private Texture acidTexture;
    //
    private Texture entryPipeTexture;
    private Texture exitPipeTexture;
    //
    private Texture[] backgrounds;
    private Texture backgroundTop;
    private Texture backgroundBottom;

    public TextureBook() {
        entityTextures = new EnumMap<>(EntityType.class);
        entityAnimationTextures = new HashMap<>();
    }

    public void load() {
        entityTextures.put(EntityType.BOXIS, new Texture(Gdx.files.internal("box/box.png")));
        entityTextures.put(EntityType.BLOCK, new Texture(Gdx.files.internal("wall/brick.png")));
        entityTextures.put(EntityType.BALLOON, new Texture(Gdx.files.internal("balloon/balloon.png")));
        entityTextures.put(EntityType.BATTERY, new Texture(Gdx.files.internal("battery/battery.png")));
        entityTextures.put(EntityType.SPIKES, new Texture(Gdx.files.internal("spikes/spikes.png")));

        acidTexture = new Texture(Gdx.files.internal("acid/acid.png"));
        acidTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        entryPipeTexture = new Texture(Gdx.files.internal("pipes/entry.png"));
        exitPipeTexture = new Texture(Gdx.files.internal("pipes/entry.png"));

        backgrounds = new Texture[]{
            new Texture(Gdx.files.internal("backgrounds/background-1.png")),
            new Texture(Gdx.files.internal("backgrounds/background-2.png")),
            new Texture(Gdx.files.internal("backgrounds/background-4.png")),
            new Texture(Gdx.files.internal("backgrounds/background-4.png")),};

        backgroundTop = new Texture(Gdx.files.internal("backgrounds/background-1-top.png"));
        backgroundBottom = new Texture(Gdx.files.internal("backgrounds/background-1-bottom.png"));
    }

    public Texture getEntityTexture(EntityType type) {
        return entityTextures.get(type);
    }

    public Texture getEntityAnimationTexture(Animation.Image image) {
        if (entityAnimationTextures.containsKey(image.name))
            return entityAnimationTextures.get(image.name);
        else {
            Texture tex = new Texture(Gdx.files.internal(image.name));
            entityAnimationTextures.put(image.name, tex);
            return tex;
        }
    }

    public Texture getAcidTexture() {
        return acidTexture;
    }

    public Texture getEntryPipeTexture() {
        return entryPipeTexture;
    }

    public Texture getExitPipeTexture() {
        return exitPipeTexture;
    }

    public Texture getBackground(int index) {
        return backgrounds[index];
    }

    public Texture getBackgroundTop() {
        return backgroundTop;
    }

    public Texture getBackgroundBottom() {
        return backgroundBottom;
    }
}
