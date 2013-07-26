package me.lachlanap.balloonbox.core.level.physics;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author lachlan
 */
public class GridToChainFactoryTest {

    public GridToChainFactoryTest() {
    }

    @Test
    public void testNull() {
        boolean[][] grid = new boolean[10][10];

        List<List<Vector2>> chains = GridToChainFactory.makePath(grid);
        assertThat("Didn't get no chains", chains.size(), is(0));
    }

    @Test
    public void testSingleSquare() {
        boolean[][] grid = new boolean[10][10];

        grid[1][1] = true;

        List<List<Vector2>> chains = GridToChainFactory.makePath(grid);
        assertThat("Didn't get 1 chain", chains.size(), is(1));

        List<Vector2> chain = chains.get(0);
        assertThat("Chain doesn't have 4 points", chain.size(), is(4));

        assertThat(chain.get(0), is(new Vector2(1, 1)));
        assertThat(chain.get(1), is(new Vector2(1, 2)));
        assertThat(chain.get(2), is(new Vector2(2, 2)));
        assertThat(chain.get(3), is(new Vector2(2, 1)));
    }

    @Test
    public void testSingleSquareAtOrigin() {
        boolean[][] grid = new boolean[10][10];

        grid[0][0] = true;

        List<List<Vector2>> chains = GridToChainFactory.makePath(grid);
        assertThat("Didn't get 1 chain", chains.size(), is(1));

        List<Vector2> chain = chains.get(0);
        assertThat("Chain doesn't have 4 points", chain.size(), is(4));

        assertThat(chain.get(0), is(new Vector2(0, 0)));
        assertThat(chain.get(1), is(new Vector2(0, 1)));
        assertThat(chain.get(2), is(new Vector2(1, 1)));
        assertThat(chain.get(3), is(new Vector2(1, 0)));
    }

    @Test
    public void testTwoXSquares() {
        boolean[][] grid = new boolean[3][3];

        grid[0][0] = true;
        grid[1][0] = true;

        List<List<Vector2>> chains = GridToChainFactory.makePath(grid);
        assertThat("Didn't get 1 chain", chains.size(), is(1));

        List<Vector2> chain = chains.get(0);
        assertThat("Chain doesn't have 6 points", chain.size(), is(6));

        assertThat(chain.get(0), is(new Vector2(0, 0)));
        assertThat(chain.get(1), is(new Vector2(0, 1)));
        assertThat(chain.get(2), is(new Vector2(1, 1)));
        assertThat(chain.get(3), is(new Vector2(2, 1)));
        assertThat(chain.get(4), is(new Vector2(2, 0)));
        assertThat(chain.get(5), is(new Vector2(1, 0)));
    }

    @Test
    public void testTwoYSquares() {
        boolean[][] grid = new boolean[3][3];

        grid[0][0] = true;
        grid[0][1] = true;

        List<List<Vector2>> chains = GridToChainFactory.makePath(grid);
        assertThat("Didn't get 1 chain", chains.size(), is(1));

        List<Vector2> chain = chains.get(0);
        assertThat("Chain doesn't have 6 points", chain.size(), is(6));

        assertThat(chain.get(0), is(new Vector2(0, 0)));
        assertThat(chain.get(1), is(new Vector2(0, 1)));
        assertThat(chain.get(2), is(new Vector2(0, 2)));
        assertThat(chain.get(3), is(new Vector2(1, 2)));
        assertThat(chain.get(4), is(new Vector2(1, 1)));
        assertThat(chain.get(5), is(new Vector2(1, 0)));
    }

    @Test
    public void testTwoDiags() {
        String[] gridDef = {
            " x",
            "x "
        };

        String[] path = {
            " 1 ",
            "32 ",
            "45 "
        };
        
        boolean[][] grid = gridFromPattern(gridDef);

        List<List<Vector2>> chains = GridToChainFactory.makePath(grid);
        assertThat("Didn't get 1 chain", chains.size(), is(2));

        List<Vector2> chain = chains.get(0);
        assertThat(chain, is(pathFromPattern(path)));
    }

    @Test
    public void testTwoChains() {
        String[] gridDef = {
            "x x ",
            "x xx"
        };

        String[] path1 = {
            "16",
            "25",
            "34"
        };

        String[] path2 = {
            "  18 ",
            "  276",
            "  345"
        };

        boolean[][] grid = gridFromPattern(gridDef);

        List<List<Vector2>> chains = GridToChainFactory.makePath(grid);
        assertThat("Didn't get 2 chains", chains.size(), is(2));

        List<Vector2> chain = chains.get(0);
        assertThat("Chain doesn't have 6 points", chain.size(), is(6));
        assertThat(chain, is(pathFromPattern(path1)));

        chain = chains.get(1);
        assertThat("Chain doesn't have 8 points", chain.size(), is(8));
        assertThat(chain, is(pathFromPattern(path2)));
    }

