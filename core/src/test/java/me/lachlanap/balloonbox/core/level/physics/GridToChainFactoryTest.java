package me.lachlanap.balloonbox.core.level.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Ignore;
import static org.junit.matchers.JUnitMatchers.*;

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

        List<List<Vector2>> chains = GridToChainFactory.makeChainPoints(grid);
        assertThat("Didn't get no chains", chains.size(), is(0));
    }

    @Test
    public void testSingleSquare() {
        boolean[][] grid = new boolean[10][10];

        grid[1][1] = true;

        List<List<Vector2>> chains = GridToChainFactory.makeChainPoints(grid);
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

        List<List<Vector2>> chains = GridToChainFactory.makeChainPoints(grid);
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

        List<List<Vector2>> chains = GridToChainFactory.makeChainPoints(grid);
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

        List<List<Vector2>> chains = GridToChainFactory.makeChainPoints(grid);
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
    public void testTwoChains() {
        // x x
        // x xx

        boolean[][] grid = new boolean[5][5];

        grid[0][0] = true;
        grid[0][1] = true;

        grid[2][0] = true;
        grid[2][1] = true;
        grid[3][1] = true;

        List<List<Vector2>> chains = GridToChainFactory.makeChainPoints(grid);
        assertThat("Didn't get 2 chains", chains.size(), is(2));

        List<Vector2> chain = chains.get(0);
        assertThat("Chain doesn't have 6 points", chain.size(), is(6));

        assertThat(chain.get(0), is(new Vector2(0, 0)));
        assertThat(chain.get(1), is(new Vector2(0, 1)));
        assertThat(chain.get(2), is(new Vector2(0, 2)));
        assertThat(chain.get(3), is(new Vector2(1, 2)));
        assertThat(chain.get(4), is(new Vector2(1, 1)));
        assertThat(chain.get(5), is(new Vector2(1, 0)));


        chain = chains.get(1);
        assertThat("Chain doesn't have 8 points", chain.size(), is(8));

        assertThat(chain.get(0), is(new Vector2(2, 0)));
        assertThat(chain.get(1), is(new Vector2(2, 1)));
        assertThat(chain.get(2), is(new Vector2(2, 2)));
        assertThat(chain.get(3), is(new Vector2(3, 2)));
        assertThat(chain.get(4), is(new Vector2(4, 2)));
        assertThat(chain.get(5), is(new Vector2(4, 1)));
        assertThat(chain.get(6), is(new Vector2(3, 1)));
        assertThat(chain.get(7), is(new Vector2(3, 0)));
    }

    @Test
    public void testComplex1() {
        // xxxxx
        //  xx
        //   x
        //   xxxxx
        //   x

        boolean[][] grid = new boolean[][]{
            {true, false, false, false, false},
            {true, true, false, false, false},
            {true, true, true, true, true},
            {true, false, false, true, false},
            {true, false, false, true, false},
            {false, false, false, true, false},
            {false, false, false, true, false},};

        List<List<Vector2>> chains = GridToChainFactory.makeChainPoints(grid);
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
}