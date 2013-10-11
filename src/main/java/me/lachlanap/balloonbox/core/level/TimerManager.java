package me.lachlanap.balloonbox.core.level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author lachlan
 */
public class TimerManager {

    private final List<Task> tasks;

    public TimerManager() {
        tasks = new ArrayList<>();
    }

    public void update(float tpf) {
        for (Iterator<Task> it = tasks.iterator(); it.hasNext();) {
            Task t = it.next();

            if (t.isReady()) {
                t.execute();
                it.remove();
            }
        }
    }

    public void queue(long millis, Runnable toCreateEntity) {
        tasks.add(new Task(System.currentTimeMillis() + millis, toCreateEntity));
    }

    static class Task {

        final long timeToRunOn;
        final Runnable toExecute;

        Task(long timeToRunOn, Runnable toExecute) {
            this.timeToRunOn = timeToRunOn;
            this.toExecute = toExecute;
        }

        boolean isReady() {
            return System.currentTimeMillis() > timeToRunOn;
        }

        void execute() {
            toExecute.run();
        }
    }
}
