package gitlet;

//

import org.antlr.v4.runtime.tree.Tree;

import javax.crypto.spec.PSource;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import java.io.File;

public class Repo implements Serializable {

    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File GITLET_FOLDER = Utils.join(CWD, ".gitlet");

    static final File STAGING_FOLDER = Utils.join(GITLET_FOLDER, "/staging");

    static final File COMMIT_FOLDER = Utils.join(GITLET_FOLDER, "/commits");

    /**files for serialization*/
    static final File HEAD_FILE = Utils.join(GITLET_FOLDER, "/head");
    static final File STAGE_FILE = Utils.join(GITLET_FOLDER, "/stage_add");
    static final File STAGE_RM_FILE = Utils.join(GITLET_FOLDER, "/stage_rm");

    static final File BRANCH_FILE = Utils.join(GITLET_FOLDER, "/branches");
    static final File CURRENT_BRANCH_FILE = Utils.join(GITLET_FOLDER, "/currentBranch");

    static final File COMMIT_HISTORY_FILE = Utils.join(GITLET_FOLDER, "/commitHistory");

    public static Commit HEAD;
   // private static String MASTER =  "master";

    private static StagingArea stage;

   // private static String currentBranch = "";

    private static TreeMap<String, String> branchesHash = new TreeMap<>();

    private static TreeMap<String, Commit> commitHistory = new TreeMap<>();

    public static void init() {
        if (!GITLET_FOLDER.exists()) {
            setupPersistence();

            Commit initialCommit = new Commit();
            String initCommitSHA1 = Utils.sha1(Utils.serialize(initialCommit));
            File _initialCommitFile = Utils.join(COMMIT_FOLDER, (initCommitSHA1));

            if (!_initialCommitFile.exists()) {
                try {
                    _initialCommitFile.createNewFile();
                } catch (IOException err) {
                    return;
                }
            }

            Utils.writeObject(_initialCommitFile, initialCommit);

            branchesHash.put("master", initCommitSHA1);

            setBranches(branchesHash);
            setHeadCommit(initialCommit);

            Utils.writeContents(CURRENT_BRANCH_FILE, "master");

            commitHistory.put(initCommitSHA1, initialCommit);
            setCommitHistory(commitHistory);

            stage = new StagingArea();
            Utils.writeObject(STAGE_FILE, new StagingArea());
//            Utils.writeObject(STAGE_RM_FILE, new StagingArea());

        }
        else {
            System.out.println("Gitlet already exists");
        }
    }

    public static void add(String fileName) {

        File toAdd = Utils.join(CWD, fileName);

        HEAD = getHeadCommit();
        stage = getStage();

        if (toAdd.exists()) {
            byte[] currentFile = HEAD.get_Blobs().get(fileName);
            byte[] newFile = Utils.readContents(toAdd);
//            String fileSHA = Utils.sha1(newFile);

            if (Arrays.equals(newFile, currentFile)) {
                if (stage.get_stageAddition().containsKey(fileName)) {
                    stage.get_stageAddition().remove(fileName);
                }
                if (stage.get_stageRemoval().containsKey(fileName)) {
                    stage.get_stageRemoval().remove(fileName);
                }

                setStage(stage);
                return;
            }


            if (stage.get_stageRemoval().containsKey(fileName)) {
                try {
                    toAdd.createNewFile();
                }
                catch (IOException exc) {
                    return;
                }

                byte[] toRemove = stage.get_stageRemoval().get(fileName);
                Utils.writeContents(toAdd, toRemove);

                stage.get_stageRemoval().remove(fileName);
                setStage(stage);
            }


            stage.get_stageAddition().put(fileName, newFile);
            setHeadCommit(HEAD);
            setStage(stage);

        }
        else {
            System.out.println("file does not exist");
            return;
        }
    }

