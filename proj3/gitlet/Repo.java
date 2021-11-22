package gitlet;

//

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.File;
import java.util.HashMap;

public class Repo implements Serializable {

    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File GITLET_FOLDER = Utils.join(CWD, ".gitlet");

    static final File STAGING_FOLDER = Utils.join(GITLET_FOLDER, "/staging");

    static final File COMMIT_FOLDER = Utils.join(GITLET_FOLDER, "/commits");

    static final File BLOB_FOLDER = Utils.join(GITLET_FOLDER, "/blobs");

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
//
//            File _initialCommitFile = Utils.join(COMMIT_FOLDER, Utils.sha1(Utils.serialize(initialCommit)));

            File _initialCommitFile = Utils.join(COMMIT_FOLDER, Utils.sha1(initialCommit));
            Utils.writeObject(_initialCommitFile, initialCommit);

            Utils.writeObject(HEAD_FILE, initialCommit);

            stage = new StagingArea();

            Utils.writeObject(STAGE_FILE, new StagingArea()); //USE STAGE? OR NEW STAGING AREA

            Utils.writeObject(COMMIT_FOLDER, _initialCommitFile); //USE STAGE? OR NEW STAGING AREA
        }
        else {
            System.out.println("Gitlet already exists");
        }
    }

    public static void add(String fileName) {

        File toAdd = Utils.join(GITLET_FOLDER, fileName);

        HEAD = getHeadCommit();
        stage = getStage();


        if (toAdd.exists()) {
            String currentFileSHA = HEAD.get_Blobs().get(fileName);
            byte[] newFile = Utils.serialize(toAdd);
            String fileSHA = Utils.sha1(newFile);

            if (fileSHA.equals(currentFileSHA)) {
                if (stage.get_stageAddition().containsKey(fileName)) {
                    stage.get_stageAddition().remove(fileName);
                }
                else if (stage.get_stageRemoval().containsKey(fileName)) {
                    stage.get_stageRemoval().remove(fileName);
                }

                setStage(stage);
                return;
            }

            if (stage.get_stageRemoval().containsKey(fileName)) {
                stage.get_stageRemoval().remove(fileName);
                setStage(stage);
            }

            stage.get_stageAddition().put(fileName, fileSHA);
            Utils.join(BLOB_FOLDER, currentFileSHA); //or current file SHA
            setStage(stage);

        }
        else {
                System.out.println("file does not exist");
                return;
        }



        /**Case 1: If the current working version of the file is identical to the version in the current commit,
         * do not stage it to be added, and remove it from the staging area if it is already there*/


    }

    public static void setStage(StagingArea a) {
        Utils.writeObject(STAGE_FILE, a);

    }
    public static StagingArea getStage() {
        return Utils.readObject(STAGE_FILE, StagingArea.class);
    }

    public static Commit getHeadCommit() {
        return Utils.readObject(HEAD_FILE, Commit.class);
    }

    public static void setHeadCommit(Commit c) {
        Utils.writeObject(HEAD_FILE, c);
    }

    public static void setupPersistence() {
        if (!GITLET_FOLDER.exists()) {
            GITLET_FOLDER.mkdir();
            STAGING_FOLDER.mkdir();
            COMMIT_FOLDER.mkdir();
            BRANCHES_FOLDER.mkdir();
            BLOB_FOLDER.mkdir();
        }
    }

    public static void commit(String message) {

        if (message == "") {
            System.out.println("please enter commit message");
        }
        if (stage.get_stageAddition().isEmpty() && stage.get_stageRemoval().isEmpty()) {
            System.out.print("no changes to the commit");
            return;
        }

        Commit curHEAD = getHeadCommit();
        StagingArea curstage = getStage();

        ArrayList<String> filesToAdd = new ArrayList<>(curstage.get_stageAddition().keySet());
        ArrayList<String> filesToRemove = new ArrayList<>(curstage.get_stageRemoval().keySet());

        Commit commitClone = new Commit(message, (HashMap<String, String>) curHEAD.get_Blobs().clone(), HEAD.SHA1());

        for (String s: filesToAdd) {
            commitClone.setBlobs(s, stage.get_stageAddition().get(s), "add");
        }

        for (String s: filesToRemove) {
            commitClone.setBlobs(s, stage.get_stageRemoval().get(s), "rm");
        }

        File newFile = Utils.join(COMMIT_FOLDER, Utils.sha1(commitClone));
        try {
            newFile.createNewFile();
        } catch (IOException excp) {
            return;
        }
        Utils.writeObject(newFile, commitClone);
        setStage(stage);

        // read from my computer the HEAD commit and staging area

        // clone the HEAD commit
        // modify its message and timestamp according to user input
        // use staging area in order to modify the files tracked by the new commit

        // ask the computer: did we make any new objects that need to be saved onto the computer? or do we need to modify them?
        // write back any new objects made or modified
    }

    static void log() {
        HEAD = getHeadCommit();
        System.out.println("===");
//        System.out.println("commit " + HEAD.get_SHA1());
        System.out.println("Date: " + HEAD.get_time());
        System.out.println(HEAD.get_message() + "\n");
    }

}
