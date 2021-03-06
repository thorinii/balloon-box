package me.lachlanap.balloonbox.core.perf;

import java.awt.BorderLayout;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author lachlan
 */
public class LogMonitor extends JPanel {

    private final Logger LOG = Logger.getLogger("me.lachlanap.balloonbox");
    private final JTextArea logOutput;

    public LogMonitor() {
        setLayout(new BorderLayout());

        logOutput = new JTextArea(5, 0);
        logOutput.setLineWrap(true);

        JScrollPane pane = new JScrollPane(logOutput,
                                           JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                           JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(pane, BorderLayout.CENTER);

        setupLog();
    }

    private void setupLog() throws SecurityException {
        LOG.addHandler(new LogHandler());
    }

    private class LogHandler extends Handler {

        final Formatter formatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                String message = formatMessage(record);
                if (record.getThrown() == null)
                    return record.getLevel().getLocalizedName()
                            + " (" + record.getThreadID() + "): "
                            + message
                            + " [" + record.getMillis() + "]";
                else {
                    Throwable t = record.getThrown();
                    return record.getLevel().getLocalizedName()
                            + " (" + record.getThreadID() + "): "
                            + t.getClass().getName() + ": "
                            + message + ": "
                            + t.getMessage()
                            + " [" + record.getMillis() + "]";
                }
            }
        };

        @Override
        public void publish(final LogRecord record) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    String line = formatter.format(record);
                    logOutput.setText(line + "\n" + logOutput.getText());
                    logOutput.setCaretPosition(0);
                }
            });
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }
}
