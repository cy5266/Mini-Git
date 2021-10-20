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
        testSet.put("54");
        testSet.put("1");
        testSet.put("5");
        testSet.put("blue");
        testSet.put("like");
        testSet.put("test1");
        testSet.put("test again");
        testSet.put("test3");
        testSet.put("hiadflkas");
        testSet.put("cindy yang");
        newSet.add(element);

        assertEquals(true, testSet.contains(element));
        assertEquals(true, testSet.contains("54"));
        assertEquals(true, testSet.contains("cindy yang"));
        assertEquals(false, testSet.contains("blah"));


    }

}
