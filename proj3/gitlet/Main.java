package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {

        switch (args[0]) {
            case "init":
                Repo.init();
                break;
        }
        //error for bad command
        return;
        // FILL THIS IN
    }

}
