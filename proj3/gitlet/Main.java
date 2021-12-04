package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        // FILL THIS IN

        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        switch (args[0]) {
            case "init":
                Repo.init();
                break;
            case "add":
                if (!Repo.GITLET_FOLDER.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repo.add(args[1]);
                break;
            case "commit":
                if (!Repo.GITLET_FOLDER.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repo.commit(args[1]);
                break;
            case "log":
                if (!Repo.GITLET_FOLDER.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repo.log();
                break;
            case "checkout":
                if (!Repo.GITLET_FOLDER.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                if (args.length == 3) {
                    Repo.checkout(args[2]);
                    break;
                } else if (args.length == 4 && args[2].equals("--")) {
                    Repo.checkout2(args[1], args[3]);
                    break;
                } else if (args.length == 2) {
                    Repo.checkout3(args[1]);
                    break;
                }
                else {
                    System.out.println("Incorrect operands.");
                    break;
                }
            case "rm":
                if (!Repo.GITLET_FOLDER.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repo.rm(args[1]);
                break;
            case "global-log":
                if (!Repo.GITLET_FOLDER.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repo.globalLog();
                break;
            case "find":
                if (!Repo.GITLET_FOLDER.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repo.find(args[1]);
                break;
            case "branch":
                if (!Repo.GITLET_FOLDER.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repo.branch(args[1]);
                break;
            case "rm-branch":
                if (!Repo.GITLET_FOLDER.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repo.rmBranch(args[1]);
                break;
            case "reset":
                if (!Repo.GITLET_FOLDER.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repo.reset(args[1]);
                break;
            case "status":
                if (!Repo.GITLET_FOLDER.exists()) {
                    System.out.println("Not in an initialized Gitlet directory.");
                    break;
                }
                Repo.status();
                break;
            default:
                System.out.println("No command with that name exists.");
                break;
        }
        //error for bad command
        return;
    }

}
