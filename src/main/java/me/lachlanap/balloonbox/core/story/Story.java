package me.lachlanap.balloonbox.core.story;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 *
 * @author lachlan
 */
public class Story {

    private final Scene[] scenes;

    public Story(Scene[] scenes) {
        checkArgument(scenes.length > 0, "Cannot have no Scenes in a Story");

        this.scenes = new Scene[scenes.length];
        System.arraycopy(scenes, 0, this.scenes, 0, scenes.length);
    }

    public Scene getScene(int i) {
        return scenes[i];
    }

    public int getSceneCount() {
        return scenes.length;
    }

    public List<Scene> getScenes() {
        return Arrays.asList(scenes);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Story #scenes=").append(scenes.length);

        for (Scene scene : scenes) {
            builder.append('\n').append(" |- ").append(scene);
        }

        builder.append(']');

        return builder.toString();
    }
}
