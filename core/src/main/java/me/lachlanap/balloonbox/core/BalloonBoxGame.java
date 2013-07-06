package me.lachlanap.balloonbox.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.screen.level.LevelScreen;

public class BalloonBoxGame extends Game {

    @Override
    public void create() {
        setScreen(new LevelScreen(new Level(new Vector2(0, -9.81f))));
    }
}
