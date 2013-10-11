package me.lachlanap.balloonbox.core.screen.eol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import me.lachlanap.balloonbox.core.BalloonBoxGame;
import me.lachlanap.balloonbox.core.level.EndOfLevelInfo;
import me.lachlanap.balloonbox.core.screen.AbstractScreen;

/**
 *
 * @author lachlan
 */
public class EndOfLevelScreen extends AbstractScreen {

    private static final int P_X = 540;
    private static final int P_Y = 470;
    private final String levelName;
    private final boolean won;
    private final Map<String, String> data = new LinkedHashMap<>();

    public EndOfLevelScreen(BalloonBoxGame game, EndOfLevelInfo info) {
        super(game);

        levelName = info.levelName;
        won = info.lives > 0;

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);

        if (info.lives > 0) {
            data.put("Time", nf.format(info.timeSpent) + " seconds");
            data.put("Balloons", nf.format(info.balloons));
            data.put("Lives Left", nf.format(info.lives));
        } else {
            data.put("You", "Failed");
            data.put("Time", nf.format(info.timeSpent) + " seconds");
            data.put("Balloons", nf.format(info.balloons));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        batch.begin();
        fontBig.setColor(Color.BLACK);
        fontBig.drawMultiLine(batch, levelName,
                              P_X, P_Y + 20,
                              0, BitmapFont.HAlignment.CENTER);

        int i = 1;
        for (String key : data.keySet()) {
            fontBig.drawMultiLine(batch, key + ":",
                                  P_X - 10, P_Y - i * fontBig.getLineHeight(),
                                  0, BitmapFont.HAlignment.RIGHT);
            i++;
        }

        fontBig.setColor(Color.RED);
        i = 1;
        for (String value : data.values()) {
            fontBig.drawMultiLine(batch, value,
                                  P_X + 10, P_Y - i * fontBig.getLineHeight(),
                                  0, BitmapFont.HAlignment.LEFT);
            i++;
        }

        batch.end();
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT)
            return true;

        if (screenX < 540) {
            if (won)
                gotoNextMap();
            else
                gotoSameMap();
        } else
            gotoMainMenu();

        return true;
    }

    private void gotoNextMap() {
        game.gotoNextLevel();
    }

    private void gotoSameMap() {
        game.gotoSameLevel();
    }

    private void gotoMainMenu() {
        game.gotoMainMenu();
    }
}
