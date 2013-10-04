package me.lachlanap.balloonbox.core.level;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Score {

    private static final Logger LOG = Logger.getLogger(Score.class.getName());
    public static final int DEFAULT_LIVES = 3;
    private int balloons;
    private int lives;
    private int batteries;

    public Score() {
        balloons = 0;
        lives = DEFAULT_LIVES;
        batteries = 0;
    }

    public void collectBalloon() {
        balloons++;
    }

    public void collectBattery() {
        batteries++;
    }

    public void collectLife() {
        lives++;
    }

    public void takeLife() {
        lives--;
        LOG.log(Level.INFO, "Took a life; now {0} left", new Object[]{lives});
    }

    public int getBalloons() {
        return balloons;
    }

    public int getLives() {
        return lives;
    }

    public int getBatteries() {
        return batteries;
    }
}
