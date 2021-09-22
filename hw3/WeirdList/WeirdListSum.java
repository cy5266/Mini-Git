public class WeirdListSum implements IntUnaryFunction
{
    int sum = 0;

    @Override
    public int apply(int x)
    {
        return sum += x;
    }
}
