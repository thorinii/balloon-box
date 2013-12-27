package me.lachlanap.balloonbox.core.messaging;

import me.lachlanap.balloonbox.core.level.EndOfLevelInfo;

/**
 *
 * @author lachlan
 */
public class SimpleMessageListener implements MessageListener {

    protected SimpleMessageListener() {
    }

    @Override
    public boolean disconnectTransientListeners() {
        return false;
    }

    @Override
    public void collectedBalloon() {
    }

    @Override
    public void collectedBattery() {
    }

    @Override
    public void collectedLife() {
    }

    @Override
    public void died() {
    }

    @Override
    public void endOfLevel(EndOfLevelInfo info) {
    }

    @Override
    public void nextLevel() {
    }

    @Override
    public void restartLevel() {
    }

    @Override
    public void exitLevel() {
    }

    @Override
    public void toggleDevTools() {
    }
}
