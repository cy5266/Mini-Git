package gitlet;

import java.io.IOException;
import java.io.Serializable;

import java.nio.charset.StandardCharsets;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.TreeMap;

/** A class for all methods.
 *  @author Cindy Yang
 */
public class Repo implements Serializable {

    /** Main metadata folder. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main Gitlet folder. */
    static final File GITLET_FOLDER = Utils.join(CWD, ".gitlet");

    /** Main staging folder. */
    static final File STAGING_FOLDER = Utils.join(GITLET_FOLDER, "/staging");

    /** Main commit folder. */
    static final File COMMIT_FOLDER = Utils.join(GITLET_FOLDER, "/commits");

    /**files for serialization - HEAD. */
    static final File HEAD_FILE = Utils.join(GITLET_FOLDER, "/head");

    /**files for serialization - STAGE. */
    static final File STAGE_FILE = Utils.join(GITLET_FOLDER, "/stage_add");

    /**files for serialization - REMOVE. */
    static final File STAGE_RM_FILE = Utils.join(GITLET_FOLDER, "/stage_rm");

    /**files for serialization - BRANCHES. */
    static final File BRANCH_FILE = Utils.join(GITLET_FOLDER, "/branches");

    /**files for serialization - CURRENT BRANCH (STORES STRING). */
    static final File CURRENT_BRANCH_FILE =
            Utils.join(GITLET_FOLDER, "/currentBranch");

    /**files for serialization - COMMIT HISTORY. */
    static final File COMMIT_HISTORY_FILE =
            Utils.join(GITLET_FOLDER, "/commitHistory");

    /**files for serialization - REMOTE. */
    static final File REMOTE_FILE = Utils.join(GITLET_FOLDER, "/remote");

    /**variable HEAD. */
    private static Commit head;

    /**variable for the staging area. */
    private static StagingArea stage;

    /**variable for the branches. */
    private static TreeMap<String, String> branchesHash =
            new TreeMap<String, String>();

    /**variable for the commitHistory. */
    private static TreeMap<String, Commit> commitHistory = new TreeMap<>();

    /**variable for the remote files. */
    private static TreeMap<String, File> remotes = new TreeMap<>();

    /**variable for the remote branches. */
    private static TreeMap<String, String> remoteBranches =
            new TreeMap<String, String>();

    /**length for sha1. */
    private static final int DEFAULTSHALENGTH = 40;

    /**method for initial commit. */
    public static void init() {
        if (!GITLET_FOLDER.exists()) {
            setupPersistence();

            Commit initialCommit = new Commit();
            String initCommitSHA1 = Utils.sha1(Utils.serialize(initialCommit));
            File initialCommitFile =
                    Utils.join(COMMIT_FOLDER, (initCommitSHA1));

            if (!initialCommitFile.exists()) {
                try {
                    initialCommitFile.createNewFile();
                } catch (IOException err) {
                    return;
                }
            }

            Utils.writeObject(initialCommitFile, initialCommit);

            branchesHash.put("master", initCommitSHA1);

            setBranches(branchesHash);
            setHeadCommit(initialCommit);

            Utils.writeContents(CURRENT_BRANCH_FILE, "master");

            commitHistory.put(initCommitSHA1, initialCommit);
            setCommitHistory(commitHistory);

            stage = new StagingArea();
            Utils.writeObject(STAGE_FILE, new StagingArea());
            Utils.writeObject(REMOTE_FILE, new TreeMap<String, File>());
        }  else {
            System.out.println("A Gitlet version-control system already "
                    + "exists in the current directory.");
        }
    }

