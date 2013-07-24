package me.lachlanap.balloonbox.core.level;

public class Score {

    public static final int DEFAULT_LIVES = 3;
    private int balloons;
    private int lives;
    private int stars;
    private float time;

    public Score() {
        balloons = 0;
        lives = DEFAULT_LIVES;
        stars = 0;
        time = 0f;
    }

    public void collectBalloon() {
        balloons++;
    }

    public void collectStar() {
        stars++;
    }

    public void collectLife() {
        lives++;
    }

    public void takeLife() {
        lives--;
    }

    public int getBalloons() {
        return balloons;
    }

    public int getLives() {
        return lives;
    }

    public int getStars() {
        return stars;
    }

    public float getTime() {
        return time;
    }
}
