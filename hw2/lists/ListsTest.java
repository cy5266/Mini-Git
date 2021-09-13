package lists;

import arrays.Arrays;
import arrays.IntListList;
import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author FIXME
 */

public class ListsTest {
    @Test
    public void naturalRunsTest()
    {

        int[][] arr = new int[][]{{1, 3, 7}, {5}, {4, 6, 9, 10}, {10,11}};
        IntListList list = IntListList.list(arr);

        int[] A = new int[]{1, 3, 7, 5, 4, 6, 9, 10, 10, 11};
        IntList testA = IntList.list(A);

        assertEquals(list.toString(), Lists.naturalRuns(testA).toString());
    }

    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
