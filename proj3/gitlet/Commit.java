package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

/** A class for each commit.
 *  @author Cindy Yang
 */
public class Commit implements Serializable {

    /** Variable to keep track of each commit's message. */
    private String _message;

    /** Variable to keep track of each commit's timestamp. */
    private String _time;

    /** Variable to keep track of the SHA of each commit's parent. */
    private String parent;

    /** Variable to keep track of each commit's blobs.
     *  Key = filename, Value = byte[] of the file. */
    private TreeMap<String, byte[]> blobs = new TreeMap<>();

    /** The initial commit constructor. */
    public Commit() {
        SimpleDateFormat formatter =
                new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        _time = formatter.format(new Date(0));
        _message = "initial commit";
        parent = null;

    }

    /** The commit constructor.
     * @param  message commit message
     * @param  commitBlobs commit blobs
     * @param  parentSHA commit parent's SHA*/
    public Commit(String message, TreeMap<String,
            byte[]> commitBlobs, String parentSHA) {
        _message = message;
        SimpleDateFormat currentTime =
                new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        _time = currentTime.format(new Date());

        this.blobs = commitBlobs;
        this.parent = parentSHA;
    }


    /** The constructor for getting commit blobs.
     * @return the blobs*/
    public TreeMap<String, byte[]> getBlobs() {
        return blobs;
    }

    /** The constructor for getting messages.
     * @return the message*/
    public String getMessage() {
        return _message;
    }

    /** The constructor for getting commit time.
     * @return the time*/
    public String getTime() {
        return _time;
    }

    /** setting the blobs.
     * @param  fileName name of file
     * @param  contents file contents
     * @param  toAdd whether to add or remove*/
    public void setBlobs(String fileName, byte[] contents, String toAdd) {
        if (toAdd.equals("add")) {
            blobs.put(fileName, contents);
        } else if (toAdd.equals("rm")) {
            blobs.remove(fileName);
        }
    }

    /** The constructor for getting commit parent.
     * @return the parent SHA*/
    public String getParentSHA() {
        return parent;
    }


}
