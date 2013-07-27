package me.lachlanap.balloonbox.core;

import com.badlogic.gdx.Game;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.level.loader.LevelLoader;
import me.lachlanap.balloonbox.core.screen.level.LevelScreen;
import me.lachlanap.lct.LCTManager;
import me.lachlanap.lct.gui.LCTFrame;

public class BalloonBoxGame extends Game {

    public BalloonBoxGame() {
        LCTManager manager = new LCTManager();
        manager.register(Level.class);

        LCTFrame frame = new LCTFrame(manager);
        frame.setVisible(true);
    }

    @Override
    public void create() {
        LevelLoader loader = new LevelLoader();
        Level level = loader.loadLevel("the bigmap");

        setScreen(new LevelScreen(level));
    }
}
