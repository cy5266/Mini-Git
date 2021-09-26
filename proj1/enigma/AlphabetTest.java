package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

public class AlphabetTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    @Test
    public void sizeTest() {
        Alphabet A = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals(-1, A.toInt('8'));
        assertEquals(25, A.toInt('Z'));
        assertEquals(false, A.contains('8'));
        assertEquals(false, A.contains('h'));
        assertEquals(true, A.contains('H'));
    }
}
