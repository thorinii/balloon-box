package me.lachlanap.balloonbox.core.level.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a list of paths/chains from a grid of boolean squares.
 * <p/>
 * @author lachlan
 */
public class GridToChainFactory {

    private static final int G_EMPTY = 0;
    private static final int G_FILLED = 1;
    private static final int G_USED = 2;

    /**
     * Makes a Box2D chain from the output of {@link #makePath(boolean[][]) }.
     */
    public static ChainShape[] makeChainShapes(boolean[][] g, float scale) {
        List<List<Vector2>> pointChains = makePath(g);

        List<ChainShape> chains = new ArrayList<>();

        for (List<Vector2> pointChain : pointChains) {
            for (Vector2 v : pointChain)
                v.scl(scale);

            ChainShape chain = new ChainShape();
            chain.createLoop(pointChain.toArray(new Vector2[pointChain.size()]));
            chains.add(chain);
        }

        return chains.toArray(new ChainShape[chains.size()]);
    }

    /**
     * Makes a list of lists of points (counter-clockwise) that define a path around the
     * specified grid.
     */
    public static List<List<Vector2>> makePath(boolean[][] originalGrid) {
        int[][] working = new int[originalGrid.length][originalGrid[0].length];

        // Setup the working copy - all 'on' cells are set to be filled
        for (int i = 0; i < working.length; i++) {
            for (int j = 0; j < working[i].length; j++) {
                if (originalGrid[i][j]) {
                    working[i][j] = G_FILLED;
                }
            }
        }

        List<List<Vector2>> chains = new ArrayList<>();
        List<Vector2> chainPoints = new ArrayList<>();

        // Process the grid
        // Start by finding a filled cell
        for (int i = 0; i < working.length; i++) {
            for (int j = 0; j < working[i].length; j++) {
                if (working[i][j] == G_FILLED) {
                    Vector2 p = new Vector2(i, j);

                    // Once we've found such a cell, walk along the edge of it
                    // until we get to the start
                    do {
                        chainPoints.add(p.cpy());

                        if (sg(working, p.x, p.y) == G_FILLED
                                && sg(working, p.x - 1, p.y) != G_FILLED) {
                            p.y++;
                        } else if (sg(working, p.x, p.y - 1) == G_FILLED
                                && sg(working, p.x, p.y) != G_FILLED) {
                            p.x++;
                        } else if (sg(working, p.x - 1, p.y - 1) == G_FILLED
                                && sg(working, p.x, p.y - 1) != G_FILLED) {
                            p.y--;
                        } else if (sg(working, p.x - 1, p.y) == G_FILLED
                                && sg(working, p.x - 1, p.y - 1) != G_FILLED
                                && sg(working, p.x, p.y - 1) != G_FILLED) {
                            p.x--;
                        } else
                            break;
                    } while (!(p.x == i && p.y == j));

                    // Then mark that section unwalkable
                    setUsed(working, i, j);

                    // Obviously you can't have a path around a square with less than
                    // 4 points
                    if (chainPoints.size() < 4)
                        continue;

                    chains.add(chainPoints);
                    chainPoints = new ArrayList<>();
                }
            }
        }

        return chains;
    }

    /**
     * A 'safe get' from an array. If the x and y index are out of bounds, just
     * pretend the result is G_EMPTY
     */
    private static int sg(int[][] g, float x, float y) {
        if (x < 0 || y < 0)
            return G_EMPTY;
        else if (x > g.length - 1 || y > g[0].length - 1)
            return G_EMPTY;
        return g[(int) x][(int) y];
    }

    /**
     * Set the (x, y) square to USED and recursively try and do the same
     * for the surrounding cells.
     */
    private static void setUsed(int[][] g, int x, int y) {
        if (g[x][y] == G_FILLED) {
            g[x][y] = G_USED;

            if (x > 0 && g[x - 1][y] == G_FILLED)
                setUsed(g, x - 1, y);
            if (x < g.length - 1 && g[x + 1][y] == G_FILLED)
                setUsed(g, x + 1, y);

            if (y > 0 && g[x][y - 1] == G_FILLED)
                setUsed(g, x, y - 1);
            if (y < g[0].length - 1 && g[x][y + 1] == G_FILLED)
                setUsed(g, x, y + 1);
        }
    }
}
