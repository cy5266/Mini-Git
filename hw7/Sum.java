import java.util.Arrays;

/** HW #7, Two-sum problem.
 * @author
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        //sort a list, takes NlogN time
        Arrays.sort(A);

        //taking that sorted list, use a binary search on a sorted list to check to see if it contains the
        //element that you're looking for. for a sorted list, binary search takes logN time
        for (int i = 0; i < B.length; i ++) {
            if (Arrays.binarySearch(A, m - B[i]) > 0) {
                return true;
            }
        }
        return false;
    }

}
