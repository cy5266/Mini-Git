package gitlet;

//

import org.antlr.v4.runtime.tree.Tree;

import javax.crypto.spec.PSource;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.nio.charset.StandardCharsets;

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

    static final File REMOTE_FILE = Utils.join(GITLET_FOLDER, "/remote");

    public static Commit HEAD;
   // private static String MASTER =  "master";

    private static StagingArea stage;

   // private static String currentBranch = "";

    private static TreeMap<String, String> branchesHash = new TreeMap<>();

    private static TreeMap<String, Commit> commitHistory = new TreeMap<>();

    public static TreeMap<String, File> remotes = new TreeMap<>();

    public static TreeMap<String, String> remoteBranches = new TreeMap();

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

            Utils.writeObject(REMOTE_FILE, new TreeMap<String, File>());

        }
        else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }
    }

    public static void add(String fileName) {

        File toAdd = Utils.join(CWD, fileName);

        HEAD = getHeadCommit();
        stage = getStage();

        if (toAdd.exists()) {
            byte[] currentFile = HEAD.getBlobs().get(fileName);
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
        if (!HEAD.getBlobs().containsKey(fileName)) {
            System.out.println("No reason to remove the file.");
            return;
        }

        byte[] blobsToDelete = HEAD.getBlobs().get(fileName);
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



//    public static void setRemoteBranches (TreeMap<String, String>  remote)  {
//        Utils.writeObject(remoteBranches, remote);
//    }

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
            REMOTE_FILE.createNewFile();
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

        if (HEAD.getBlobs() != null) {
            tempClone.putAll(HEAD.getBlobs());
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
        byte[] currentBlobContents = HEAD.getBlobs().get(fileName);

        if (!HEAD.getBlobs().containsKey(fileName)) {
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

        if (commitID.length() < 40) {
            for (String fullCommitID: Utils.plainFilenamesIn(COMMIT_FOLDER)) {
                if (fullCommitID.contains(commitID)) {
                    commitID = fullCommitID;
                }
            }
        }

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
                if (!newCommit.getBlobs().containsKey(fileName)) {
                    System.out.println("File does not exist in that commit.");
                    return;
                }
//                String blobSHA = Utils.sha1(Utils.serialize(newCommit.get_Blobs().get(fileName)));
                Utils.writeContents(Utils.join(CWD, fileName), newCommit.getBlobs().get(fileName));
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
            if (!HEAD.getBlobs().containsKey(fileName)) {
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

        for (String fileName: requestCommit.getBlobs().keySet()) {
            try {
                Utils.join(CWD, fileName).createNewFile();
            } catch (IOException exc) {
                return;
            }
            Utils.writeContents(Utils.join(CWD, fileName), requestCommit.getBlobs().get(fileName));
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
            System.out.println("Date: " + tempHead.getTime());
            System.out.println(tempHead.getMessage() + "\n");

            tempHead = tempCommitHist.get(tempHead.getParentSHA());
        }

        System.out.println("===");
        System.out.println("commit " + Utils.sha1(Utils.serialize(tempHead)));
        System.out.println("Date: " + tempHead.getTime());
        System.out.println(tempHead.getMessage() + "\n");

    }

    static void globalLog() {

//        System.out.println("commit-folder" + Utils.plainFilenamesIn(COMMIT_FOLDER));
        for (String commitSHA: Utils.plainFilenamesIn(COMMIT_FOLDER)) {
            Commit commit = Utils.readObject(Utils.join(COMMIT_FOLDER, commitSHA), Commit.class);
            System.out.println("===");
            System.out.println("commit " + commitSHA);
            System.out.println("Date: " + commit.getTime());
            System.out.println(commit.getMessage() + "\n");
        }
    }

    static void find(String message) {

//        System.out.println("commit-folder" + Utils.plainFilenamesIn(COMMIT_FOLDER));
        boolean hasMessage = false;
        for (String commitSHA: Utils.plainFilenamesIn(COMMIT_FOLDER)) {
            Commit commit = Utils.readObject(Utils.join(COMMIT_FOLDER, commitSHA), Commit.class);
            if (commit.getMessage().equals(message)) {
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
            if (commit.getBlobs().containsKey(file) && !HEAD.getBlobs().containsKey(file)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
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

            Utils.writeContents(Utils.join(CWD, file), commit.getBlobs().get(file));
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

       TreeMap<String, byte[]> headBlobs = HEAD.getBlobs();


        System.out.println("=== Modifications Not Staged For Commit ===");
            for (String fileName : headBlobs.keySet()) {

                File blobFile = Utils.join(CWD, fileName);
                if ((!blobFile.exists() && !stage.get_stageRemoval().containsKey(fileName)) ||
                        !blobFile.exists() && stage.get_stageAddition().containsKey(fileName)) {
                    System.out.println(fileName + " (deleted)");
                    break;
                }

                String headBlob = Utils.sha1(HEAD.getBlobs().get(fileName));
                String string = "";

                if (blobFile.isFile()) {
                    string = Utils.sha1(Utils.readContents(blobFile));
                }

                if ((!stage.get_stageRemoval().containsKey(fileName)
                        && !string.equals(headBlob)
                        && !stage.get_stageAddition().containsKey(fileName))
                        ||
                        (stage.get_stageAddition().containsKey(fileName) &&
                                !headBlob.equals(Utils.sha1(Utils.readContents(blobFile))))) {
                    System.out.println(fileName + " (modified)");
                }
            }

        System.out.println();
        System.out.println("=== Untracked Files ===");
            for (String fileName : Utils.plainFilenamesIn(CWD)) {
                if (!headBlobs.containsKey(fileName)
                        && !stage.get_stageAddition().containsKey(fileName)) {
                    System.out.println(fileName);
                }
            }

    }

    public static void merge(String mergeBranch) {
        HEAD = getHeadCommit();
        branchesHash = getBranches();
        String currentBranchName = getCurrentBranchName();
        stage = getStage();

        if (!stage.get_stageRemoval().isEmpty() || !stage.get_stageAddition().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if (branchesHash.get(currentBranchName).equals(branchesHash.get(mergeBranch))) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        if (!branchesHash.containsKey(mergeBranch)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        Commit mergeBranchCommit = Utils.readObject
                (Utils.join(COMMIT_FOLDER, branchesHash.get(mergeBranch)), Commit.class);

        for (String filename : Utils.plainFilenamesIn(CWD)) {
            if (!HEAD.getBlobs().containsKey(filename) &&
                    mergeBranchCommit.getBlobs().containsKey(filename)) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                return;
            }
        }

        Commit splitCommit = split(mergeBranch);

        if (Utils.sha1(Utils.serialize(splitCommit)).equals(branchesHash.get(currentBranchName))) {
            checkout3(mergeBranch);
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        if (Utils.sha1(Utils.serialize(splitCommit)).equals(branchesHash.get(mergeBranch))) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }


        ArrayList<String> fileNames = new ArrayList<>();
        fileNames = getAllFileNames(splitCommit, HEAD, mergeBranchCommit);

//        System.out.println(fileNames);

        int action;
        boolean hasConflict = false;

        for (String file: fileNames) {
            byte[] headFile = HEAD.getBlobs().get(file);
            byte[] splitFile = splitCommit.getBlobs().get(file);
            byte[] mergeFile = mergeBranchCommit.getBlobs().get(file);

//            System.out.println(headFile);
//            System.out.println(splitFile);
//            System.out.println(mergeFile);

            action = mergeCases(headFile, splitFile, mergeFile);

//            System.out.println(action);

            if (action == 1) {
                File dir = Utils.join(CWD, file);
                Utils.writeContents(dir, mergeFile);

                stage.get_stageAddition().put(file, mergeFile);

            } else if (action == 2) {
                stage.get_stageRemoval().put(file, headFile);
                Utils.restrictedDelete(Utils.join(CWD, file));

            } else if (action == 3) {
//                System.out.println("hello");
//                System.out.println(headFile);

                String combinedContents = combined(headFile, mergeFile);
//                String combinedContents2 = combined(Utils.readContents(Utils.join(CWD, headFile), mergeFile);
//                File dir = Utils.join(CWD, file);

                File stagingFile = Utils.join(CWD, file);
                Utils.writeContents(stagingFile, combinedContents);
                stage.get_stageAddition().put(file, Utils.readContents(stagingFile));

//                stage.get_stageAddition().put(file, newBlob.getID());
//
//                File workingFile = Utils.join(CWD, combinedContents);
//                Utils.writeContents(workingFile, Utils.readContents(dir));

//                Utils.writeContents(dir, contents);
//                stage.get_stageAddition().put(file, Utils.readContents(dir));

                hasConflict = true;
            }
            setStage(stage);

        }

        if (hasConflict) {
            System.out.println("Encountered a merge conflict.");
//            return;
        }

        mergeCommit(("Merged " + mergeBranch + " into " + currentBranchName + "." ), mergeBranchCommit);
        setStage(stage);
        //get split point
        //get all the files
        //go through the different cases
    }

    public static String combined(byte[] head, byte[] merged) {
        String result;
        if (merged == null) {
            result = "<<<<<<< HEAD\n" + new String(head, StandardCharsets.UTF_8)  + "=======\n>>>>>>>\n";
        } else {
            result = "<<<<<<< HEAD\n" + new String(head, StandardCharsets.UTF_8) + "=======\n"
                    + new String(merged, StandardCharsets.UTF_8) + ">>>>>>>\n";
        }
        return result;
    }


    public static void mergeCommit(String message, Commit mergedBranch) {
        HEAD = getHeadCommit();
        branchesHash = getBranches();
        String currentBranch = getCurrentBranchName();
        stage = getStage();
        commitHistory = getCommitHistory();

        TreeMap<String, byte[]> tempClone = new TreeMap<>();

        if (HEAD.getBlobs() != null) {
            tempClone.putAll(HEAD.getBlobs());
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


//        String currentBranch = getCurrentBranchName();

        branchesHash = getBranches();
        branchesHash.put(currentBranch, newCommitSHA1);

        setBranches(branchesHash);

//        Utils.writeObject(CURRENT_BRANCH_FILE, currentBranch);


        Utils.writeObject(newFile, commitClone);
        commitHistory.put(newCommitSHA1, commitClone);
        setCommitHistory(commitHistory);

//        Commit requestedCommit = new Commit(message, tempClone, Utils.sha1(Utils.serialize(HEAD)));


    }


    public static int mergeCases(byte[] headFile, byte[] splitFile, byte[] mergeFile) {

        String splitSHA = Utils.sha1(Utils.serialize(splitFile));
        String headSHA = Utils.sha1(Utils.serialize(headFile));
        String mergeSHA = Utils.sha1(Utils.serialize(mergeFile));

        if (splitFile != null && headFile != null && splitSHA.equals(headSHA) && mergeFile != null
        && !headSHA.equals(mergeSHA)) {
            return 1;
        }

        if (splitFile != null && headFile != null && !splitSHA.equals(headSHA) && mergeFile != null
                && !headSHA.equals(mergeSHA) && !mergeSHA.equals(splitSHA)) {
            return 3;
        }

        if (splitFile != null && headFile != null && splitSHA.equals(headSHA) && mergeFile == null) {
            return 2;
        }

        if (splitFile == null && headFile == null && mergeFile != null) {
            return 1;
        }



        // Case 9: if all three files r different and merged is missing
        // result is combine head and merge and action is conflict
        if (splitFile != null && headFile != null && !splitSHA.equals(headSHA) && mergeFile == null) {
            return 3;
        }


        return 0;
    }

    public static ArrayList getAllFileNames(Commit splitHeadPointer, Commit HeadPointer, Commit MergePointer) {
        ArrayList<String> fileNames = new ArrayList<>(splitHeadPointer.getBlobs().keySet());
//        System.out.println(fileNames);

        for (String key: HeadPointer.getBlobs().keySet()) {
            if (!fileNames.contains(key)) {
                fileNames.add(key);
            }
        }

        for (String key: MergePointer.getBlobs().keySet()) {
            if (!fileNames.contains(key)) {
                fileNames.add(key);
            }
        }
//        System.out.println(fileNames);

        return fileNames;
    }

    public static Commit split(String branch) {
        branchesHash = getBranches();
        ArrayList<String> headHistory = new ArrayList<>();

        Commit tempHead = getHeadCommit();
        TreeMap<String, Commit> tempCommitHist = (TreeMap<String, Commit>) getCommitHistory().clone();

        while (tempHead.getParentSHA() != null) {
            headHistory.add(Utils.sha1(Utils.serialize(tempHead)));
            tempHead = tempCommitHist.get(tempHead.getParentSHA());
        }

        headHistory.add(Utils.sha1(Utils.serialize(tempHead)));

//        System.out.println(headHistory);

        String branchSHA1 = branchesHash.get(branch); //returns the SHA of the HEAD of that branch

        Commit branchCommit = Utils.readObject(Utils.join(COMMIT_FOLDER, branchSHA1), Commit.class);
        // reads in the commit of that head

        String splitSHA = null;

        while (branchCommit.getParentSHA() != null) {
//            System.out.println("branch commit: " + Utils.sha1(Utils.serialize(branchCommit)));
            if (headHistory.contains(Utils.sha1(Utils.serialize(branchCommit)))) {
//                System.out.println("branch commit: " + Utils.sha1(Utils.serialize(branchCommit)));
                splitSHA = Utils.sha1(Utils.serialize(branchCommit));
                break;
            }

            branchCommit = tempCommitHist.get(branchCommit.getParentSHA());

        }

//        System.out.println("split sha " + splitSHA);

        return Utils.readObject(Utils.join(COMMIT_FOLDER, splitSHA), Commit.class);
    }


    public static TreeMap<String, File> getRemote() {
        return Utils.readObject(REMOTE_FILE, TreeMap.class);
    }

    public static void setRemote(TreeMap<String, File>  remote) {
        Utils.writeObject(REMOTE_FILE, remote);
    }


    public static void addRemote (String remoteName, String remoteDir) {
        remotes = getRemote();

        if (remotes.containsKey(remoteName)) {
            System.out.println("A remote with that name already exists.");
            return;
        }

        File remoteFile = new File(remoteDir.replace("/", java.io.File.separator));
        remotes.put(remoteName, remoteFile);
        setRemote(remotes);
    }

//
//    public static void setRemoteBranches(File remote, TreeMap<String, String> temp) {
//        Utils.writeObject(Utils.join(remote, "/branches"), temp);
//    }

    public static TreeMap<String, String> getRemoteBranches(File remoteName) {
        return Utils.readObject(Utils.join(remoteName, "/branches"), TreeMap.class);
    }

    public static void rmRemote (String remoteName) {
        remotes = getRemote();

        if (!remotes.containsKey(remoteName)) {
            System.out.println("A remote with that name does not exist.");
            return;
        }

        remotes.remove(remoteName);
        setRemote(remotes);
    }

    public static void push (String remoteName, String remoteBranchName) {
        HEAD=getHeadCommit();
        remotes=getRemote();

        if (!remotes.containsKey(remoteName) || !remotes.get(remoteName).exists()) {
            System.out.println("Remote directory not found.");
            return;
        }

        File dir = remotes.get(remoteName);
        remoteBranches = getRemoteBranches(dir);
        String remoteBranch = remoteBranches.get(remoteBranchName);

        TreeMap<String, Commit> tempCommitHist = (TreeMap<String, Commit>) getCommitHistory().clone();
        if (!tempCommitHist.containsKey(remoteBranch)) {
            System.out.println("Please pull down remote changes before pushing.");
            return;
        }
    }




    public static void fetch (String remoteName, String branchName) {
        HEAD=getHeadCommit();
        remotes=getRemote();

        if (!remotes.containsKey(remoteName) || !remotes.get(remoteName).exists()) {
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

    public static void pull(String remoteName, String remoteBranchName) {
        fetch(remoteName, remoteBranchName);
//        merge(remoteName + "/" + remoteBranchName);
    }


}
