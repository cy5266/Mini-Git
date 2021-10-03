package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void testInvertChar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('B', p.invert('A'));
        assertEquals('A', p.invert('C'));
        assertEquals('D', p.invert('B'));

    }

    @Test
    public void permuteTest()
    {
        Permutation p = getNewPermutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", getNewAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals('E', p.permute('A'));
        assertEquals('A', p.permute('U'));
        assertEquals('N', p.permute('K'));
        assertEquals('J', p.permute('Z'));
        assertEquals('Z', p.permute('J'));
        assertEquals('S', p.permute('S'));

    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        p.invert('F');
        p.permute('G');
    }

//
//    @Test(expected = EnigmaException.class)
//    public void permuteTestBuggy()
//    {
//        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
//        p.invert('F');
//        p.invert('1');
//    }


    @Test
    public void invertTest()
    {
        Permutation p = getNewPermutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", getNewAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals('A', p.invert('E'));
        assertEquals('R', p.invert('U'));
        assertEquals('U', p.invert('A'));
        assertEquals('J', p.invert('Z'));
        assertEquals('Z', p.invert('J'));
        assertEquals('S', p.invert('S'));

    }


    @Test(expected = EnigmaException.class)
    public void invertTestBuggy()
    {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        p.invert('F');
        p.invert('1');
    }

    @Test
    public void invertIntTest() {
        Permutation p = getNewPermutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", getNewAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals(20, p.invert(0));
        assertEquals(4, p.invert(11));

    }

    @Test
    public void permuteIntTest() {
        Permutation p = getNewPermutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", getNewAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals(4, p.permute(0));
        assertEquals(20, p.permute(17));
        assertEquals(0, p.permute(20));

    }


    @Test(expected = EnigmaException.class)
    public void badCycle() {
        Permutation p = getNewPermutation("(B ACD)", getNewAlphabet("ABCD"));
        p.invert('A');
        p.permute('B');
    }

    @Test(expected = EnigmaException.class)
    public void badCycle2() {
        Permutation p = getNewPermutation("(BACD)) ", getNewAlphabet("ABCD"));
        p.invert('D');
        p.permute('C');
    }


    @Test(expected = EnigmaException.class)
    public void invertIntTestBuggy()
    {
        Permutation p = getNewPermutation("(FGHI)", getNewAlphabet("ABCD"));
        p.invert('H');
        p.permute('G');
        p.invert(3);
        p.permute(2);
    }

    @Test(expected = EnigmaException.class)
    public void callCapsNotLocked()
    {
        Permutation p = getNewPermutation("(abcd)", getNewAlphabet("ABCD"));
        p.invert('a');
        p.permute('B');
    }


    @Test(expected = EnigmaException.class)
    public void badParenthesis()
    {
        Permutation p = getNewPermutation("{ABCD}", getNewAlphabet("ABCD"));
        p.invert('a');
        p.permute('B');
        p.invert('-');
        p.permute(1);
    }


    @Test(expected = EnigmaException.class)
    public void lettertwice()
    {
        Permutation p = getNewPermutation("(AABCD)", getNewAlphabet("ABCD"));
        p.invert('A');
        p.permute('B');
    }

    @Test(expected = EnigmaException.class)
    public void lettertwice2()
    {
        Permutation p = getNewPermutation("(ACBD)", getNewAlphabet("ABBCD"));
        p.invert('D');
        p.permute('B');
    }


    @Test
    public void mapItself()
    {
        Permutation p = getNewPermutation("(ACB)", getNewAlphabet("ABCD"));
        assertEquals('D', p.permute('D'));
        assertEquals('D', p.invert('D'));
    }


    @Test
    public void derangementTest()
    {
        Permutation p = getNewPermutation("SE", getNewAlphabet("SE"));
        assertEquals(false, p.derangement());
    }





    // FIXME: Add tests here that pass on a correct Permutation and fail on buggy Permutations.
}
