public class HW0
{

    public static void main(String[] unused)
    {
        int[] a = new int[]{1, 2, 3, 4};
        max(a);
    }

    public static int max(int[] a)
    {

        int max = 0;
        for (int i = 0; i < a.length; i++)
        {
            if (a[i] > max)
            {
                max = a[i];
            }
        }

        return max;
    }

    

}
