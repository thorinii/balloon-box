package me.lachlanap.balloonbox.core.perf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import me.lachlanap.balloonbox.core.messaging.MessageBus;
import me.lachlanap.balloonbox.core.messaging.SimpleMessageListener;
import me.lachlanap.lct.LCTManager;
import me.lachlanap.lct.gui.LCTEditor;

/**
 *
 * @author lachlan
 */
public class DevToolsWindow {

    private final LCTManager lctManager;
    private final PerformanceMonitor performanceMonitor;
    private final JFrame frame;
    private final MessageBus messageBus;

    public DevToolsWindow(MessageBus messageBus, LCTManager lctManager, PerformanceMonitor performanceMonitor) {
        this.messageBus = messageBus;
        this.lctManager = lctManager;
        this.performanceMonitor = performanceMonitor;

        frame = new JFrame("Balloon Box Dev-Tools");
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(500, 900);

        setup();
    }

    private void setup() {
        Container root = frame.getContentPane();
        root.setLayout(new BorderLayout());

        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        root.add(pane, BorderLayout.CENTER);

        LCTEditor lctEditor = new LCTEditor(lctManager);
        pane.add(wrap(lctEditor, "Live Constant Tweaker"));

        ManualActionsPanel manualActionsPanel = new ManualActionsPanel(messageBus);
        pane.add(wrap(manualActionsPanel, "Manual Actions"));

        LogMonitor logMonitor = new LogMonitor();
        pane.add(wrap(logMonitor, "Log Monitor"));

        PerformanceStatViewer statViewer = new PerformanceStatViewer(performanceMonitor);
        pane.add(wrap(statViewer, "Performance Stats"));


        messageBus.addMessageListener(new SimpleMessageListener() {

            @Override
            public void toggleDevTools() {
                frame.setVisible(!frame.isVisible());
            }

        });
    }

    private Component wrap(Component comp, String title) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 1, true), title));
        wrapper.add(comp, BorderLayout.CENTER);

        return wrapper;
    }
}
