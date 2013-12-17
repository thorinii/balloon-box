package me.lachlanap.balloonbox.core.level;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.lachlanap.balloonbox.core.messaging.MessageBus;
import me.lachlanap.balloonbox.core.messaging.SimpleMessageListener;

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

    public Score(MessageBus messageBus) {
        messageBus.addMessageListener(new SimpleMessageListener() {

            @Override
            public void collectedBalloon() {
                Score.this.collectBalloon();
            }

            @Override
            public void collectedBattery() {
                Score.this.collectBattery();
            }

            @Override
            public void collectedLife() {
                Score.this.collectLife();
            }

            @Override
            public void died() {
                Score.this.takeLife();
            }

        });

        balloons = 0;
        lives = DEFAULT_LIVES;
        batteries = 0;

        tookLifeSinceLastUpdate = false;
    }

    private void collectBalloon() {
        balloons++;
    }

    private void collectBattery() {
        batteries++;
    }

    private void collectLife() {
        lives++;
    }

    private void takeLife() {
        lives = Math.max(0, lives - 1);
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
