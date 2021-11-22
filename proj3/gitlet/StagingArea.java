package gitlet;

import java.io.Serializable;
import java.util.HashMap;

public class StagingArea implements Serializable {

    /** instance variables */

    /** takes in String for Filename and String for SHA1 */
    private HashMap<String, String> _stageAddition = new HashMap<>();
    private HashMap<String, String> _stageRemoval = new HashMap<>();

    public void addStage(String fileName, String sha1) {
        _stageAddition.put(fileName, sha1);
    }

    public void addRemove(String fileName, String sha1) {
        _stageRemoval.put(fileName, sha1);
    }

    public void clear() {
        _stageRemoval.clear();
        _stageAddition.clear();
    }


    public HashMap<String, String> get_stageAddition() {
        return _stageAddition;
    }

    public HashMap<String, String> get_stageRemoval() {
        return _stageRemoval;
    }
}
