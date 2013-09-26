package me.lachlanap.balloonbox.core;

import me.lachlanap.balloonbox.core.lctext.BooleanConstantFieldProvider;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import java.util.logging.Logger;
import me.lachlanap.balloonbox.core.level.EndOfLevelInfo;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.level.loader.LevelLoader;
import me.lachlanap.balloonbox.core.perf.DevToolsWindow;
import me.lachlanap.balloonbox.core.perf.PerformanceMonitor;
import me.lachlanap.balloonbox.core.screen.AbstractScreen;
import me.lachlanap.balloonbox.core.screen.eol.EndOfLevelScreen;
import me.lachlanap.balloonbox.core.screen.level.Background;
import me.lachlanap.balloonbox.core.screen.level.LevelScreen;
import me.lachlanap.balloonbox.core.story.LevelScene;
import me.lachlanap.balloonbox.core.story.Scene;
import me.lachlanap.balloonbox.core.story.Story;
import me.lachlanap.balloonbox.core.story.StoryController;
import me.lachlanap.balloonbox.core.story.StoryListener;
import me.lachlanap.balloonbox.core.story.StoryLoader;
import me.lachlanap.lct.LCTManager;
import me.lachlanap.lct.data.ConstantFieldFactory;

public class BalloonBoxGame extends Game {

    private static final Logger LOG = Logger.getLogger(BalloonBoxGame.class.getName());
    private final PerformanceMonitor performanceMonitor;
    private final DevToolsWindow devToolsWindow;
    private final LevelLoader loader;
    private StoryController storyController;

    public BalloonBoxGame() {
        loader = new LevelLoader();

        LCTManager manager = setupLCTManager();

        performanceMonitor = new PerformanceMonitor();
        devToolsWindow = new DevToolsWindow(manager, performanceMonitor);
    }

    private LCTManager setupLCTManager() {
        ConstantFieldFactory cff = new ConstantFieldFactory();
        cff.addProvider(new BooleanConstantFieldProvider());

        LCTManager manager = new LCTManager(cff);

        manager.register(Level.class);
        manager.register(LevelScreen.class);
        manager.register(EndOfLevelScreen.class);
        manager.register(Background.class);

        return manager;
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
        LOG.info("Going to MainMenu");
        setScreen(new AbstractScreen(this) {
        });
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
            LOG.log(java.util.logging.Level.INFO, "Going to next scene: {0}", next);

            if (next instanceof LevelScene) {
                LevelScene levelScene = (LevelScene) next;

                Level level = loader.loadLevel(levelScene.getName());
                setScreen(new LevelScreen(BalloonBoxGame.this, level, performanceMonitor));
            }
        }

        @Override
        public void end() {
            LOG.log(java.util.logging.Level.INFO, "Finished game");

            gotoMainMenu();
        }
    }
}
