package me.lachlanap.balloonbox.core.level.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lachlan
 */
public class GridToChainFactory {

    private static final int G_EMPTY = 0;
    private static final int G_FILLED = 1;
    private static final int G_USED = 2;

    public static ChainShape[] makeChainShapes(boolean[][] g, float scale) {
        List<List<Vector2>> pointChains = makeChainPoints(g);

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

    public static List<List<Vector2>> makeChainPoints(boolean[][] g) {
        int[][] grid = new int[g.length][g[0].length];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (g[i][j]) {
                    grid[i][j] = G_FILLED;
                }
            }
        }

        List<List<Vector2>> chains = new ArrayList<>();
        List<Vector2> chainPoints = new ArrayList<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == G_FILLED) {
                    Vector2 p = new Vector2(i, j);

                    do {
                        chainPoints.add(p.cpy());

                        if (sg(grid, p.x, p.y) == G_FILLED
                                && sg(grid, p.x - 1, p.y) != G_FILLED) {
                            p.y++;
                        } else if (sg(grid, p.x, p.y - 1) == G_FILLED
                                && sg(grid, p.x, p.y) != G_FILLED) {
                            p.x++;
                        } else if (sg(grid, p.x - 1, p.y - 1) == G_FILLED
                                && sg(grid, p.x, p.y - 1) != G_FILLED) {
                            p.y--;
                        } else if (sg(grid, p.x - 1, p.y) == G_FILLED
                                && sg(grid, p.x - 1, p.y - 1) != G_FILLED
                                && sg(grid, p.x, p.y - 1) != G_FILLED) {
                            p.x--;
                        } else
                            break;
                    } while (!(p.x == i && p.y == j));

                    setUsed(grid, i, j);

                    if (chainPoints.size() < 4)
                        continue;

                    chains.add(chainPoints);
                    chainPoints = new ArrayList<>();
                }
            }
        }

        return chains;
    }

    private static int sg(int[][] g, float x, float y) {
        if (x < 0 || y < 0)
            return G_EMPTY;
        else if (x > g.length - 1 || y > g[0].length - 1)
            return G_EMPTY;
        return g[(int) x][(int) y];
    }

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
