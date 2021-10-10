package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Cindy Yang
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    void advance() {
        set(permutation().wrap(setSetting(1)));
    }

    @Override
    boolean atNotch() {
        if (_notches.contains(String.valueOf(
                alphabet().toChar(getSetting())))) {
            return true;
        }
        return false;
    }

    @Override
    boolean rotates() {
        return true;
    }

    /** Notches. */
    private String _notches;

}