    public static void rm(String fileName) {
        HEAD = getHeadCommit();
        stage = getStage();
        File toRemove = Utils.join(CWD, fileName);

        if (stage.get_stageAddition().containsKey(fileName)) {
            stage.get_stageAddition().remove(fileName);
            setStage(stage);
            return;
        }
        if (!HEAD.get_Blobs().containsKey(fileName)) {
            System.out.println("No reason to remove the file.");
            return;
        }

        byte[] blobsToDelete = HEAD.get_Blobs().get(fileName);
        stage.get_stageRemoval().put(fileName, blobsToDelete);
        Utils.restrictedDelete(toRemove);
        setStage(stage);
        Utils.writeObject(HEAD_FILE, HEAD);
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

    public static TreeMap<String, String> getBranches()
    {
        return Utils.readObject(BRANCH_FILE, TreeMap.class);
    }

    public static void setBranches(TreeMap h)
    {
        Utils.writeObject(BRANCH_FILE, h);
    }

    public static String getCurrentBranchName() {
        return Utils.readContentsAsString(CURRENT_BRANCH_FILE);
    }

    public static void setCurrentBranchName(String name) {
        Utils.writeContents(CURRENT_BRANCH_FILE, name);
    }


    public static TreeMap<String, Commit> getCommitHistory(){
        return Utils.readObject(COMMIT_HISTORY_FILE, TreeMap.class);
    }

    public static void setCommitHistory(TreeMap l){
        Utils.writeObject(COMMIT_HISTORY_FILE, l);
    }




//    public static String getBranches() {
//        return Utils.readObject(BRANCH_FILE, String.class);
//    }

    public static void setupPersistence() {
        if (!GITLET_FOLDER.exists()) {
            GITLET_FOLDER.mkdir();
            STAGING_FOLDER.mkdir();
            COMMIT_FOLDER.mkdir();
        }
        try {
            HEAD_FILE.createNewFile();
            CURRENT_BRANCH_FILE.createNewFile();
            BRANCH_FILE.createNewFile();
            STAGE_FILE.createNewFile();
            COMMIT_HISTORY_FILE.createNewFile();
            STAGE_RM_FILE.createNewFile();
        }
        catch (IOException err) {
            return;
        }

    }

    public static void commit(String message) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }

        HEAD = getHeadCommit();
        stage = getStage();
        commitHistory = getCommitHistory();

        if (stage.get_stageAddition().isEmpty() && stage.get_stageRemoval().isEmpty()) {
            System.out.print("No changes added to the commit.");
            return;
        }

        TreeMap<String, byte[]> tempClone = new TreeMap<>();

        if (HEAD.get_Blobs() != null) {
            tempClone.putAll(HEAD.get_Blobs());
        }

        ArrayList<String> filesToAdd = new ArrayList<>(stage.get_stageAddition().keySet());
        ArrayList<String> filesToRemove = new ArrayList<>(stage.get_stageRemoval().keySet());

        /**the new commit*/
        Commit commitClone = new Commit(message, tempClone, Utils.sha1(Utils.serialize(HEAD)));

        for (String s: filesToAdd) {
            commitClone.setBlobs(s, stage.get_stageAddition().get(s), "add");
        }

        for (String s: filesToRemove) {
            commitClone.setBlobs(s, stage.get_stageRemoval().get(s), "rm");
        }

        stage.clear();
        setStage(stage);
        setHeadCommit(commitClone);

        String newCommitSHA1 = Utils.sha1(Utils.serialize(commitClone));

        File newFile = Utils.join(COMMIT_FOLDER, newCommitSHA1);
        try {
            newFile.createNewFile();
        } catch (IOException excp) {
            return;
        }


        String currentBranch = getCurrentBranchName();

        branchesHash = getBranches();
        branchesHash.put(currentBranch, newCommitSHA1);

        setBranches(branchesHash);

//        Utils.writeObject(CURRENT_BRANCH_FILE, currentBranch);


