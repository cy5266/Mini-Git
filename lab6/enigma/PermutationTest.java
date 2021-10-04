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
 *  @author Isabelle Liu
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

    /* *** TESTS *** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void checkPermutationChar()
    {
        Permutation perm = getNewPermutation("(ABCD) (EFG)", getNewAlphabet());
        assertEquals('F', perm.permute('E'));
        assertEquals('E', perm.permute('G'));
        //checkPerm("check permute", "AJDOSL", "ABCDEF", perm);
    }

    @Test
    public void checkInvertChar()
    {
        Permutation perm = getNewPermutation("(ABCD) (EFG)", getNewAlphabet());
        assertEquals('C', perm.invert('D'));
        assertEquals('G', perm.invert('E'));
        assertEquals('Z', perm.invert('Z'));
    }

    @Test
    public void checkPermuteInt() {
        Permutation perm = getNewPermutation("(ABCD) (EFG)", getNewAlphabet());
        assertEquals(3, perm.permute(2));
        assertEquals(2, perm.permute(1));
    }

    @Test
    public void checkDerangement() {
        Permutation perm = getNewPermutation("(ABCD) (EFG)", getNewAlphabet());
        assertEquals(false, perm.derangement());
        Permutation p = getNewPermutation("(ABCD)", getNewAlphabet("ABCD"));
        assertEquals(true, p.derangement());
    }

    @Test
    public void checkSize(){
        Permutation perm = getNewPermutation("(ABCD) (EFG)", getNewAlphabet());
        assertEquals(26, perm.size());
        Permutation p = getNewPermutation("(ABCD)", getNewAlphabet("ABCD"));
        assertEquals(4, p.size());
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        p.invert('F');
        p.permute('#');
    }

    @Test(expected = EnigmaException.class)
    public void testWeirdSpacing() {
        Permutation p1 = getNewPermutation("(BACD ) (E P L)", getNewAlphabet());
        p1.permute('P');
        p1.invert('B');
    }

    @Test(expected = EnigmaException.class)
    public void testWeirdParen(){
        Permutation p2 = getNewPermutation("(BACD)) ) (E( P L)", getNewAlphabet());
        p2.invert(3);
        p2.permute('P');
    }

    @Test(expected = EnigmaException.class)
    public void testUpperCase(){
        Permutation p2 = getNewPermutation("(BACK) (EPZD)", getNewAlphabet());
        p2.invert('d');
        p2.permute('b');
    }

    @Test(expected = EnigmaException.class)
    public void invalidChar(){
        Permutation p2 = getNewPermutation("(BACK) (EPZ¥)", getNewAlphabet());
        p2.invert('d');
        p2.permute('Z');
    }

    @Test(expected = EnigmaException.class)
    public void cycleLetterRepeat(){
        Permutation p2 = getNewPermutation("(KACK) (EPKD)", getNewAlphabet());
        p2.invert('K');
        p2.permute('K');
    }

}