    /**method for add commit.
     * @param fileName name of file to add */
    public static void add(String fileName) {

        File toAdd = Utils.join(CWD, fileName);

        head = getHeadCommit();
        stage = getStage();

        if (toAdd.exists()) {
            byte[] currentFile = head.getBlobs().get(fileName);
            byte[] newFile = Utils.readContents(toAdd);

            if (Arrays.equals(newFile, currentFile)) {
                if (stage.getStageAddition().containsKey(fileName)) {
                    stage.getStageAddition().remove(fileName);
                }
                if (stage.getStageRemoval().containsKey(fileName)) {
                    stage.getStageRemoval().remove(fileName);
                }

                setStage(stage);
                return;
            }


            if (stage.getStageRemoval().containsKey(fileName)) {
                try {
                    toAdd.createNewFile();
                } catch (IOException exc) {
                    return;
                }

                byte[] toRemove = stage.getStageRemoval().get(fileName);
                Utils.writeContents(toAdd, toRemove);

                stage.getStageRemoval().remove(fileName);
                setStage(stage);
            }


            stage.getStageAddition().put(fileName, newFile);
            setHeadCommit(head);
            setStage(stage);

        } else {
            System.out.println("file does not exist");
            return;
        }
    }

    /**method for remove commit.
     * @param fileName name of file to remove */

    public static void rm(String fileName) {
        head = getHeadCommit();
        stage = getStage();
        File toRemove = Utils.join(CWD, fileName);

        if (stage.getStageAddition().containsKey(fileName)) {
            stage.getStageAddition().remove(fileName);
            setStage(stage);
            return;
        }
        if (!head.getBlobs().containsKey(fileName)) {
            System.out.println("No reason to remove the file.");
            return;
        }

        byte[] blobsToDelete = head.getBlobs().get(fileName);
        stage.getStageRemoval().put(fileName, blobsToDelete);
        Utils.restrictedDelete(toRemove);
        setStage(stage);
        Utils.writeObject(HEAD_FILE, head);
    }

    /**method writing to the stage file.
     * @param a name of file to write*/

    public static void setStage(StagingArea a) {
        Utils.writeObject(STAGE_FILE, a);
    }

    /**method reading in the stage file.
     @ return the read in object*/
    public static StagingArea getStage() {
        return Utils.readObject(STAGE_FILE, StagingArea.class);
    }

    /**method reading in the head file.
     @ return the read in object*/
    public static Commit getHeadCommit() {
        return Utils.readObject(HEAD_FILE, Commit.class);
    }

    /**method writing to the head file.
     * @param c commit file to write to local file*/

    public static void setHeadCommit(Commit c) {
        Utils.writeObject(HEAD_FILE, c);
    }

    /**method reading in the branch file.
     @ return the read in object*/
    public static TreeMap<String, String> getBranches() {
        return Utils.readObject(BRANCH_FILE, TreeMap.class);
    }

    /**method writing to the branch file.
     * @param h name of file contents to write to branch file*/

    public static void setBranches(TreeMap h) {
        Utils.writeObject(BRANCH_FILE, h);
    }

    /**method reading in the stage file.
     @ return the read in contents (branch name)*/
    public static String getCurrentBranchName() {
        return Utils.readContentsAsString(CURRENT_BRANCH_FILE);
    }

    /**method writing to the current branch file.
     * @param name name of file contents to write to branch file*/
    public static void setCurrentBranchName(String name) {
        Utils.writeContents(CURRENT_BRANCH_FILE, name);
    }

    /**method reading in the commit history file.
     @ return the read in contents*/
    public static TreeMap<String, Commit> getCommitHistory() {
        return Utils.readObject(COMMIT_HISTORY_FILE, TreeMap.class);
    }

    /**method writing to the commit history file.
     * @param l name of file contents to write to commit history file*/

    public static void setCommitHistory(TreeMap l) {
        Utils.writeObject(COMMIT_HISTORY_FILE, l);
    }

