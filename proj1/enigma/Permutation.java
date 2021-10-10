package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Cindy Yang
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        cycles = cycles.replace(" ", "");
        _cycles = cycles.split("\\)");

        int i = 0;

        String testcycles = cycles.replace("(", "");
        testcycles = testcycles.replace(")", "");
        for (int c = 0; c < testcycles.length(); c++) {
            if (!_alphabet.contains(testcycles.charAt(c))) {
                throw new EnigmaException("character not in alphabet");
            }
        }

        for (String s: _cycles) {

            if (s.length() <= 1) {
                _cycles[i] = "";
            } else {
                _cycles[i] = _cycles[i].substring(1);
            }
            i += 1;
        }

    }


    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
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
    int permute(int p) {
        return _alphabet.toInt(permute(
                _alphabet.toChar(p % alphabet().size())));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return _alphabet.toInt(invert(
                _alphabet.toChar(c % alphabet().size())));

    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {

        for (String loop: _cycles) {
            if (loop.indexOf(p) != -1) {
                return loop.charAt((loop.indexOf(p) + 1) % loop.length());
            }

        }
        if (_alphabet.contains(p)) {
            return p;
        }
        throw new EnigmaException("letter not in alphabet");
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {

        for (String loop: _cycles) {
            if (loop.indexOf(c) != -1) {
                int index = loop.indexOf(c) - 1;

                if (index < 0) {
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
    boolean derangement() {
        for (String s: _cycles) {
            if (s.length() == 1) {
                return true;
            }
        }
        return false;
    }

    /** Returns cycles. */
    String[] getCycles() {
        return _cycles;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** String array of all cycles. */
    private String[] _cycles;
}
