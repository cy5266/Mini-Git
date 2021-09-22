public class WeirdListSum implements IntUnaryFunction
{
    int sum = 0;
//
//    public WeirdListSum(int head){
//        sum = head;
//    }

    @Override
    public int apply(int x)
    {
        return sum += x;
    }
}
