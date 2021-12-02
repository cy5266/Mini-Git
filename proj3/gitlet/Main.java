package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        // FILL THIS IN
        switch (args[0]) {
            case "init":
                Repo.init();
                break;
            case "add":
                Repo.add(args[1]);
                break;
            case "commit":
                Repo.commit(args[1]);
                break;
            case "log":
                Repo.log();
                break;
            case "checkout":
                if (args.length == 3) {
                    Repo.checkout(args[2]);
                    break;
                } else if (args.length == 4) {
                    Repo.checkout2(args[1], args[3]);
                    break;
                } else if (args.length == 2) {
                    Repo.checkout3(args[1]);
                    break;
                }
            case "rm":
                Repo.rm(args[1]);
                break;
            case "global-log":
                Repo.globalLog();
                break;
            case "find":
                Repo.find(args[1]);
                break;
            case "branch":
                Repo.branch(args[1]);
                break;
            case "rm-branch":
                Repo.rmBranch(args[1]);
                break;
            case "reset":
                Repo.reset(args[1]);
                break;
            case "status":
                Repo.status();
                break;
        }
        //error for bad command
        return;
    }

}
