import java.util.Arrays;

/** HW #7, Two-sum problem.
 * @author
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        Arrays.sort(A);
        for (int i = 0; i < B.length; i ++) {
            if (Arrays.binarySearch(A, m - B[i]) < 0) {
                return true;
            }
        }
        return false;
    }

}
