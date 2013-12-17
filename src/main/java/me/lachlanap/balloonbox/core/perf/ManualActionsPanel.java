package me.lachlanap.balloonbox.core.perf;

import com.badlogic.gdx.Gdx;
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

    public ManualActionsPanel(MessageBus bus) {
        this.messageBus = bus;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        makeAction("Take Life", new Runnable() {
            @Override
            public void run() {
                messageBus.died();
            }
        });

        makeAction("Next Level", new Runnable() {
            @Override
            public void run() {
                messageBus.nextLevel();
            }
        });
        makeAction("Restart Level", new Runnable() {
            @Override
            public void run() {
                messageBus.restartLevel();
            }
        });
        makeAction("Main Menu", new Runnable() {
            @Override
            public void run() {
                messageBus.exitLevel();
            }
        });
    }

    private void makeAction(String title, final Runnable action) {
        JButton actionBtn = new JButton(title);
        actionBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Gdx.app.postRunnable(action);
            }
        });
        add(actionBtn);
    }
}
