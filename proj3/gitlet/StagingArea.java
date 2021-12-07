package gitlet;

import java.io.Serializable;
import java.util.TreeMap;

/** A class for Staging Area.
 *  @author Cindy Yang
 */
public class StagingArea implements Serializable {

    /** instance variables */

    /** Treemap of fileName and file contents for addition.*/
    private TreeMap<String, byte[]> _stageAddition = new TreeMap<>();

    /** Treemap of fileName and file contents for removal.*/
    private TreeMap<String, byte[]> _stageRemoval = new TreeMap<>();

    /** to clear the treemaps.*/
    public void clear() {
        _stageRemoval.clear();
        _stageAddition.clear();
    }

    /** get treeMap addition.
     * @return addition treeMap*/
    public TreeMap<String, byte[]> getStageAddition() {
        return _stageAddition;
    }

    /** get treeMap removal.
     * @return removal treeMap*/
    public TreeMap<String, byte[]> getStageRemoval() {
        return _stageRemoval;
    }
}
