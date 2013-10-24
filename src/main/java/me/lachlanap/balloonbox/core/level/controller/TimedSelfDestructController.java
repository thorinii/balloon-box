package me.lachlanap.balloonbox.core.level.controller;

import me.lachlanap.balloonbox.core.level.Score;

/**
 *
 * @author lachlan
 */
public class TimedSelfDestructController extends Controller {

    private final long timeToRunOn;
    private final Score score;
    private boolean executed = false;

    public TimedSelfDestructController(float time) {
        this.timeToRunOn = System.currentTimeMillis() + (long) (time * 1000);
        this.score = null;
    }

    public TimedSelfDestructController(float time, Score score) {
        this.timeToRunOn = System.currentTimeMillis() + (long) (time * 1000);
        this.score = score;
    }

    @Override
    public void update(float tpf) {
        if (executed)
            return;

        if (System.currentTimeMillis() >= timeToRunOn) {
            if (score != null)
                score.takeLife();
            else
                entity.markForKill();

            executed = true;
        }
    }
}
