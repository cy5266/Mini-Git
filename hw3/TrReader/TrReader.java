import java.io.Reader;
import java.io.IOException;


/** Translating Reader: a stream that is a translation of an
*  existing reader.
*  @author Cindy Yang
*
*  NOTE: Until you fill in the right methods, the compiler will
*        reject this file, saying that you must declare TrReader
* 	     abstract.  Don't do that; define the right methods instead!
*/
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */

    static Reader str;
    static String from;
    static String to;

    public TrReader(Reader str, String from, String to)
    {
        this.str = str;
        this.from = from;
        this.to = to;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException
    {

        int stop = this.str.read(cbuf, off, len);
        if (stop == -1)
        {
            return -1;
        }


        for (int i = off; i < len; i ++)
        {
//            int c = str.read();
            int temp = from.indexOf(cbuf[i]);
            if (temp != -1)
            {
                cbuf[i] = to.charAt(temp);
//                c = to.charAt(temp);
            }
        }
        return stop;
    }

    @Override
    public void close() throws IOException
    {
        this.str.close();
    }

    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     */
}
