package me.lachlanap.balloonbox.core.messaging;

/**
 *
 * @author lachlan
 */
public interface MessageListener {

    public boolean disconnectTransientListeners();


    public void collectedBalloon();

    public void collectedBattery();

    public void collectedLife();


    public void died();
}
