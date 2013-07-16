package me.lachlanap.balloonbox.core;

import com.badlogic.gdx.Game;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.level.LevelLoader;
import me.lachlanap.balloonbox.core.screen.level.LevelScreen;

public class BalloonBoxGame extends Game {

    @Override
    public void create() {
        LevelLoader loader = new LevelLoader();
        Level level = loader.loadLevel("firstmap");

        setScreen(new LevelScreen(level));
    }
}
