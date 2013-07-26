package me.lachlanap.balloonbox.core;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author lachlan
 */
public class PerformanceMonitor {

    private final Map<String, StopWatch> stopWatches;

    public PerformanceMonitor() {
        stopWatches = new HashMap<>();
    }

    public void begin(String name) {
        StopWatch watch = stopWatches.get(name);

        if (watch == null) {
            watch = new StopWatch(name);
            stopWatches.put(name, watch);
        }

        watch.start();
    }

    public void end(String name) {
        StopWatch watch = stopWatches.get(name);

        if (watch == null) {
            return;
        }

        watch.stop();
    }

    public Collection<StopWatch> getData() {
        return stopWatches.values();
    }

    public static class StopWatch {

        private static final int SKIP = 50;
        public final String name;
        private final float[] avgTable = new float[SKIP];
        private int count = 0;
        public long start;
        public float time;
        public float avg;

        public StopWatch(String name) {
            this.name = name;
        }

        void start() {
            start = System.nanoTime();
        }

        void stop() {
            long nanos = System.nanoTime() - start;
            time = (float) ((double) nanos / 1000000000.0);

            avgTable[count] = time;
            count = (count + 1) % SKIP;

            avg = 0;
            for (int i = 0; i < SKIP; i++)
                avg += avgTable[i];
            avg = avg / SKIP;
        }
    }
}
