package me.lachlanap.balloonbox.core.level.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generates a list of paths/chains from a grid of boolean squares.
 * <p/>
 * @author lachlan
 */
public class GridToChainFactory {

    public static boolean DEBUG = false;
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
            for (Vector2 v : pointChain) {
                v.scl(scale);
            }

            ChainShape chain = new ChainShape();
            chain.createLoop(pointChain.toArray(new Vector2[pointChain.size()]));
            chains.add(chain);
        }

        System.out.println(chains.size() + " chains.");
        return chains.toArray(new ChainShape[chains.size()]);
    }

    /**
     * Makes a list of lists of points (counter-clockwise) that define a path around the
     * specified grid.
     */
    public static List<List<Vector2>> makePath(boolean[][] originalGrid) {
        int[][] working = new int[originalGrid.length][originalGrid[0].length];
        final int W = working.length;
        final int H = working[0].length;

        if (W == 0 || H == 0)
            return Collections.EMPTY_LIST;

        // Setup the working copy - all 'on' cells are set to be filled
        for (int j = 0; j < H; j++)
            for (int i = 0; i < W; i++)
                if (originalGrid[i][j])
                    working[i][j] = G_FILLED;


        List<List<Vector2>> chains = new ArrayList<>();
        List<Vector2> chainPoints = new ArrayList<>();
        Set<Vector2> circularFilter = new HashSet<>();

        final Vector2 vel = new Vector2();

        // Process the grid
        // Start by finding a filled cell
        try {
            for (int j = 0; j < H; j++) {
                for (int i = 0; i < W; i++) {
                    if (working[i][j] == G_FILLED) {
                        Vector2 p = new Vector2(i, j);
                        vel.set(0, 1);

                        // Once we've found such a cell, walk along the edge of it
                        // until we get to the start
                        do {
                            Vector2 cpy = p.cpy();
                            chainPoints.add(cpy);
                            if (!circularFilter.add(cpy))
                                throw new IllegalStateException(
                                        "Detected Circle at + " + p + " chain so far:\n" + chainPoints);

                            // Do lots of complicated matching...
                            if (vel.x >= 1) { // ->
                                if (m(working, p.x, p.y,
                                      1, 0,
                                      0, 0)
                                        || m(working, p.x, p.y,
                                             0, 1,
                                             1, 1)
                                        || m(working, p.x, p.y,
                                             1, 0,
                                             0, 1)) {
                                    vel.set(0, -1);
                                } else if (m(working, p.x, p.y,
                                             0, 1,
                                             1, 0)
                                        || m(working, p.x, p.y,
                                             1, 1,
                                             0, 1)) {
                                    vel.set(0, 1);
                                } else {
                                }
                                // ...
                            } else if (vel.x <= -1) { // <-
                                if (m(working, p.x, p.y,
                                      0, 1,
                                      0, 0)
                                        || m(working, p.x, p.y,
                                             0, 1,
                                             1, 0)
                                        || m(working, p.x, p.y,
                                             1, 0,
                                             1, 1)) {
                                    vel.set(0, -1);
                                } else if (m(working, p.x, p.y,
                                             0, 0,
                                             0, 1)
                                        || m(working, p.x, p.y,
                                             1, 0,
                                             0, 1)
                                        || m(working, p.x, p.y,
                                             1, 1,
                                             1, 0)) {
                                    vel.set(0, 1);
                                } else {
                                }
                                // ...
                            } else if (vel.y >= 1) { // down
                                if (m(working, p.x, p.y,
                                      0, 1,
                                      0, 0)
                                        || m(working, p.x, p.y,
                                             0, 1,
                                             1, 0)
                                        || m(working, p.x, p.y,
                                             1, 0,
                                             1, 1)) {
                                    vel.set(1, 0);
                                } else if (m(working, p.x, p.y,
                                             1, 0,
                                             0, 0)
                                        || m(working, p.x, p.y,
                                             1, 0,
                                             0, 1)
                                        || m(working, p.x, p.y,
                                             0, 1,
                                             1, 1)) {
                                    vel.set(-1, 0);
                                } else {
                                }
                            } else if (vel.y <= -1) { // up
                                if (m(working, p.x, p.y,
                                      0, 0,
                                      1, 0)
                                        || m(working, p.x, p.y,
                                             0, 1,
                                             1, 0)) {
                                    vel.set(-1, 0);
                                } else if (m(working, p.x, p.y,
                                             0, 0,
                                             0, 1)
                                        || m(working, p.x, p.y,
                                             1, 0,
                                             0, 1)
                                        || m(working, p.x, p.y,
                                             1, 1,
                                             1, 0)) {
                                    vel.set(1, 0);
                                } else {
                                }
                                // ...
                            } else {
                                throw new IllegalStateException("Velocity is corrupted " + vel);
                            }

                            p.add(vel);
                        } while (!(p.x == i && p.y == j));

                        // Then mark that section unwalkable
                        setUsed(working, i, j);

                        // Obviously you can't have a path around a square with less than
                        // 4 points
                        if (chainPoints.size() < 4) {
                            continue;
                        }


                        chains.add(chainPoints);
                        chainPoints = new ArrayList<>();
                        circularFilter = new HashSet<>();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Crash: " + chainPoints);
            throw e;
        }

        return chains;
    }

    /**
     * A 'safe' match.
     * ab
     * cd
     */
    public static boolean m(int[][] g, float x, float y, int a, int b, int c, int d) {
        int p1, p2, p3, p4;
        p1 = sg(g, x - 1, y - 1);
        p2 = sg(g, x, y - 1);
        p3 = sg(g, x - 1, y);
        p4 = sg(g, x, y);

        if (p1 != 1 && p2 != 1 && p3 != 1 && p4 != 1)
            throw new IllegalStateException("Cannot match middle of nowhere: " + x + "," + y);

        boolean matches = true;
        if (a == 0)
            matches = matches && p1 != G_FILLED;
        else if (a == 1)
            matches = matches && p1 == G_FILLED;

        if (b == 0)
            matches = matches && p2 != G_FILLED;
        else if (b == 1)
            matches = matches && p2 == G_FILLED;

        if (c == 0)
            matches = matches && p3 != G_FILLED;
        else if (c == 1)
            matches = matches && p3 == G_FILLED;

        if (d == 0)
            matches = matches && p4 != G_FILLED;
        else if (d == 1)
            matches = matches && p4 == G_FILLED;

        return matches;
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
