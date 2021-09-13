package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

import java.util.ArrayList;

/** Array utilities.
 *  @author Cindy Yang
 */
public class Arrays
{

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B)
    {
        int[] C = new int[A.length + B.length];
        System.arraycopy(A, 0, C, 0, A.length);
        System.arraycopy(B, 0, C, A.length, B.length);
        return C;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. If the start + len is out of bounds for our array, you
     *  can return null.
     *  Example: if A is [0, 1, 2, 3] and start is 1 and len is 2, the
     *  result should be [0, 3]. */
    static int[] remove(int[] A, int start, int len)
    {
        if (start+len > A.length-1)
        {
            return null;
        }
        int[] test = new int[A.length - len];
        int count = 0;

        for (int i = 0; i < A.length; i ++)
        {
            if (i < start || i > len)
            {
                test[count] = A[i];
                count += 1;
            }
        }

        return test;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A)
    {
//        int[][] test;
//        if (A.length == 0)
//        {
//            return new int[0][];
//        }
//        else
//        {
//            int numelements = 1;
//            for (int i = 0; i < A.length-1; i ++)
//            {
//                if (A[i+1] < A[i])
//                {
//                    numelements += 1;
//                }
//            }
//            test = new int[numelements][];
//
//            for (int row = 0; row < test.length; row ++)
//            {
//
//            }
//
//            return test;
//
//        }

        ArrayList<ArrayList> test = new ArrayList<ArrayList>();
        ArrayList<Integer> A1 = new ArrayList<Integer>();
        int temp = A[0];

        while (A.length >=2)
        {

            if (A[0] >= temp && A[0] < A[1])
            {
                A1.add(A[0]);
                temp = A[1];
            }
            else if (A[0] > A[1] && A[0] >= temp)
            {
                A1.add(A[0]);
            }
            else if (A[0] == A[1])
            {
                A1.add(A[0]);
                temp = A[0];
                test.add(A1);
//                System.out.println(test);
                A1 = new ArrayList<Integer>();
            }
            else
            {
//                System.out.println(A1);
                test.add(A1);
//                System.out.println(test);
                A1 = new ArrayList<Integer>();
                A1.add(A[0]);
                temp = A[0];
//                System.out.println(test);
            }

            int[] transfer = new int[A.length];
            transfer = A;
            A = new int[A.length-1];
            System.arraycopy(transfer, 1, A, 0, transfer.length-1);
        }
        A1.add(A[0]);
        test.add(A1);

//        System.out.println(test);
        test.toArray();

        int[][] arr = new int[test.size()][];

        for (int i = 0; i < test.size(); i ++)
        {
            int[] blah = new int[test.get(i).size()];
            for (int j = 0; j < test.get(i).size(); j ++)
            {
//                System.out.println(Integer.valueOf((test.get(i).get(j)).toString()));
                blah[j] = Integer.valueOf((test.get(i).get(j)).toString());
            }

            arr[i] = blah;
        }
        return arr;
    }

}
