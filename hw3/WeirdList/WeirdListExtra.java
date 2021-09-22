public class WeirdListExtra implements IntUnaryFunction{

    int newVar;

    public WeirdListExtra(int passin)
    {
        this.newVar = passin;
    }
    @Override
    public int apply(int x)
    {
        return this.newVar + x;
    }
}
