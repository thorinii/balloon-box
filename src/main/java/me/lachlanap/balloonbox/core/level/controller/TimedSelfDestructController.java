package me.lachlanap.balloonbox.core.level.controller;

import me.lachlanap.balloonbox.core.level.Score;

/**
 *
 * @author lachlan
 */
public class TimedSelfDestructController extends Controller {

    private final float time;
    private final Score score;
    private float ticks;
    private boolean executed = false;

    public TimedSelfDestructController(float time) {
        this.time = time;
        this.score = null;
    }

    public TimedSelfDestructController(float time, Score score) {
        this.time = time;
        this.score = score;
    }

    @Override
    public void update(float tpf) {
        ticks += tpf;

        if (executed)
            return;

        if (ticks >= time) {
            if (score != null)
                score.takeLife();
            else
                entity.markForKill();

            executed = true;
        }
    }
}
