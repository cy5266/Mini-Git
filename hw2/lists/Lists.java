package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

import java.util.ArrayList;
import java.util.List;

/** HW #2, Problem #1. */

/** List problem.
 *  @author Cindy Yang
 */
class Lists {

    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L)
    {

        ArrayList<ArrayList> test = new ArrayList<ArrayList>();
        ArrayList<Integer> A1 = new ArrayList<Integer>();
        int temp = L.head;

        while (L.tail != null)
        {

            if (L.head >= temp && L.head < L.tail.head)
            {
                A1.add(L.head);
                temp = L.tail.head;
            }
            else if (L.head > L.tail.head && L.head >= temp)
            {
                A1.add(L.head);
            }
            else if (L.head == L.tail.head)
            {
                A1.add(L.head);
                temp = L.head;
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
                A1.add(L.head);
                temp = L.head;
//                System.out.println(test);
            }

            L = L.tail;
        }
        A1.add(L.head);
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

        System.out.println(arr);
        IntListList list = IntListList.list(arr);
        IntListList finallist = IntListList.list(arr);


        return finallist;
    }
}
