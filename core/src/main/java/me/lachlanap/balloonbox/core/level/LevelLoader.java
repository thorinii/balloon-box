package me.lachlanap.balloonbox.core.level;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author lachlan
 */
public class LevelLoader {

    public Level loadLevel(String mapName) {
        TiledMap map = new TmxMapLoader().load("maps/" + mapName + ".tmx");
        TiledMapTileLayer brickLayer = (TiledMapTileLayer) map.getLayers().get("bricks");

        boolean[][] brickMap = new boolean[brickLayer.getWidth()][brickLayer.getHeight()];
        for (int i = 0; i < brickLayer.getWidth(); i++) {
            for (int j = 0; j < brickLayer.getHeight(); j++) {
                brickMap[i][j] = brickLayer.getCell(i, j) != null;
            }
        }

        map.dispose();

        return new Level(new Vector2(0, -9.81f), brickMap);
    }
}
