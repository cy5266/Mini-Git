package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeMap;

public class StagingArea implements Serializable {

    /** instance variables */

    /** takes in String for Filename and String for SHA1 */
    private TreeMap<String, byte[]> _stageAddition = new TreeMap<>();
    private TreeMap<String, byte[]> _stageRemoval = new TreeMap<>();

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


    public TreeMap<String, byte[]> get_stageAddition() {
        return _stageAddition;
    }

    public TreeMap<String, byte[]> get_stageRemoval() {
        return _stageRemoval;
    }
}
