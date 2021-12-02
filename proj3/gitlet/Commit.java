package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.TreeMap;

public class Commit implements Serializable {

    private String _message;
    private String _time;

    private String parent; //filename where we can find the commit object

//    private HashMap<String, byte[]> blobs = new HashMap<String, byte[]>();
    private TreeMap<String, byte[]> blobs = new TreeMap<>();

    public Commit() {
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        _time = formatter.format(new Date(0));
        _message = "initial commit";
        parent = null;

    }

    public Commit(String message, TreeMap<String, byte[]> blobs, String parentSHA) {
        _message = message;
        SimpleDateFormat currentTime = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
        _time = currentTime.format(new Date());

        this.blobs = blobs;
        this.parent = parentSHA;
    }


    public TreeMap<String, byte[] > get_Blobs() {
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

    public String getParentSHA() {
        return parent;
    }



}
