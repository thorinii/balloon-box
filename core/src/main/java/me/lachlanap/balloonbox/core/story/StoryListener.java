package me.lachlanap.balloonbox.core.story;

/**
 *
 * @author lachlan
 */
public interface StoryListener {

    public void advanceScene(Scene next);

    public void end();
}
