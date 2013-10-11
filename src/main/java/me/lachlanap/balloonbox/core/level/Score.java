package me.lachlanap.balloonbox.core.level;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Score {

    private static final Logger LOG = Logger.getLogger(Score.class.getName());
    //
    public static final int DEFAULT_LIVES = 3;
    public static final int TIME_TO_RESPAWN = 2;
    //
    private int balloons;
    private int lives;
    private int batteries;
    //
    private boolean tookLifeSinceLastUpdate;

    public Score() {
        balloons = 0;
        lives = DEFAULT_LIVES;
        batteries = 0;

        tookLifeSinceLastUpdate = false;
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
        tookLifeSinceLastUpdate = true;
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

    public void update(Entity e, TimerManager timerManager, Runnable toCreateEntity) {
        if (tookLifeSinceLastUpdate) {
            if (e != null) {
                e.markForKill();
                timerManager.queue(TIME_TO_RESPAWN * 1000, toCreateEntity);
            }
        }

        tookLifeSinceLastUpdate = false;
    }
}
