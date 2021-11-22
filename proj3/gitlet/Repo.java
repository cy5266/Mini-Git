package gitlet;

//

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import java.io.File;

public class Repo implements Serializable {

    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File GITLET_FOLDER = Utils.join(CWD, "gitlet");

    static final File STAGING_FOLDER = Utils.join(GITLET_FOLDER, "/staging");

    static final File COMMIT_FOLDER = Utils.join(GITLET_FOLDER, "/commits");

//    static final File BLOB_FOLDER = Utils.join(GITLET_FOLDER, "/blobs");

    static final File BRANCHES_FOLDER = Utils.join(GITLET_FOLDER, "/branches");

    /**files for serialization*/
    static final File HEAD_FILE = Utils.join(GITLET_FOLDER, "/head");
    static final File STAGE_FILE = Utils.join(GITLET_FOLDER, "/stage_add");

    public static Commit HEAD;

    private static String MASTER;

    private static StagingArea stage;

    public static void init() {
        if (!GITLET_FOLDER.exists()) {
            setupPersistence();
            try {
                HEAD_FILE.createNewFile();
            }
            catch (IOException err) {
                return;
            }

            Commit initialCommit = new Commit();
            MASTER = initialCommit.getHEAD();

            File _initialCommitFile = Utils.join(COMMIT_FOLDER, initialCommit.get_SHA1());
            Utils.writeObject(_initialCommitFile, Utils.serialize(initialCommit));

            Utils.writeObject(HEAD_FILE, Utils.serialize(initialCommit));

            stage = new StagingArea();

            Utils.writeObject(STAGE_FILE, new StagingArea()); //USE STAGE? OR NEW STAGING AREA
        }



    }

    public static void add(String fileName) {
        File addFile = Utils.join(GITLET_FOLDER, fileName);
        HEAD = getHeadCommit();

        if (!addFile.exists()) {
            System.out.println("File doesn't exist");
            return;
        }
        byte[] blob = Utils.readContents(addFile);
        String blobHash = Utils.sha1(blob);

        if (HEAD.get_Blobs().containsKey(fileName)) {
            if (blobHash.equals(HEAD.get_Blobs().get(fileName))) {
                stage.get_stageAddition().remove(fileName);
                Utils.writeObject(STAGE_FILE, stage);
            }
            return;
        }

        if (stage.get_stageRemoval().containsKey(fileName)) {
            stage.get_stageRemoval().remove(fileName);
            Utils.writeObject(STAGE_FILE, stage);
        }

        stage.addStage(fileName, blobHash);
        Utils.writeObject(STAGE_FILE, stage.get_stageAddition());
        Utils.writeObject( HEAD_FILE, HEAD);
    }

    public StagingArea getStage() {
        return Utils.readObject(STAGE_FILE, StagingArea.class);
    }

    public static Commit getHeadCommit() {
        return Utils.readObject(HEAD_FILE, Commit.class);
    }

    public static void setupPersistence() {
        if (!GITLET_FOLDER.exists()) {
            GITLET_FOLDER.mkdir();
            STAGING_FOLDER.mkdir();
            COMMIT_FOLDER.mkdir();
            BRANCHES_FOLDER.mkdir();
        }
        if (!Commit.COMMIT_FOLDER.exists()) {
            Commit.COMMIT_FOLDER.mkdir();
        }
    }

    public void commit(String message) {

        if (message == "") {
            System.out.println("please enter commit message");
        }

        // read from my computer the HEAD commit and staging area

        // clone the HEAD commit
        // modify its message and timestamp according to user input
        // use staging area in order to modify the files tracked by the new commit

        // ask the computer: did we make any new objects that need to be saved onto the computer? or do we need to modify them?
        // write back any new objects made or modified

    }

}
