package me.lachlanap.balloonbox.core.story;

import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author lachlan
 */
public class StoryControllerTest {

    private final Story story = new Story(new Scene[]{
        new LevelScene("level 1"),
        new LevelScene("level 2")
    });

    @Test
    public void testCallsAdvanceSceneWithFirstWhenStarting() {
        StoryController storyController = new StoryController(story);

        StoryListener listener = mock(StoryListener.class);
        storyController.addStoryListener(listener);

        storyController.advance();

        verify(listener).advanceScene(story.getScene(0));
    }

    @Test
    public void testCallsAdvanceSceneWithNextWhenAdvanced() {
        StoryController storyController = new StoryController(story);

        StoryListener listener = mock(StoryListener.class);
        storyController.addStoryListener(listener);

        storyController.advance();
        storyController.advance();

        verify(listener).advanceScene(story.getScene(0));
        verify(listener).advanceScene(story.getScene(1));
    }

    @Test
    public void testCallsEndWhenFinished() {
        StoryController storyController = new StoryController(story);

        StoryListener listener = mock(StoryListener.class);
        storyController.addStoryListener(listener);

        storyController.advance();
        storyController.advance();
        storyController.advance();

        verify(listener).end();
    }

    @Test
    public void testCallsEndOnlyOnce() {
        StoryController storyController = new StoryController(story);

        StoryListener listener = mock(StoryListener.class);
        storyController.addStoryListener(listener);

        storyController.advance();
        storyController.advance();
        storyController.advance();
        storyController.advance();

        verify(listener).end();
    }
}