        Utils.writeObject(newFile, commitClone);
        commitHistory.put(newCommitSHA1, commitClone);
        setCommitHistory(commitHistory);
    }

    public static void checkout (String fileName) {
        HEAD = getHeadCommit(); //current branch
        byte[] currentBlobContents = HEAD.get_Blobs().get(fileName);

        if (!HEAD.get_Blobs().containsKey(fileName)) {
            System.out.println("file doesn't exist in the commit");
            return;
        }
        //get the byte array that the sha returns
        // maintain sha1 id to byte array

        /**Make sure that you are using Utils.writeContents() instead of Utils.writeObject()
         * when you are writing a string into a file!*/
        Utils.writeContents(Utils.join(CWD, fileName), currentBlobContents);

    }

    public static void checkout2 (String commitID, String fileName) {


        LinkedHashMap<String, Commit> tempHistory =  new LinkedHashMap<>();
        tempHistory.putAll(getCommitHistory());
//
//        System.out.println(Utils.readObject(COMMIT_HISTORY_FILE, LinkedHashMap.class));

        File newFile = Utils.join(COMMIT_FOLDER, commitID);
        if (!tempHistory.containsKey(commitID)) {
            System.out.println("No commit with that id exists.");
            return;
        }
         {
            try {
                Commit newCommit = Utils.readObject(newFile, Commit.class);
                if (!newCommit.get_Blobs().containsKey(fileName)) {
                    System.out.println("File does not exist in that commit.");
                    return;
                }
//                String blobSHA = Utils.sha1(Utils.serialize(newCommit.get_Blobs().get(fileName)));
                Utils.writeContents(Utils.join(CWD, fileName), newCommit.get_Blobs().get(fileName));
            } catch (IllegalArgumentException excp) {
                return;
            }
        }
    }

    public static void checkout3(String branchName) {
//        TreeMap<String, String> currentBranch = getBranches().get(branchName);
        branchesHash = getBranches();
        String currentBranch = getCurrentBranchName();
        HEAD = getHeadCommit();
        stage = getStage();

        if (!branchesHash.containsKey(branchName)) {
            System.out.println(" No such branch exists.");
            return;
        }
        if (currentBranch.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        for (String fileName: Utils.plainFilenamesIn(CWD)) {
            if (!HEAD.get_Blobs().containsKey(fileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }

        String requestedCommitSHA = branchesHash.get(branchName);
        Commit requestCommit = Utils.readObject(Utils.join(COMMIT_FOLDER, requestedCommitSHA), Commit.class);
        HEAD = requestCommit;
        setHeadCommit(HEAD);

        stage.clear();
        setStage(stage);

        for (String fileName: Utils.plainFilenamesIn(CWD)) {
            Utils.restrictedDelete(Utils.join(CWD, fileName));
        }

        for (String fileName: requestCommit.get_Blobs().keySet()) {
            try {
                Utils.join(CWD, fileName).createNewFile();
            } catch (IOException exc) {
                return;
            }
            Utils.writeContents(Utils.join(CWD, fileName), requestCommit.get_Blobs().get(fileName));
        }

        setCurrentBranchName(branchName);

    }

    static void log() {
        Commit tempHead = getHeadCommit();
        TreeMap<String, Commit> tempCommitHist = (TreeMap<String, Commit>) getCommitHistory().clone();

        if (tempHead == null) {
            return;
        }
        while (tempHead.getParentSHA() != null) {
            System.out.println("===");
            System.out.println("commit " + Utils.sha1(Utils.serialize(tempHead)));
            System.out.println("Date: " + tempHead.get_time());
            System.out.println(tempHead.get_message() + "\n");

            tempHead = tempCommitHist.get(tempHead.getParentSHA());
        }

        System.out.println("===");
        System.out.println("commit " + Utils.sha1(Utils.serialize(tempHead)));
        System.out.println("Date: " + tempHead.get_time());
        System.out.println(tempHead.get_message() + "\n");

    }

    static void globalLog() {

//        System.out.println("commit-folder" + Utils.plainFilenamesIn(COMMIT_FOLDER));
        for (String commitSHA: Utils.plainFilenamesIn(COMMIT_FOLDER)) {
            Commit commit = Utils.readObject(Utils.join(COMMIT_FOLDER, commitSHA), Commit.class);
            System.out.println("===");
            System.out.println("commit " + commitSHA);
            System.out.println("Date: " + commit.get_time());
            System.out.println(commit.get_message() + "\n");
        }
    }

    static void find(String message) {

//        System.out.println("commit-folder" + Utils.plainFilenamesIn(COMMIT_FOLDER));
        boolean hasMessage = false;
        for (String commitSHA: Utils.plainFilenamesIn(COMMIT_FOLDER)) {
            Commit commit = Utils.readObject(Utils.join(COMMIT_FOLDER, commitSHA), Commit.class);
            if (commit.get_message().equals(message)) {
                System.out.println(commitSHA);
                hasMessage = true;
            }
        }
        if (hasMessage == false) {
            System.out.println("Found no commit with that message.");
        }
    }

    static void branch(String branchName) {
        HEAD = getHeadCommit();
        branchesHash = getBranches();
        if (branchesHash.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        branchesHash.put(branchName, Utils.sha1(Utils.serialize(HEAD)));
        setBranches(branchesHash);
    }

    static void rmBranch(String branchName) {
        branchesHash = getBranches();
        String currentBranch = getCurrentBranchName();

        if (!branchesHash.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (currentBranch.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        branchesHash.remove(branchName);
        setBranches(branchesHash);

    }

    static void reset(String commitID) {
        HEAD = getHeadCommit();
        stage = getStage();

        if (!Utils.plainFilenamesIn(COMMIT_FOLDER).contains(commitID)) {
            System.out.println("No commit with that id exists.");
            return;
        }

        Commit commit = Utils.readObject(Utils.join(COMMIT_FOLDER, commitID), Commit.class);
        for (String file: Utils.plainFilenamesIn(CWD)) {
            if (commit.get_Blobs().containsKey(file) && !HEAD.get_Blobs().containsKey(file)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }



        for (String file: Utils.plainFilenamesIn(CWD)) {
            Utils.restrictedDelete(Utils.join(CWD, file));
        }

        for (String file: commit.get_Blobs().keySet()) {
            try {
                Utils.join(CWD, file).createNewFile();
            } catch (IOException excp) {
                return;
            }

            Utils.writeContents(Utils.join(CWD, file), commit.get_Blobs().get(file));
        }

        stage.clear();
        setStage(stage);

        HEAD = commit;
        setHeadCommit(HEAD);

        branchesHash = getBranches();
        String currentBranch = getCurrentBranchName();
        branchesHash.put(currentBranch, commitID);
        setBranches(branchesHash);
    }

    public static void status() {
        HEAD = getHeadCommit();
        branchesHash = getBranches();
        String currentBranch = getCurrentBranchName();
        stage = getStage();

        System.out.println("=== Branches ===");
        for (String branchName: branchesHash.keySet()) {
            if (branchName.equals(currentBranch)) {
                System.out.println("*" + currentBranch) ;
            }
            else {
                System.out.println(branchName);
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        for (String addFiles: stage.get_stageAddition().keySet()) {
            System.out.println(addFiles);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String removeFiles: stage.get_stageRemoval().keySet()) {
            System.out.println(removeFiles);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        /**
        for (String fileName: HEAD.get_Blobs().keySet()) {
            //tracked in current commit, changed in working directory
            File blobFile = Utils.join(CWD, fileName);

            String commitSHA1 = Utils.sha1(HEAD.get_Blobs().get(blobFile));
            String blobSHA1 = Utils.sha1(blobFile);

            if ((!commitSHA1.equals(blobSHA1) &&
                    !stage.get_stageRemoval().containsKey(fileName) &&
                    !stage.get_stageAddition().containsKey(fileName)) ||(
                    stage.get_stageAddition().containsKey(fileName) && !commitSHA1.equals(blobSHA1))
            ) {
                System.out.println(fileName + " (modified)");
            }

            if ((!blobFile.exists() && stage.get_stageAddition().containsKey(fileName) ) ||
                    (!blobFile.exists() && stage.get_stageRemoval().containsKey(fileName))) {
                System.out.println(fileName + " (deleted)");
            }

        }
         */


        System.out.println();
        System.out.println("=== Untracked Files ===");
        // staged for addition, but with different contents

//        for (String fileName: HEAD.get_Blobs().keySet()) {
//
//        }

    }

}
