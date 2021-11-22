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

    /**we shouldn't actually use an instance variable for the sha value*/
    private String _SHA1;

    private String HEAD;

    private String parent; //filename where we can find the commit object

    private int _commitIndex = 0;
    private LinkedHashMap<String, Commit> _commitHistory;

    private HashMap<String, String> blobs;

//    public void Commit() {
//        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
//        _time = formatter.format(new Date(0));
//
//        _message = "initial commit";
//        this.parent = null;
//        _SHA1 = SHA1();
//        HEAD = _SHA1;
//        _commitHistory.put(_SHA1, null);
//    }

    public void Commit(String commitMessage, String parent) {

        if (parent == null) {
            SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy Z");
            _time = formatter.format(new Date(0));
            _SHA1 = SHA1();
            HEAD = _SHA1;
            _message = commitMessage;
            _commitHistory.put(_SHA1, null);
        }
        else {
            LocalDateTime current = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss yyyy Z");
            _time = current.format(formatter);
            _message = commitMessage;
            _SHA1 = SHA1();
            HEAD = _SHA1;
            this.parent = parent;
        }



//        _commitHistory.put(_SHA1, this);
    }

    public String SHA1() {
        byte[] commitObj = Utils.serialize(this);
        return Utils.sha1(commitObj);
    }

    public String get_SHA1() {
        return _SHA1;
    }

    public HashMap get_Blobs() {
        return blobs;
    }

    public String get_message() {
        return _message;
    }

    public String get_time() {
        return _time;
    }

    public LinkedHashMap get_CommitHistory() {
        return _commitHistory;
    }

    public String getHEAD() {
        return HEAD;
    }



}
