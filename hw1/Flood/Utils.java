import static java.lang.System.arraycopy;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.*;

/** Various utility methods.
 *  @author P. N. Hilfinger
 */
class Utils {

    /** Copy contents of SRC into DEST.  SRC and DEST must both be
     *  rectangular, with identical dimensions. */
    static void deepCopy(int[][] src, int[][] dest) {
        assert src.length == dest.length && src[0].length == dest[0].length;
        for (int i = 0; i < src.length; i += 1) {
            arraycopy(src[i], 0, dest[i], 0, src[i].length);
        }
    }
}

