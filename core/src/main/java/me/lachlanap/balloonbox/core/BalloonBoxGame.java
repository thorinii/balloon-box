package me.lachlanap.balloonbox.core;

import com.badlogic.gdx.Game;
import me.lachlanap.balloonbox.core.level.EndOfLevelInfo;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.level.loader.LevelLoader;
import me.lachlanap.balloonbox.core.screen.eol.EndOfLevelScreen;
import me.lachlanap.balloonbox.core.screen.level.LevelScreen;
import me.lachlanap.lct.LCTManager;
import me.lachlanap.lct.gui.LCTFrame;

public class BalloonBoxGame extends Game {

    public BalloonBoxGame() {
        LCTManager manager = new LCTManager();
        manager.register(Level.class);
        manager.register(EndOfLevelScreen.class);

        LCTFrame frame = new LCTFrame(manager);
        frame.setVisible(true);
    }

    @Override
    public void create() {
        LevelLoader loader = new LevelLoader();
        Level level = loader.loadLevel("level1");

        setScreen(new LevelScreen(this, level));
    }

    public void gotoEoLScreen(EndOfLevelInfo info) {
        setScreen(new EndOfLevelScreen(this, info));
    }

    public void gotoNextLevel() {
        System.out.println("Going to next level");
    }
    public void gotoMainMenu(){
        
    }
}
