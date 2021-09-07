/** Multidimensional array 
 *  @author Zoe Plaxco
 */

public class MultiArr {

    /**
    {{“hello”,"you",”world”} ,{“how”,”are”,”you”}} prints:
    Rows: 2
    Columns: 3
    
    {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
    Rows: 4
    Columns: 4
    */
    public static void printRowAndCol(int[][] arr) {
        System.out.println("Rows:" + arr.length);
        System.out.println("Columns:" + arr[0].length);
    } 

    /**
    @param arr: 2d array
    @return maximal value present anywhere in the 2d array
    */
    public static int maxValue(int[][] arr)
    {
        int max = arr[0][0];
        for (int i = 0; i < arr.length; i ++)
        {
            for (int j = 0; j < arr[i].length; j ++)
            {
                if (arr[i][j] > max)
                {
                    max = arr[i][j];
                }

            }
        }
        return max;
    }

    /**Return an array where each element is the sum of the 
    corresponding row of the 2d array*/
    public static int[] allRowSums(int[][] arr)
    {
        int finalarr[];
        finalarr = new int[arr.length];

        int sum = 0;

        for (int i = 0; i < arr.length; i++)
        {
            for (int j = 0; j < arr[i].length; j++)
            {
                sum += arr[i][j];
            }
            finalarr[i] = sum;
            sum = 0;
        }
        return finalarr;
    }
}