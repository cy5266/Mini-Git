import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class BSTStringSetTest  {
    // FIXME: Add your own tests for your BST StringSet

    @Test
    public void testNothing() {
        int[] keys = {2,5,3,1,4,1};
        String[] values = {"1","5","4","3","4","2"};
        BSTStringSet test = new BSTStringSet();
        java.util.TreeMap<Integer, String> solnMap = new java.util.TreeMap<>();
        for (int i = 0; i < keys.length; i += 1) {
            solnMap.put(keys[i], values[i]);
            test.put(values[i]);
        }
        assertEquals(test.contains("1"), true);
        assertEquals(test.contains("5"), true);
        assertEquals(test.contains("7"), false);
        System.out.println(test.asList());
    }
}
