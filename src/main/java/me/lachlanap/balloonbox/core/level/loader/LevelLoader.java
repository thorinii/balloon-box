package me.lachlanap.balloonbox.core.level.loader;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import me.lachlanap.balloonbox.core.level.Level;
import me.lachlanap.balloonbox.core.level.Level.StaticLevelData;

/**
 *
 * @author lachlan
 */
public class LevelLoader {

    private static final Logger LOG = Logger.getLogger(LevelLoader.class.getName());
    private final TmxMapLoader tmxMapLoader = new TmxMapLoader();

    public Level loadLevel(String mapName) {
        LOG.log(java.util.logging.Level.INFO, "Loading level {0}", mapName);
        TiledMap map = tmxMapLoader.load("maps/" + mapName + ".tmx");


        LOG.info("Unpacking bricks...");
        TiledMapTileLayer brickLayer = (TiledMapTileLayer) map.getLayers().get("bricks");
        final float unitsInGrid = brickLayer.getTileHeight();
        boolean[][] brickMap = loadBricks(brickLayer);


        LOG.info("Processing Point Data");
        MapLayer pointsLayer = map.getLayers().get("points");
        Vector2 spawnPoint = loadVector(unitsInGrid, pointsLayer, "spawn");
        Vector2 exitPoint = loadVector(unitsInGrid, pointsLayer, "exit");

        List<Vector2> balloons = loadVectors(unitsInGrid, map.getLayers().get("balloons"));
        List<Vector2> batteries = loadVectors(unitsInGrid, map.getLayers().get("batteries"));

        map.dispose();


        StaticLevelData staticLevelData = new StaticLevelData(brickMap,
                                                              spawnPoint, exitPoint,
                                                              balloons, batteries);
        return new Level(new Vector2(0, -9.81f), staticLevelData);
    }

    private boolean[][] loadBricks(TiledMapTileLayer brickLayer) {
        boolean[][] brickMap = new boolean[brickLayer.getWidth()][brickLayer.getHeight()];
        for (int i = 0; i < brickLayer.getWidth(); i++) {
            for (int j = 0; j < brickLayer.getHeight(); j++) {
                brickMap[i][j] = brickLayer.getCell(i, j) != null;
            }
        }
        return brickMap;
    }

    private List<Vector2> loadVectors(float unitsInGrid, MapLayer layer) {
        List<Vector2> list = new ArrayList<>();

        if (layer == null)
            LOG.warning("Couldn't load vectors");
        else
            for (MapObject obj : layer.getObjects())
                list.add(loadVector(unitsInGrid, obj));

        return list;
    }

    private Vector2 loadVector(float unitsInGrid, MapLayer layer, String objName) {
        return loadVector(unitsInGrid, layer.getObjects().get(objName));
    }

    private Vector2 loadVector(float unitsInGrid, MapObject obj) {
        MapProperties props = obj.getProperties();
        return new Vector2(props.get("x", Integer.class) / unitsInGrid,
                           props.get("y", Integer.class) / unitsInGrid);
    }
}
