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

//        for (int i = 0; i < _cycles.length; i++)
//        {
//            _cycles[i] = _cycles[i].replace("(", "");
//        }
        int i = 0;

        String testcycles = cycles.replace("(", "");
        testcycles = testcycles.replace(")", "");
        for (int c = 0 ; c < testcycles.length(); c ++){
            if (!_alphabet.contains(testcycles.charAt(c))){
                throw new EnigmaException("character not in alphabet");
            }
        }

        //hello
        //test

        for (String s: _cycles){

            if (s.length() <= 1){
                _cycles[i] = "";
            }
            else{
                _cycles[i] = _cycles[i].substring(1);
            }
            i += 1;
        }




//        for (int i = 0; i < _alphabet.; i++)
//        {
//            _cycles[i] = _cycles[i].replace("(", "");
//        }

//        for (int i = 0; i < _alphabet.size(); i++) {
//            if (!testCycles.contains(_alphabet.toChar(i))) {
//                _cycles.add(String.valueOf(_alphabet.toChar(i)));
//            }
//        }
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
        return _alphabet.toInt(permute(_alphabet.toChar(p % alphabet().size())));
//        return _alphabet.toInt(permute(_alphabet.toChar(p % alphabet().size())));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return _alphabet.toInt(invert(_alphabet.toChar(c % alphabet().size())));
//        return _alphabet.toInt(invert(_alphabet.toChar(c % alphabet().size())));

    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p)
    {
//        for (String loop: _cycles)
//        {
////            int temp = _alphabet.toInt(p);
////            System.out.println(temp);
//            if (loop.indexOf(p) != -1 )
//            {
//
////                int index = loop.indexOf(p) + 1;
////
////                if (index < 0)
////                {
////                    index = index + loop.length();
////                }
////                return loop.charAt(wrap(index));
//
//                return loop.charAt((loop.indexOf((p) + 1) % loop.length()));
//            }
//        }
//        return p;


        for (String loop: _cycles)
        {
            if (loop.indexOf(p) != -1 )
            {
                return loop.charAt((loop.indexOf(p) + 1) % loop.length());
            }
        }
        return p;


//        for (String s: _cycles){
//            if (s.indexOf(p) > -1){
//                if (s.indexOf(p) != s.length() -1){
//                    return s.charAt(s.indexOf(p) + 1);
//                }
//                else{
//                    return s.charAt(0);
//                }
//            }
//        }
//        throw error("rip");
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
//        for (String loop: _cycles)
//        {
//
//            if (loop.indexOf(c) != -1 )
//            {
//                int index = loop.indexOf(c) -1;
//
//                if (index < 0)
//                {
//                    index = index + loop.length();
//                }
//                return loop.charAt(wrap(index));
//            }
//        }
//        return c;
//        for (String s: _cycles){
//            if (s.indexOf(c) > -1){
//                if (s.indexOf(c) != 0){
//                    return s.charAt(s.indexOf(c) -1);
//                }
//                else{
//                    return s.charAt(s.length() -1);
//                }
//            }
//        }
//        throw error ("rip2");
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement()
    {
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
