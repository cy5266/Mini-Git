package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** Title: Tests for Array class
 *  @author Cindy Yang
 */

public class ArraysTest {
    @Test
    public void catenateTest()
    {
        int[] finaltest = new int[]{1,2,3,4,5,6,7,8,9,10 };
        int[] A = new int[]{1,2,3,4,5};
        int[] B = new int[]{6,7,8,9,10};
        assertArrayEquals(finaltest, Arrays.catenate(A, B));
    }

    @Test
    public void removeTest()
    {
        int[] A = new int[]{0, 1, 2, 3};
        int[] correct = new int[]{0, 3};
        assertArrayEquals(correct, Arrays.remove(A, 1,2));
    }

    @Test
    public void naturalRunsTest()
    {
        int[][] correct = new int[][]{{1, 3, 7}, {5}, {4, 6, 9, 10}};
        int[] A = new int[]{1, 3, 7, 5, 4, 6, 9, 10};
        assertArrayEquals(correct, Arrays.naturalRuns(A));
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
