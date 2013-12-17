package me.lachlanap.balloonbox.core.perf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import me.lachlanap.balloonbox.core.messaging.MessageBus;

/**
 *
 * @author lachlan
 */
public class ManualActionsPanel extends JPanel {

    private final MessageBus messageBus;

    public ManualActionsPanel(MessageBus messageBus) {
        this.messageBus = messageBus;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton takeLifeBtn = new JButton("Take Life");
        takeLifeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManualActionsPanel.this.messageBus.died();
            }
        });
        add(takeLifeBtn);
    }
}
