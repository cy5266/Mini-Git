import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class ECHashStringSetTest  {
    // FIXME: Add your own tests for your ECHashStringSetTest

    @Test
    public void testNothing() {
        // FIXME: Delete this function and add your own tests
        ECHashStringSet testSet = new ECHashStringSet();
        HashSet newSet = new HashSet();
        String element = "hi";
        testSet.put(element);
        newSet.add(element);

        assertEquals(true, testSet.contains(element));
        assertEquals(false, testSet.contains("blah"));


    }

}
