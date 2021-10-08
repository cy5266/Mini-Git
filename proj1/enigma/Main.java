package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Cindy Yang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine M = readConfig();

        while (_input.hasNextLine()){
            String nextLine = _input.nextLine();
            if (nextLine.length() == 0){
                printMessageLine(" ");
            }
            else if (nextLine.charAt(0) == '*' && nextLine.length() > 1){
                setUp(M, nextLine);
            }
            else{

                String settingString = nextLine.replaceAll(" ", "");
                printMessageLine(M.convert(settingString));

            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alphabet = _config.next();
            _alphabet= new Alphabet(alphabet);
            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();
            ArrayList<Rotor> _allRotors = new ArrayList<>();

            while (_config.hasNext()){
                _allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, numPawls, _allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String typeandnotches = _config.next();
            String type = typeandnotches.substring(0,1);
            String notches = "";

            if (type.equals("M")) {
                notches = typeandnotches.substring(1);
            }

            String cycles = "";

            HashMap<String, Boolean> contains = new HashMap<>();

            while(_config.hasNext("\\(.+\\)")){
                cycles += _config.next();
            }
            Permutation perm = new Permutation(cycles, _alphabet);

            if (contains.containsKey(type)){
                throw error("duplicate rotor");
            }
            else{
                contains.put(type, true);
            }
            if (type.equals("M")){
                return new MovingRotor(name, perm, notches);//at moving rotor
            }

            else if (type.equals("R")){
                return new Reflector(name, perm);
            }

//            else if (type.equals("N")){
                return new FixedRotor(name, perm);
//            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        int numRotors = M.numRotors();
        String[] _newRotors = new String[numRotors];
        int i = 0;

        String plugs = "";
        String setting = "";

        if (settings.charAt(0) != '*'){
            throw new EnigmaException("not a valid setting");
        }
        Scanner testScan = new Scanner(settings);
        if (testScan.hasNext()) {
            testScan.next();
        }

        while (testScan.hasNext()){
            String next = testScan.next();

            for (Rotor r: M.getallRotors()){
                if (r.name().equals(next)){
                    _newRotors[i] = next;
                    i ++;
                }
                else if (next.charAt(0) != '(')
                {
                    setting = next;
                }
                else if (next.charAt(0) == '(')
                {
                    plugs += next + " ";
                }
                else{
                    throw new EnigmaException("not a rotor, plug, or setting");
                }
            }

        }

        M.insertRotors(_newRotors);
        M.setRotors(setting);
        M.setPlugboard(new Permutation(plugs, _alphabet));

    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 5){
            if (i + 5 > msg.length() || i + 5 == msg.length()){
                _output.println(msg.substring(i,msg.length()));
            }
            else{
                _output.print(msg.substring(i, i + 5) + " ");
            }
        }
        // FIXME
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    private HashMap<String, Rotor> hashRotor =  new HashMap<>();
}