    @Test
    public void testComplex1() {
        String[] gridDef = {
            "xxxxx  ",
            " xx    ",
            "  x    ",
            "  xxxxx",
            "  x    ",};

        boolean[][] grid = gridFromPattern(gridDef);

        List<List<Vector2>> chains = GridToChainFactory.makePath(grid);
        assertThat("Didn't get 1 chain", chains.size(), is(1));

        List<Vector2> chain = chains.get(0);
        assertThat("Chain doesn't have 28 points", chain.size(), is(28));

        assertThat(chain.get(0), is(new Vector2(0, 0)));
        assertThat(chain.get(1), is(new Vector2(0, 1)));
        assertThat(chain.get(2), is(new Vector2(1, 1)));
        assertThat(chain.get(3), is(new Vector2(1, 2)));
        assertThat(chain.get(4), is(new Vector2(2, 2)));
        assertThat(chain.get(5), is(new Vector2(2, 3)));
        assertThat(chain.get(6), is(new Vector2(2, 4)));
        assertThat(chain.get(7), is(new Vector2(2, 5)));
        assertThat(chain.get(8), is(new Vector2(3, 5)));
        assertThat(chain.get(9), is(new Vector2(3, 4)));
        assertThat(chain.get(10), is(new Vector2(4, 4)));
        assertThat(chain.get(11), is(new Vector2(5, 4)));
        assertThat(chain.get(12), is(new Vector2(6, 4)));
        assertThat(chain.get(13), is(new Vector2(7, 4)));
        assertThat(chain.get(14), is(new Vector2(7, 3)));
        assertThat(chain.get(15), is(new Vector2(6, 3)));
        assertThat(chain.get(16), is(new Vector2(5, 3)));
        assertThat(chain.get(17), is(new Vector2(4, 3)));
        assertThat(chain.get(18), is(new Vector2(3, 3)));
        assertThat(chain.get(19), is(new Vector2(3, 2)));
        assertThat(chain.get(20), is(new Vector2(3, 1)));
        assertThat(chain.get(21), is(new Vector2(4, 1)));
        assertThat(chain.get(22), is(new Vector2(5, 1)));
        assertThat(chain.get(23), is(new Vector2(5, 0)));
        assertThat(chain.get(24), is(new Vector2(4, 0)));
        assertThat(chain.get(25), is(new Vector2(3, 0)));
        assertThat(chain.get(26), is(new Vector2(2, 0)));
        assertThat(chain.get(27), is(new Vector2(1, 0)));
    }

    @Test
    public void testComplex2() {
        String[] gridDef = {
            "  xxx",
            "xx  x",
            "x   x",
            "x   x"
        };

        String[] path = {
            "  1   ",
            "432   ",
            "5ab   ",
            "69    ",
            "78    "
        };

        boolean[][] grid = gridFromPattern(gridDef);


        List<List<Vector2>> chains = GridToChainFactory.makePath(grid);
        assertThat("Didn't get 1 chain", chains.size(), is(1));

        List<Vector2> chain = chains.get(0);
        //assertThat("Chain doesn't have 4 points", chain.size(), is(16));
        assertThat(chain, is(pathFromPattern(path)));
    }

    private static boolean[][] gridFromPattern(String[] pattern) {
        boolean[][] grid = new boolean[pattern[0].length()][pattern.length];

        int y, x;
        y = 0;
        for (String line : pattern) {
            x = 0;
            for (char c : line.toCharArray()) {
                grid[x][y] = Character.isLetterOrDigit(c);

                x++;
            }

            y++;
        }

        return grid;
    }

    private static List<Vector2> pathFromPattern(String[] pattern) {
        final int H = pattern.length, W = pattern[0].length();

        int[][] grid = new int[W][H];
        int max = 0;

        for (int y = 0; y < H; y++) {
            char[] line = pattern[y].toCharArray();
            if (line.length != W)
                throw new IllegalArgumentException("Pattern must be a uniform grid");

            for (int x = 0; x < W; x++) {
                char c = line[x];

                if (!Character.isLetterOrDigit(c))
                    grid[x][y] = -1;

                if (c < '0')
                    continue;
                else if (c <= '9')
                    grid[x][y] = c - '0';
                else if (c < 'a')
                    continue;
                else if (c <= 'z')
                    grid[x][y] = c - 'a' + 10;
                else if (c < 'A')
                    continue;
                else if (c <= 'Z')
                    grid[x][y] = c - 'A' + 36;

                max = Math.max(max, grid[x][y]);
                System.out.print(grid[x][y]);
            }

            System.out.println();
        }

        List<Vector2> points = new ArrayList<>();
        for (int i = 0; i < max + 1; i++) {
            for (int x = 0; x < W; x++) {
                for (int y = 0; y < H; y++) {
                    if (grid[x][y] == i) {
                        points.add(new Vector2(x, y));
                    }
                }
            }
        }

        return points;
    }
}