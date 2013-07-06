package me.lachlanap.balloonbox.core;

import com.badlogic.gdx.Game;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.screen.level.LevelScreen;

public class BalloonBoxGame extends Game {

    @Override
    public void create() {
        setScreen(new LevelScreen(new Level()));
    }
}
