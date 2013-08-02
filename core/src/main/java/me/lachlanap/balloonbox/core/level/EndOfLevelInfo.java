package me.lachlanap.balloonbox.core.level;

/**
 *
 * @author lachlan
 */
public class EndOfLevelInfo {

    public final String levelName;
    public final float timeSpent;
    public final int balloons;
    public final int lives;

    public EndOfLevelInfo(String levelName, float timeSpent, int balloons, int lives) {
        this.levelName = levelName;
        this.timeSpent = timeSpent;
        this.balloons = balloons;
        this.lives = lives;
    }

    public static EndOfLevelInfo fromLevel(Level level, float timeSpent) {
        return new EndOfLevelInfo("unnamed-level",
                                  timeSpent,
                                  level.getScore().getBalloons(),
                                  level.getScore().getLives());
    }
}
