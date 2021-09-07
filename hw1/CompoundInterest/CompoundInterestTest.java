import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(0, CompoundInterest.numYears(2021));
        assertEquals(1, CompoundInterest.numYears(2022));
        assertEquals(100, CompoundInterest.numYears(2121));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(10*1.12*1.12, CompoundInterest.futureValue(10,12,2023), tolerance);
        assertEquals(8*1.1*1.1*1.1, CompoundInterest.futureValue(8,10,2024), tolerance);
        assertEquals(5*0.8, CompoundInterest.futureValue(5,-20,2022), tolerance);
    }

    @Test
    public void futureValueReal() {
        double tolerance = 0.01;
        assertEquals(12.544 * 0.97 * 0.97, CompoundInterest.futureValueReal(10,12,2023, 3), tolerance);

    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(5000*1.1 + 5000, CompoundInterest.totalSavings(5000, 2022, 10), tolerance);
        assertEquals(5000*1.1*1.1+5000*1.1 + 5000, CompoundInterest.totalSavings(5000, 2023, 10), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals((5000*1.1 + 5000)*0.97, CompoundInterest.totalSavingsReal(5000,2022,10, 3), tolerance);
        assertEquals((5000*1.1*1.1+5000*1.1 + 5000)*0.97*0.97, CompoundInterest.totalSavingsReal(5000, 2023, 10, 3), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
