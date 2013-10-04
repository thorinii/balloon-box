package me.lachlanap.balloonbox.core.perf;

import com.google.common.base.Strings;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author lachlan
 */
public class PerformanceMonitor {

    private final Map<String, StopWatch> stopWatches;
    private final StopWatch root;

    public PerformanceMonitor() {
        stopWatches = new HashMap<>();

        root = new StopWatch("");
        stopWatches.put("", root);
    }

    public void begin(String name) {
        StopWatch watch = stopWatches.get(name);

        if (watch == null) {
            watch = makeStopWatch(name);
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

    private StopWatch makeStopWatch(String name) {
        StopWatch watch = new StopWatch(name);

        int lastDot = name.lastIndexOf('.');
        String parentName = (lastDot < 0) ? "" : name.substring(0, lastDot);

        StopWatch parent = stopWatches.get(parentName);
        if (parent == null)
            parent = makeStopWatch(parentName);

        parent.children.put(watch.shortName, watch);
        stopWatches.put(name, watch);

        return watch;
    }

    public StopWatch getTreeRoot() {
        return root;
    }

    public abstract static class Node {

        public final String name;
        public final String shortName;
        public final Map<String, Node> children;

        public Node(String name) {
            this.name = name;
            children = new HashMap<>();

            int lastDot = name.lastIndexOf('.');
            shortName = (lastDot < 0) ? name : name.substring(lastDot + 1);
        }

        public float getTime() {
            float total = 0;
            for (Node n : children.values())
                total += n.getTime();
            return total;
        }

        public float getAverageTime() {
            float total = 0;
            for (Node n : children.values())
                total += n.getAverageTime();
            return total;
        }

        public Node sub(String shortName) {
            return children.get(shortName);
        }

        @Override
        public String toString() {
            return toString(0);
        }

        private String toString(int level) {
            String indent = Strings.repeat(" ", level * 2);

            StringBuilder builder = new StringBuilder();
            builder.append(indent)
                    .append(':')
                    .append(shortName)
                    .append(" (").append(getTime()).append(")\n");

            for (Node node : children.values()) {
                builder.append(node.toString(level + 1));
            }

            return builder.toString();
        }
    }

    public static class StopWatch extends Node {

        private static final int AVERAGE = 70;
        private final float[] avgTable = new float[AVERAGE];
        private int count = 0;
        public long start;
        public float time;
        public float avg;

        public StopWatch(String name) {
            super(name);
        }

        void start() {
            start = System.nanoTime();
        }

        void stop() {
            long nanos = System.nanoTime() - start;
            time = (float) ((double) nanos / 1000000000.0);

            avgTable[count] = time;
            count = (count + 1) % AVERAGE;

            avg = 0;
            for (int i = 0; i < AVERAGE; i++)
                avg += avgTable[i];
            avg = avg / AVERAGE;
        }

        @Override
        public float getTime() {
            if (time == 0)
                return super.getTime();
            return time;
        }

        @Override
        public float getAverageTime() {
            if (avg == 0)
                return super.getAverageTime();
            return avg;
        }
    }
}
