package me.lachlanap.balloonbox.core.story;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Ignore;

/**
 *
 * @author lachlan
 */
public class StoryLoaderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWithNoLines() {
        StoryLoader.load(new String[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWithEmptyLine() {
        String[] lines = {""};

        StoryLoader.load(lines);
    }

    @Test
    public void testSingleScene() {
        String[] lines = {
            "level level1"
        };

        Story story = StoryLoader.load(lines);

        assertThat(story.getSceneCount(), is(1));
        assertThat(story.getScene(0), is(LevelScene.class));
        assertThat(story.getScene(0).getName(), is("level1"));
    }

    @Test
    public void testMultipleScenes() {
        String[] lines = {
            "level level1",
            "cinematic knife-racks",
            "level level2"
        };

        Story story = StoryLoader.load(lines);

        assertThat(story.getSceneCount(), is(3));

        assertThat(story.getScene(0), is(LevelScene.class));
        assertThat(story.getScene(0).getName(), is("level1"));

        assertThat(story.getScene(1), is(CinematicScene.class));
        assertThat(story.getScene(1).getName(), is("knife-racks"));

        assertThat(story.getScene(2), is(LevelScene.class));
        assertThat(story.getScene(2).getName(), is("level2"));
    }

    @Test
    public void testHandlesCommentsAndBlankLines() {
        String[] lines = {
            "level level1",
            "cinematic knife-racks",
            "",
            "# this is a comment",
            "level level2"
        };

        Story story = StoryLoader.load(lines);

        assertThat(story.getSceneCount(), is(3));

        assertThat(story.getScene(0), is(LevelScene.class));
        assertThat(story.getScene(0).getName(), is("level1"));

        assertThat(story.getScene(1), is(CinematicScene.class));
        assertThat(story.getScene(1).getName(), is("knife-racks"));

        assertThat(story.getScene(2), is(LevelScene.class));
        assertThat(story.getScene(2).getName(), is("level2"));
    }
}
