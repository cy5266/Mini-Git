package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Cindy Yang
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void cycleTest() {
        Permutation testCycle = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        String[] testArray = new String[]{
            "AELTPHQXRU", "BKNW", "CMOY", "DFG", "IV", "JZ", "S"};
        assertArrayEquals(testArray, testCycle.getCycles());

    }

    @Test
    public void permuteTest() {
        Permutation testCycle = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ)", UPPER);
        assertEquals('E', testCycle.permute('A'));
        assertEquals('A', testCycle.permute('U'));
        assertEquals('N', testCycle.permute('K'));
        assertEquals('J', testCycle.permute('Z'));
        assertEquals('Z', testCycle.permute('J'));
        assertEquals('S', testCycle.permute('S'));

    }

    @Test
    public void invertTest() {
        Permutation testCycle = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ)", UPPER);
        assertEquals('A', testCycle.invert('E'));
        assertEquals('R', testCycle.invert('U'));
        assertEquals('U', testCycle.invert('A'));
        assertEquals('J', testCycle.invert('Z'));
        assertEquals('Z', testCycle.invert('J'));
        assertEquals('S', testCycle.invert('S'));

    }

    @Test
    public void invertIntTest() {
        Permutation testCycle = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        assertEquals(20, testCycle.invert(0));
        assertEquals(4, testCycle.invert(11));

    }

    @Test
    public void permuteIntTest() {
        Permutation testCycle = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        assertEquals(4, testCycle.permute(0));
        assertEquals(20, testCycle.permute(17));
        assertEquals(0, testCycle.permute(20));
        assertEquals(4, testCycle.permute(26));

    }



}
