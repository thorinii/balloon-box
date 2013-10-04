package me.lachlanap.balloonbox.desktop;

import me.lachlanap.balloonbox.core.BalloonBoxGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class BalloonBoxGameDesktop {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1080;
        config.height = 720;
        config.useGL20 = true;
        new LwjglApplication(new BalloonBoxGame(), config);
    }
}
