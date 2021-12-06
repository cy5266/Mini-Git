package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Cindy Yang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {

        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        switch (args[0]) {
        case "init":
            Repo.init(); break;
        case "add":
            checkGitRepo(); Repo.add(args[1]); break;
        case "commit":
            checkGitRepo(); Repo.commit(args[1]); break;
        case "log":
            checkGitRepo(); Repo.log(); break;
        case "checkout":
            checkGitRepo();
            if (args.length == 3) {
                Repo.checkout(args[2]); break;
            } else if (args.length == 4 && args[2].equals("--")) {
                Repo.checkout2(args[1], args[3]); break;
            } else if (args.length == 2) {
                Repo.checkout3(args[1]); break;
            } else {
                System.out.println("Incorrect operands."); break;
            }
        case "rm":
            checkGitRepo(); Repo.rm(args[1]); break;
        case "global-log":
            checkGitRepo(); Repo.globalLog(); break;
        case "find":
            checkGitRepo(); Repo.find(args[1]); break;
        case "branch":
            checkGitRepo(); Repo.branch(args[1]); break;
        case "rm-branch":
            checkGitRepo(); Repo.rmBranch(args[1]); break;
        case "reset":
            checkGitRepo(); Repo.reset(args[1]); break;
        case "status":
            checkGitRepo(); Repo.status(); break;
        case "merge":
            Repo.merge(args[1]); break;
        case "push":
            Repo.push(args[1], args[2]); break;
        case "add-remote":
            Repo.addRemote(args[1], args[2]); break;
        case "rm-remote":
            Repo.rmRemote(args[1]); break;
        case "fetch":
            Repo.fetch(args[1], args[2]);
            break;
        default:
            System.out.println("No command with that name exists.");
            break;
        }
        return;
    }

    /** Checking if Git repo exists. */
    public static void checkGitRepo() {
        if (!Repo.GITLET_FOLDER.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
    }



}
