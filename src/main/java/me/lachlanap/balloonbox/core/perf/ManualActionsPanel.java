package me.lachlanap.balloonbox.core.perf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import me.lachlanap.balloonbox.core.level.Level;

/**
 *
 * @author lachlan
 */
public class ManualActionsPanel extends JPanel {

    private Level level;

    public ManualActionsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton takeLifeBtn = new JButton("Take Life");
        takeLifeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (level != null)
                    level.getScore().takeLife();
            }
        });
        add(takeLifeBtn);
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
