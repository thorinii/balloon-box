package me.lachlanap.balloonbox.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.lachlanap.balloonbox.core.BalloonBoxGame;

public class BalloonBoxGameDesktop {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1080;
        config.height = 720;
        config.useGL20 = true;
        config.foregroundFPS = 60;
        new LwjglApplication(new BalloonBoxGame(), config);
    }
}
