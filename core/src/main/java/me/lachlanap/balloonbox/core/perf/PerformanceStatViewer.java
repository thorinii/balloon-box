package me.lachlanap.balloonbox.core.perf;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import me.lachlanap.balloonbox.core.perf.PerformanceMonitor.Node;

/**
 *
 * @author lachlan
 */
public class PerformanceStatViewer extends JPanel {

    //private static final int COLUMN_WIDTH = 50;
    private final PerformanceMonitor monitor;
    private int depth;

    public PerformanceStatViewer(PerformanceMonitor monitor) {
        this.monitor = monitor;
        setBackground(Color.WHITE);

        new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setFont(g.getFont().deriveFont(Font.BOLD));

        int totalHeight = getHeight();
        Node root = monitor.getTreeRoot();

        int base = 0;
        for (Node child : root.children.values()) {
            int childHeight = (int) (totalHeight * (child.getAverageTime() / root.getAverageTime()));
            drawColumn(g, child,
                       base, childHeight, child.getAverageTime() / root.getAverageTime(),
                       0);
            base += childHeight;
        }
    }

    private void drawColumn(Graphics g, Node node,
            int base, int height, float ratio,
            int column) {
        if (depth < column + 1)
            depth = column + 1;

        int width = getWidth() / depth;

        g.setColor(Color.getHSBColor((float) column / depth,
                                     (1 - ratio) / 2 + .5f,
                                     ratio / 2 + .5f));
        g.fillRect(width * column, base, width, height);
        g.setColor(invert(g.getColor()));
        g.drawString(node.shortName + " " + (node.getAverageTime() * 1000000) + "us",
                     width * column + 2, base + 12);

        int newBase = base;
        for (Node child : node.children.values()) {
            if (node.getAverageTime() == 0 || child.getAverageTime() == 0)
                continue;

            int childHeight = (int) (height * (child.getAverageTime() / node.getAverageTime()));

            drawColumn(g, child,
                       newBase, childHeight, child.getAverageTime() / node.getAverageTime(),
                       column + 1);

            newBase += childHeight;
        }
    }

    private static Color invert(Color c) {
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }
}
