package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.LinkedList;

public class Commit implements Serializable {
    static final File COMMIT_FOLDER = new File(".commit_tree"); // FIXME

    private String _message;
    private String _time;

    private String HEAD;

    private String parent; //filename where we can find the commit object
    private String _SHA1;

    private int _commitIndex = 0;
    private LinkedHashMap<String, Commit> _commitHistory;

    private HashMap<String, byte[]> blobs = new HashMap<String, byte[]>();

    public Commit() {
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        _time = formatter.format(new Date(0));
//        _time = "00:00:00 UTC, Thursday, 1 January 1970";
        _message = "initial commit";
        _commitHistory = new LinkedHashMap<String, Commit>();
        _commitHistory.put(_SHA1, null);
        parent = null;

    }

    public Commit(String message, HashMap<String, byte[]> blobs, String parentSHA) {
        _message = message;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss yyyy Z");
//        LocalDateTime currentTime = LocalDateTime.now();
        SimpleDateFormat currentTime = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        _time = currentTime.format(new Date());

        this.blobs = blobs;
        this.parent = parentSHA;
    }

    public String SHA1() {
        byte[] commitObj = Utils.serialize(this);
        return Utils.sha1(commitObj);
    }

    public String get_SHA1() {
        return _SHA1;
    }

    public HashMap<String, byte[] > get_Blobs() {
        return blobs;
    }

    public String get_message() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public String get_time() {
        return _time;
    }

    public void setBlobs(String fileName, byte[] contents, String toAdd) {
        if (toAdd.equals("add")) {
            blobs.put(fileName, contents);
        }
        else if (toAdd.equals("rm")) {
            blobs.remove(fileName);
        }
    }

    public String getHEAD() {
        return HEAD;
    }



}
