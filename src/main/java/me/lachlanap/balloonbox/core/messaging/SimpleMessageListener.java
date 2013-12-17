package me.lachlanap.balloonbox.core.messaging;

/**
 *
 * @author lachlan
 */
public abstract class SimpleMessageListener implements MessageListener {

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


}
