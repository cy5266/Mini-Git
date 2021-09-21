import afu.org.checkerframework.checker.oigj.qual.O;

public class extra extends WeirdList
{

    public extra()
    {
        super(0, null);
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public String toString() {
        return "";
    }
}
