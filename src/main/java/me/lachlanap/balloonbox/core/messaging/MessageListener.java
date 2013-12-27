package me.lachlanap.balloonbox.core.messaging;

import me.lachlanap.balloonbox.core.level.EndOfLevelInfo;

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


    public void endOfLevel(EndOfLevelInfo info);

    public void nextLevel();

    public void restartLevel();

    public void exitLevel();


    public void toggleDevTools();
}
