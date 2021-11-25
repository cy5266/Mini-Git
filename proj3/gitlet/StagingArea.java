package gitlet;

import java.io.Serializable;
import java.util.HashMap;

public class StagingArea implements Serializable {

    /** instance variables */

    /** takes in String for Filename and String for SHA1 */
    private HashMap<String, byte[]> _stageAddition = new HashMap<>();
    private HashMap<String, byte[]> _stageRemoval = new HashMap<>();

    public void addStage(String fileName, byte[] contents) {
        _stageAddition.put(fileName, contents);
    }

    public void addRemove(String fileName,  byte[] contents ) {
        _stageRemoval.put(fileName, contents);
    }

    public void clear() {
        _stageRemoval.clear();
        _stageAddition.clear();
    }


    public HashMap<String, byte[]> get_stageAddition() {
        return _stageAddition;
    }

    public HashMap<String, byte[]> get_stageRemoval() {
        return _stageRemoval;
    }
}
