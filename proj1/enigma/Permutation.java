package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet)
    {
        _alphabet = alphabet;
        cycles = cycles.replace(" ", "");
        _cycles = cycles.split("\\)");
        for (int i = 0; i < _cycles.length; i++)
        {
            _cycles[i] = _cycles[i].replace("(", "");
        }
    }


    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
//        _cycles += cycle + cycle.charAt(-1);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p)
    {
        return _alphabet.toInt(permute(_alphabet.toChar(p)));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return _alphabet.toInt(invert(_alphabet.toChar(c)));

    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p)
    {
        for (String loop: _cycles)
        {
            if (loop.indexOf(p) != -1 )
            {
                return loop.charAt((loop.indexOf(p) + 1) % loop.length());
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        for (String loop: _cycles)
        {
            if (loop.indexOf(c) != -1 )
            {
                int index = loop.indexOf(c) -1;

                if (index < 0)
                {
                    index = index + loop.length();
                }
                return loop.charAt(index % loop.length());
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement()
    {
//        int numletters = _alphabet.size();
//        for (int i = 0; i < numletters; i++)
//        {
//            if (_cycles.indexOf(_alphabet.toChar(i)) == -1)
//            {
//                return true;
//            }
//        }
//        return false;

        for (String s: _cycles) {
            if (s.length() == 1) {
                return true;
            }
        }
        return false;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    public String[] _cycles;

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED
}
