package me.lachlanap.balloonbox.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import me.lachlanap.balloonbox.core.level.EndOfLevelInfo;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.level.loader.LevelLoader;
import me.lachlanap.balloonbox.core.perf.DevToolsWindow;
import me.lachlanap.balloonbox.core.perf.PerformanceMonitor;
import me.lachlanap.balloonbox.core.screen.eol.EndOfLevelScreen;
import me.lachlanap.balloonbox.core.screen.level.LevelScreen;
import me.lachlanap.balloonbox.core.story.LevelScene;
import me.lachlanap.balloonbox.core.story.Scene;
import me.lachlanap.balloonbox.core.story.Story;
import me.lachlanap.balloonbox.core.story.StoryController;
import me.lachlanap.balloonbox.core.story.StoryListener;
import me.lachlanap.balloonbox.core.story.StoryLoader;
import me.lachlanap.lct.LCTManager;

public class BalloonBoxGame extends Game {

    private final PerformanceMonitor performanceMonitor;
    private final DevToolsWindow devToolsWindow;
    private final LevelLoader loader;
    private StoryController storyController;

    public BalloonBoxGame() {
        loader = new LevelLoader();

        LCTManager manager = new LCTManager();
        manager.register(Level.class);
        manager.register(EndOfLevelScreen.class);

        performanceMonitor = new PerformanceMonitor();
        devToolsWindow = new DevToolsWindow(manager, performanceMonitor);
    }

    @Override
    public void create() {
        storyController = new StoryController(loadStory());
        storyController.addStoryListener(new StoryListenerImpl());
        storyController.advance();
    }

    public void gotoEoLScreen(EndOfLevelInfo info) {
        setScreen(new EndOfLevelScreen(this, info));
    }

    public void gotoNextLevel() {
        storyController.advance();
    }

    public void gotoMainMenu() {
        System.out.println("Going to MainMenu");
        setScreen(null);
    }

    public void toggleDevTools() {
        devToolsWindow.toggleVisibility();
    }

    private Story loadStory() {
        Story story = StoryLoader.load(Gdx.files.internal("story.txt"));
        return story;
    }

    private class StoryListenerImpl implements StoryListener {

        @Override
        public void advanceScene(Scene next) {
            if (next instanceof LevelScene) {
                LevelScene levelScene = (LevelScene) next;

                System.out.println("Going to " + levelScene.getName());
                Level level = loader.loadLevel(levelScene.getName());
                setScreen(new LevelScreen(BalloonBoxGame.this, level, performanceMonitor));
            }
        }

        @Override
        public void end() {
            gotoMainMenu();
        }
    }
}
