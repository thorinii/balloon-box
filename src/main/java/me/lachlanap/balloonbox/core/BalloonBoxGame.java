package me.lachlanap.balloonbox.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import me.lachlanap.balloonbox.core.level.EndOfLevelInfo;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.level.loader.LevelLoader;
import me.lachlanap.balloonbox.core.messaging.MessageBus;
import me.lachlanap.balloonbox.core.messaging.SimpleMessageListener;
import me.lachlanap.balloonbox.core.perf.DevToolsWindow;
import me.lachlanap.balloonbox.core.perf.PerformanceMonitor;
import me.lachlanap.balloonbox.core.screen.AbstractScreen;
import me.lachlanap.balloonbox.core.screen.eol.EndOfLevelScreen;
import me.lachlanap.balloonbox.core.screen.level.LevelScreen;
import me.lachlanap.balloonbox.core.story.*;
import me.lachlanap.lct.Constant;
import me.lachlanap.lct.LCTManager;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class BalloonBoxGame extends Game {

    private static final Logger LOG = Logger.getLogger(BalloonBoxGame.class.getName());
    private final MessageBus messageBus;
    private final PerformanceMonitor performanceMonitor;
    private final LevelLoader loader;
    private StoryController storyController;

    public BalloonBoxGame() {
        messageBus = new MessageBus();
        messageBus.addMessageListener(new ScreenMessageListener());

        loader = new LevelLoader(messageBus);

        LCTManager manager = setupLCTManager();

        performanceMonitor = new PerformanceMonitor();
        DevToolsWindow devToolsWindow = new DevToolsWindow(messageBus, manager, performanceMonitor);
    }

    private LCTManager setupLCTManager() {
        LCTManager manager = new LCTManager();

        /* Magic to get all the constants */
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("me.lachlanap.balloonbox.core"))
                .setScanners(new SubTypesScanner(), new FieldAnnotationsScanner()));

        Set<Field> fields = reflections.getFieldsAnnotatedWith(Constant.class);
        Set<Class> klasses = new HashSet<>();
        for (Field f : fields)
            klasses.add(f.getDeclaringClass());
        for (Class k : klasses)
            manager.register(k);

        return manager;
    }

    @Override
    public void create() {
        storyController = new StoryController(loadStory());
        storyController.addStoryListener(new StoryListenerImpl());
        storyController.advance();
    }

    private class ScreenMessageListener extends SimpleMessageListener {

        @Override
        public void endOfLevel(EndOfLevelInfo info) {
            messageBus.disconnectTransientListeners();
            setScreen(new EndOfLevelScreen(messageBus, info));
        }

        @Override
        public void nextLevel() {
            storyController.advance();
        }

        @Override
        public void restartLevel() {
            storyController.again();
        }

        @Override
        public void exitLevel() {
            LOG.info("Going to MainMenu");
            setScreen(new AbstractScreen(messageBus) {
            });
        }

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
                setScreen(new LevelScreen(messageBus, level, performanceMonitor));
            }
        }

        @Override
        public void end() {
            LOG.log(java.util.logging.Level.INFO, "Finished game");

            messageBus.exitLevel();
        }
    }
}
