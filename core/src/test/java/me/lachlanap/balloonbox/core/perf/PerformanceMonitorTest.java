package me.lachlanap.balloonbox.core.perf;

import me.lachlanap.balloonbox.core.perf.PerformanceMonitor.Node;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lachlan
 */
public class PerformanceMonitorTest {

    @Test
    public void testTree() {
        PerformanceMonitor monitor = new PerformanceMonitor();

        monitor.begin("test.long.path.timer");
        monitor.end("test.long.path.timer");
        monitor.begin("test.long.path2.timer");
        monitor.end("test.long.path2.timer");

        Node n = monitor.getTreeRoot();
        System.out.println(n);

        assertEquals("", n.shortName);
        assertNotNull(n.sub("test"));
        assertNotNull(n.sub("test").sub("long"));
        assertNotNull(n.sub("test").sub("long").sub("path"));
        assertNotNull(n.sub("test").sub("long").sub("path").sub("timer"));
        assertNotNull(n.sub("test").sub("long").sub("path2"));
        assertNotNull(n.sub("test").sub("long").sub("path2").sub("timer"));
    }
}
