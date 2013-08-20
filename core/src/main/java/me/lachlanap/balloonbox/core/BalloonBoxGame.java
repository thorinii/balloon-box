package me.lachlanap.balloonbox.core;

import com.badlogic.gdx.Game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import me.lachlanap.balloonbox.core.level.EndOfLevelInfo;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.level.loader.LevelLoader;
import me.lachlanap.balloonbox.core.perf.DevToolsWindow;
import me.lachlanap.balloonbox.core.perf.PerformanceMonitor;
import me.lachlanap.balloonbox.core.perf.PerformanceStatViewer;
import me.lachlanap.balloonbox.core.screen.eol.EndOfLevelScreen;
import me.lachlanap.balloonbox.core.screen.level.LevelScreen;
import me.lachlanap.lct.LCTManager;
import me.lachlanap.lct.gui.LCTEditor;

public class BalloonBoxGame extends Game {

    private final PerformanceMonitor performanceMonitor;
    private final DevToolsWindow devToolsWindow;

    public BalloonBoxGame() {

        LCTManager manager = new LCTManager();
        manager.register(Level.class);
        manager.register(EndOfLevelScreen.class);

        performanceMonitor = new PerformanceMonitor();
        devToolsWindow = new DevToolsWindow(manager, performanceMonitor);
    }

    @Override
    public void create() {
        LevelLoader loader = new LevelLoader();
        Level level = loader.loadLevel("level1");

        setScreen(new LevelScreen(this, level, performanceMonitor));
    }

    public void gotoEoLScreen(EndOfLevelInfo info) {
        setScreen(new EndOfLevelScreen(this, info));
    }

    public void gotoNextLevel() {
        System.out.println("Going to next level");
    }

    public void gotoMainMenu() {
    }

    public void toggleDevTools() {
        devToolsWindow.toggleVisibility();
    }
}
