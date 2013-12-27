package me.lachlanap.balloonbox.core.story;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lachlan
 */
public class StoryController {

    private final Story story;
    private final List<StoryListener> listeners = new ArrayList<>();
    private int index;

    public StoryController(Story story) {
        this.story = story;
        index = -1;
    }

    public void advance() {
        index++;

        if (index == story.getSceneCount())
            end();
        else if (index < story.getSceneCount())
            advanceScene(story.getScene(index));
    }

    public void again() {
        if (index == story.getSceneCount())
            end();
        else if (index < story.getSceneCount())
            advanceScene(story.getScene(index));
    }

    private void end() {
        for (StoryListener listener : listeners)
            listener.end();
    }

    private void advanceScene(Scene scene) {
        for (StoryListener listener : listeners)
            listener.advanceScene(scene);
    }

    public void addStoryListener(StoryListener storyListener) {
        listeners.add(storyListener);
    }

    public void removeStoryListener(StoryListener storyListener) {
        listeners.remove(storyListener);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[StoryController #listeners=").append(listeners.size());
        builder.append(" i=").append(index).append('\n');
        builder.append(story);
        builder.append(']');

        return builder.toString();
    }
}