    /** create all folders and files needed. */
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
            REMOTE_FILE.createNewFile();
        } catch (IOException err) {
            return;
        }

    }

    /** commit method.
     * @param message commit message */
    public static void commit(String message) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }

        head = getHeadCommit(); stage = getStage();
        commitHistory = getCommitHistory();

        if (stage.getStageAddition().isEmpty()
                && stage.getStageRemoval().isEmpty()) {
            System.out.print("No changes added to the commit.");
            return;
        }

        TreeMap<String, byte[]> tempClone = new TreeMap<>();

        if (head.getBlobs() != null) {
            tempClone.putAll(head.getBlobs());
        }

        ArrayList<String> filesToAdd =
                new ArrayList<>(stage.getStageAddition().keySet());
        ArrayList<String> filesToRemove =
                new ArrayList<>(stage.getStageRemoval().keySet());

        Commit commitClone = new Commit(message,
                tempClone, Utils.sha1(Utils.serialize(head)));

        for (String s: filesToAdd) {
            commitClone.setBlobs(s, stage.getStageAddition().get(s), "add");
        }

        for (String s: filesToRemove) {
            commitClone.setBlobs(s, stage.getStageRemoval().get(s), "rm");
        }

        stage.clear(); setStage(stage); setHeadCommit(commitClone);

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

        Utils.writeObject(newFile, commitClone);
        commitHistory.put(newCommitSHA1, commitClone);
        setCommitHistory(commitHistory);
    }


    /** checkout method.
     * @param fileName name of file. */
    public static void checkout(String fileName) {
        head = getHeadCommit();
        byte[] currentBlobContents = head.getBlobs().get(fileName);

        if (!head.getBlobs().containsKey(fileName)) {
            System.out.println("file doesn't exist in the commit");
            return;
        }
        Utils.writeContents(Utils.join(CWD, fileName),
                currentBlobContents);

    }

    /** second checkout method.
     * @param commitID SHA1 of commit to checkout
     * @param fileName filename to checkout*/
    public static void checkout2(String commitID, String fileName) {

        if (commitID.length() < DEFAULTSHALENGTH) {
            for (String fullCommitID: Utils.plainFilenamesIn(COMMIT_FOLDER)) {
                if (fullCommitID.contains(commitID)) {
                    commitID = fullCommitID;
                }
            }
        }

        LinkedHashMap<String, Commit> tempHistory =  new LinkedHashMap<>();
        tempHistory.putAll(getCommitHistory());


        File newFile = Utils.join(COMMIT_FOLDER, commitID);
        if (!tempHistory.containsKey(commitID)) {
            System.out.println("No commit with that id exists.");
            return;
        }
        {
            try {
                Commit newCommit = Utils.readObject(newFile, Commit.class);
                if (!newCommit.getBlobs().containsKey(fileName)) {
                    System.out.println("File does not exist in that commit.");
                    return;
                }
                Utils.writeContents(Utils.join(CWD, fileName),
                        newCommit.getBlobs().get(fileName));
            } catch (IllegalArgumentException excp) {
                return;
            }
        }
    }

    /** second checkout method.
     * @param branchName name of branch*/
    public static void checkout3(String branchName) {
        branchesHash = getBranches();
        String currentBranch = getCurrentBranchName();
        head = getHeadCommit();
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
            if (!head.getBlobs().containsKey(fileName)) {
                System.out.println(
                        "There is an untracked file in the way; "
                                + "delete it, or add and commit it first.");
                return;
            }
        }

        String requestedCommitSHA = branchesHash.get(branchName);
        Commit requestCommit =
                Utils.readObject(Utils.join(
                        COMMIT_FOLDER, requestedCommitSHA), Commit.class);
        head = requestCommit;
        setHeadCommit(head);

        stage.clear();
        setStage(stage);

        for (String fileName: Utils.plainFilenamesIn(CWD)) {
            Utils.restrictedDelete(Utils.join(CWD, fileName));
        }

        for (String fileName: requestCommit.getBlobs().keySet()) {
            try {
                Utils.join(CWD, fileName).createNewFile();
            } catch (IOException exc) {
                return;
            }
            Utils.writeContents(
                    Utils.join(CWD, fileName),
                    requestCommit.getBlobs().get(fileName));
        }

        setCurrentBranchName(branchName);

    }

    /** log.*/
    static void log() {
        Commit tempHead = getHeadCommit();
        TreeMap<String, Commit> tempCommitHist =
                (TreeMap<String, Commit>) getCommitHistory().clone();

        if (tempHead == null) {
            return;
        }
        while (tempHead.getParentSHA() != null) {
            System.out.println("===");
            System.out.println("commit "
                    + Utils.sha1(Utils.serialize(tempHead)));
            System.out.println("Date: " + tempHead.getTime());
            System.out.println(tempHead.getMessage() + "\n");

            tempHead = tempCommitHist.get(tempHead.getParentSHA());
        }

        System.out.println("===");
        System.out.println("commit " + Utils.sha1(Utils.serialize(tempHead)));
        System.out.println("Date: " + tempHead.getTime());
        System.out.println(tempHead.getMessage() + "\n");

    }

    /** global log. */
    static void globalLog() {
        for (String commitSHA: Utils.plainFilenamesIn(COMMIT_FOLDER)) {
            Commit commit = Utils.readObject(
                    Utils.join(COMMIT_FOLDER, commitSHA), Commit.class);
            System.out.println("===");
            System.out.println("commit " + commitSHA);
            System.out.println("Date: " + commit.getTime());
            System.out.println(commit.getMessage() + "\n");
        }
    }

    /** find.
     * @param message based on message*/
    static void find(String message) {

        boolean hasMessage = false;
        for (String commitSHA: Utils.plainFilenamesIn(COMMIT_FOLDER)) {
            Commit commit = Utils.readObject(
                    Utils.join(COMMIT_FOLDER, commitSHA), Commit.class);
            if (commit.getMessage().equals(message)) {
                System.out.println(commitSHA);
                hasMessage = true;
            }
        }
        if (!hasMessage) {
            System.out.println("Found no commit with that message.");
        }
    }

    static void branch(String branchName) {
        head = getHeadCommit();
        branchesHash = getBranches();
        if (branchesHash.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        branchesHash.put(branchName, Utils.sha1(Utils.serialize(head)));
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
        head = getHeadCommit();
        stage = getStage();

        if (!Utils.plainFilenamesIn(COMMIT_FOLDER).contains(commitID)) {
            System.out.println("No commit with that id exists.");
            return;
        }

        Commit commit = Utils.readObject(
                Utils.join(COMMIT_FOLDER, commitID), Commit.class);
        for (String file: Utils.plainFilenamesIn(CWD)) {
            if (commit.getBlobs().containsKey(file)
                    && !head.getBlobs().containsKey(file)) {
                System.out.println(
                        "There is an untracked file in the way; delete it,"
                                + " or add and commit it first.");
                return;
            }
        }



        for (String file: Utils.plainFilenamesIn(CWD)) {
            Utils.restrictedDelete(Utils.join(CWD, file));
        }

        for (String file: commit.getBlobs().keySet()) {
            try {
                Utils.join(CWD, file).createNewFile();
            } catch (IOException excp) {
                return;
            }

            Utils.writeContents(
                    Utils.join(CWD, file), commit.getBlobs().get(file));
        }

        stage.clear();
        setStage(stage);

        head = commit;
        setHeadCommit(head);

        branchesHash = getBranches();
        String currentBranch = getCurrentBranchName();
        branchesHash.put(currentBranch, commitID);
        setBranches(branchesHash);
    }

    public static void status() {
        head = getHeadCommit(); branchesHash = getBranches();
        String currentBranch = getCurrentBranchName(); stage = getStage();

        System.out.println("=== Branches ===");
        for (String branchName: branchesHash.keySet()) {
            if (branchName.equals(currentBranch)) {
                System.out.println("*" + currentBranch);
            } else {
                System.out.println(branchName);
            }
        }
        System.out.println(); System.out.println("=== Staged Files ===");
        for (String addFiles: stage.getStageAddition().keySet()) {
            System.out.println(addFiles);
        }
        System.out.println(); System.out.println("=== Removed Files ===");
        for (String removeFiles: stage.getStageRemoval().keySet()) {
            System.out.println(removeFiles);
        }
        System.out.println();
        TreeMap<String, byte[]> headBlobs = head.getBlobs();

        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String fileName : headBlobs.keySet()) {

            File blobFile = Utils.join(CWD, fileName);
            if ((!blobFile.exists()
                    && !stage.getStageRemoval().containsKey(fileName))
                    || !blobFile.exists()
                    && stage.getStageAddition().containsKey(fileName)) {
                System.out.println(fileName + " (deleted)"); break;
            }

            String headBlob = Utils.sha1(head.getBlobs().get(fileName));
            String string = "";

            if (blobFile.isFile()) {
                string = Utils.sha1(Utils.readContents(blobFile));
            }

            if ((!stage.getStageRemoval().containsKey(fileName)
                    && !string.equals(headBlob)
                    && !stage.getStageAddition().containsKey(fileName))
                    || (stage.getStageAddition().containsKey(fileName)
                    && !headBlob.equals(
                            Utils.sha1(Utils.readContents(blobFile))))) {
                System.out.println(fileName + " (modified)");
            }
        }

        System.out.println(); System.out.println("=== Untracked Files ===");
        for (String fileName : Utils.plainFilenamesIn(CWD)) {
            if (!headBlobs.containsKey(fileName)
                    && !stage.getStageAddition().containsKey(fileName)) {
                System.out.println(fileName);
            }
        }

    }

    public static void merge(String mergeBranch) {
        head = getHeadCommit(); branchesHash = getBranches();
        String currentBranchName = getCurrentBranchName();
        stage = getStage(); errors(mergeBranch);

        Commit mergeBranchCommit = Utils.readObject(Utils.join(
                        COMMIT_FOLDER, branchesHash.get(mergeBranch)), Commit.class);

        Commit splitCommit = split(mergeBranch);

        if (Utils.sha1(Utils.serialize(splitCommit)).equals(
                        branchesHash.get(currentBranchName))) {
            checkout3(mergeBranch);
            System.out.println("Current branch fast-forwarded."); return;
        }
        if (Utils.sha1(Utils.serialize(splitCommit)).equals(
                        branchesHash.get(mergeBranch))) {
            System.out.println(
                    "Given branch is an ancestor of the current branch."); return;
        }


        ArrayList<String> fileNames = new ArrayList<>();
        fileNames = getAllFileNames(splitCommit, head, mergeBranchCommit);
        int action;
        boolean hasConflict = false;

        for (String file: fileNames) {
            byte[] headFile = head.getBlobs().get(file);
            byte[] splitFile = splitCommit.getBlobs().get(file);
            byte[] mergeFile = mergeBranchCommit.getBlobs().get(file);

            action = mergeCases(headFile, splitFile, mergeFile);

            if (action == 1) {
                File dir = Utils.join(CWD, file);
                Utils.writeContents(dir, mergeFile);

                stage.getStageAddition().put(file, mergeFile);

            } else if (action == 2) {
                stage.getStageRemoval().put(file, headFile);
                Utils.restrictedDelete(Utils.join(CWD, file));

            } else if (action == 3) {

                String combinedContents = combined(headFile, mergeFile);

                File stagingFile = Utils.join(CWD, file);
                Utils.writeContents(stagingFile, combinedContents);
                stage.getStageAddition().put(
                        file, Utils.readContents(stagingFile));

                hasConflict = true;
            }
            setStage(stage);

        }

        if (hasConflict) {
            System.out.println("Encountered a merge conflict.");

        }

        mergeCommit(("Merged " + mergeBranch + " into " + currentBranchName + "."),
                mergeBranchCommit);
        setStage(stage);

    }

    public static void errors(String mergeBranch) {
        head = getHeadCommit();
        branchesHash = getBranches();
        String currentBranchName = getCurrentBranchName();
        stage = getStage();

        if (!stage.getStageRemoval().isEmpty()
                || !stage.getStageAddition().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if (branchesHash.get(currentBranchName).equals(
                branchesHash.get(mergeBranch))) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        if (!branchesHash.containsKey(mergeBranch)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        Commit mergeBranchCommit = Utils.readObject
                (Utils.join(
                        COMMIT_FOLDER,
                        branchesHash.get(mergeBranch)), Commit.class);

        for (String filename : Utils.plainFilenamesIn(CWD)) {
            if (!head.getBlobs().containsKey(filename)
                    &&
                    mergeBranchCommit.getBlobs().containsKey(filename)) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                return;
            }
        }
    }

    public static String combined(byte[] headbyte, byte[] merged) {
        String result;
        if (merged == null) {
            result = "<<<<<<< HEAD\n"
                    + new String(headbyte, StandardCharsets.UTF_8)
                    + "=======\n>>>>>>>\n";
        } else {
            result = "<<<<<<< HEAD\n"
                    + new String(headbyte, StandardCharsets.UTF_8)
                    + "=======\n"
                    + new String(merged, StandardCharsets.UTF_8) + ">>>>>>>\n";
        }
        return result;
    }


    public static void mergeCommit(String message, Commit mergedBranch) {
        head = getHeadCommit();
        branchesHash = getBranches();
        String currentBranch = getCurrentBranchName();
        stage = getStage();
        commitHistory = getCommitHistory();

        TreeMap<String, byte[]> tempClone = new TreeMap<>();

        if (head.getBlobs() != null) {
            tempClone.putAll(head.getBlobs());
        }

        ArrayList<String> filesToAdd =
                new ArrayList<>(stage.getStageAddition().keySet());
        ArrayList<String> filesToRemove =
                new ArrayList<>(stage.getStageRemoval().keySet());

        Commit commitClone = new Commit(
                message, tempClone, Utils.sha1(Utils.serialize(head)));

        for (String s: filesToAdd) {
            commitClone.setBlobs(s, stage.getStageAddition().get(s), "add");
        }

        for (String s: filesToRemove) {
            commitClone.setBlobs(s, stage.getStageRemoval().get(s), "rm");
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



        branchesHash = getBranches();
        branchesHash.put(currentBranch, newCommitSHA1);

        setBranches(branchesHash);



        Utils.writeObject(newFile, commitClone);
        commitHistory.put(newCommitSHA1, commitClone);
        setCommitHistory(commitHistory);



    }


    public static int mergeCases(
            byte[] headFile, byte[] splitFile, byte[] mergeFile) {

        String splitSHA = Utils.sha1(Utils.serialize(splitFile));
        String headSHA = Utils.sha1(Utils.serialize(headFile));
        String mergeSHA = Utils.sha1(Utils.serialize(mergeFile));

        if (splitFile != null && headFile != null
                && splitSHA.equals(headSHA) && mergeFile != null
            && !headSHA.equals(mergeSHA)) {
            return 1;
        }

        if (splitFile != null && headFile != null
                && !splitSHA.equals(headSHA) && mergeFile != null
                && !headSHA.equals(mergeSHA) && !mergeSHA.equals(splitSHA)) {
            return 3;
        }

        if (splitFile != null && headFile != null
                && splitSHA.equals(headSHA) && mergeFile == null) {
            return 2;
        }

        if (splitFile == null && headFile == null && mergeFile != null) {
            return 1;
        }

        if (splitFile != null && headFile != null
                && !splitSHA.equals(headSHA) && mergeFile == null) {
            return 3;
        }


        return 0;
    }

    public static ArrayList getAllFileNames(
            Commit splitHeadPointer, Commit headPointer, Commit mergePointer) {
        ArrayList<String> fileNames =
                new ArrayList<>(splitHeadPointer.getBlobs().keySet());

        for (String key: headPointer.getBlobs().keySet()) {
            if (!fileNames.contains(key)) {
                fileNames.add(key);
            }
        }

        for (String key: mergePointer.getBlobs().keySet()) {
            if (!fileNames.contains(key)) {
                fileNames.add(key);
            }
        }

        return fileNames;
    }

    public static Commit split(String branch) {
        branchesHash = getBranches();
        ArrayList<String> headHistory = new ArrayList<>();

        Commit tempHead = getHeadCommit();
        TreeMap<String, Commit> tempCommitHist
                = (TreeMap<String, Commit>) getCommitHistory().clone();

        while (tempHead.getParentSHA() != null) {
            headHistory.add(Utils.sha1(Utils.serialize(tempHead)));
            tempHead = tempCommitHist.get(tempHead.getParentSHA());
        }

        headHistory.add(Utils.sha1(Utils.serialize(tempHead)));


        String branchSHA1 = branchesHash.get(branch);

        Commit branchCommit = Utils.readObject(
                Utils.join(COMMIT_FOLDER, branchSHA1), Commit.class);

        String splitSHA = null;

        while (branchCommit.getParentSHA() != null) {
            if (headHistory.contains(
                    Utils.sha1(Utils.serialize(branchCommit)))) {
                splitSHA = Utils.sha1(Utils.serialize(branchCommit));
                break;
            }

            branchCommit = tempCommitHist.get(branchCommit.getParentSHA());

        }
        return Utils.readObject(
                Utils.join(COMMIT_FOLDER, splitSHA), Commit.class);
    }

    /** remove remote.
     * @return remoteFile*/
    public static TreeMap<String, File> getRemote() {
        return Utils.readObject(REMOTE_FILE, TreeMap.class);
    }

    /** remove remote.
     * @param remote remoteFile*/
    public static void setRemote(TreeMap<String, File>  remote) {
        Utils.writeObject(REMOTE_FILE, remote);
    }

    /** remove remote.
     * @param remoteName remote name
     * @param remoteDir remoteDirectory name*/
    public static void addRemote(String remoteName, String remoteDir) {
        remotes = getRemote();

        if (remotes.containsKey(remoteName)) {
            System.out.println("A remote with that name already exists.");
            return;
        }

        File remoteFile = new File(
                remoteDir.replace("/", java.io.File.separator));
        remotes.put(remoteName, remoteFile);
        setRemote(remotes);
    }

    /** retrieve remote branches.
     * @param remoteName remote name
     * @return object read by branches*/
    public static TreeMap<String, String> getRemoteBranches(File remoteName) {
        return Utils.readObject(
                Utils.join(remoteName, "/branches"), TreeMap.class);
    }

    /** remove remote.
     * @param remoteName remote name*/
    public static void rmRemote(String remoteName) {
        remotes = getRemote();

        if (!remotes.containsKey(remoteName)) {
            System.out.println("A remote with that name does not exist.");
            return;
        }

        remotes.remove(remoteName);
        setRemote(remotes);
    }

    /** push method.
     * @param remoteBranchName remote branch name
     * @param remoteName remote name*/
    public static void push(String remoteName, String remoteBranchName) {
        head = getHeadCommit();
        remotes = getRemote();

        if (!remotes.containsKey(remoteName)
                || !remotes.get(remoteName).exists()) {
            System.out.println("Remote directory not found.");
            return;
        }

        File dir = remotes.get(remoteName);
        remoteBranches = getRemoteBranches(dir);
        String remoteBranch = remoteBranches.get(remoteBranchName);

        TreeMap<String, Commit> tempCommitHist =
                (TreeMap<String, Commit>) getCommitHistory().clone();
        if (!tempCommitHist.containsKey(remoteBranch)) {
            System.out.println(
                    "Please pull down remote changes before pushing.");
            return;
        }
    }



    /** fetch method.
     * @param branchName branch to fetch
     * @param remoteName remote fetch*/
    public static void fetch(String remoteName, String branchName) {
        head = getHeadCommit();
        remotes = getRemote();

        if (!remotes.containsKey(remoteName)
                || !remotes.get(remoteName).exists()) {
            System.out.println("Remote directory not found.");
            return;
        }

        File dir = remotes.get(remoteName);
        remoteBranches = getRemoteBranches(dir);
        if (!remoteBranches.containsKey(branchName)) {
            System.out.println("That remote does not have that branch.");
            return;
        }
    }


}
