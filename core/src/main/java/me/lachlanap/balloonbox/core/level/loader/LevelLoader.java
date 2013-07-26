package me.lachlanap.balloonbox.core.level.loader;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.level.Level.StaticLevelData;

/**
 *
 * @author lachlan
 */
public class LevelLoader {

    private static final float UNITS_IN_GRID = 32f;

    public Level loadLevel(String mapName) {
        System.out.println("Loading level " + mapName + "...");

        TiledMap map = new TmxMapLoader().load("maps/" + mapName + ".tmx");
        TiledMapTileLayer brickLayer = (TiledMapTileLayer) map.getLayers().get("bricks");


        System.out.println("Collecting bricks...");
        boolean[][] brickMap = new boolean[brickLayer.getWidth()][brickLayer.getHeight()];
        for (int i = 0; i < brickLayer.getWidth(); i++) {
            for (int j = 0; j < brickLayer.getHeight(); j++) {
                brickMap[i][j] = brickLayer.getCell(i, j) != null;
            }
        }


        System.out.println("Loading points");
        MapLayer pointsLayer = map.getLayers().get("points");
        MapProperties spawnProps = pointsLayer.getObjects().get("spawn").getProperties();
        Vector2 spawnPoint = new Vector2(spawnProps.get("x", Integer.class) / UNITS_IN_GRID,
                                         spawnProps.get("y", Integer.class) / UNITS_IN_GRID);

        MapProperties exitProps = pointsLayer.getObjects().get("exit").getProperties();
        Vector2 exitPoint = new Vector2(exitProps.get("x", Integer.class) / UNITS_IN_GRID,
                                        exitProps.get("y", Integer.class) / UNITS_IN_GRID);


        System.out.println("Loading balloons");
        MapLayer balloonsLayer = map.getLayers().get("balloons");
        List<Vector2> balloons = new ArrayList<>();
        for (MapObject obj : balloonsLayer.getObjects()) {
            MapProperties objProps = obj.getProperties();
            Vector2 pos = new Vector2(objProps.get("x", Integer.class) / UNITS_IN_GRID,
                                      objProps.get("y", Integer.class) / UNITS_IN_GRID);

            balloons.add(pos);
        }

        map.dispose();

        StaticLevelData staticLevelData = new StaticLevelData(brickMap, spawnPoint, exitPoint, balloons);
        return new Level(new Vector2(0, -9.81f), staticLevelData);
    }
}
