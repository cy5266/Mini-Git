import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] arr = {{1,3,4},{1},{5,6,7,8},{7,9}};
        assertEquals(9, MultiArr.maxValue(arr));
    }

    @Test
    public void testAllRowSums() {
        int[][] arr = {{1,3,4},{1},{5,6,7,8},{7,9}};
        int [] finalarr = {8,1,26,16};
        assertArrayEquals(finalarr, MultiArr.allRowSums(arr));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
