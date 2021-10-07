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
        //gotta get settings line
//        while (input.has)
        //make a scanner of settings
        //call next line from input
        //might want to check that it's settings line
        String settingsLine = _input.nextLine();
        //check to see the length of the input is > 0
        if (settingsLine.charAt(0) == '*'){
            setUp(M, settingsLine);
        }
        //should not convert settings line
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
                //                String [] notSetting = nextLine.split(" ");
//                String settingString = "";
//;               for (String word: notSetting){
//                    settingString += word;
//                }
                //get rid of white spaces and convert into one string
                //printmessage line the converted version
                ///
//                printMessageLine(M.convert(_input.nextLine()));
            }

        }


        //first call readconfig and turn it into a machine
        //then build the machine call setup
        //think of output as a terminal print



        //gonna call set up machine
        // call readconfig
        //

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    //returns a machine
    private Machine readConfig() {
        try {
            // FIXME
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
    //
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

            while(_config.hasNext("\\(.+\\)")){
                cycles += _config.next();
            }
            Permutation perm = new Permutation(cycles, _alphabet);

            if (type.equals("M")){
                return new MovingRotor(name, perm, notches);//at moving rotor
            }

            else if (type.equals("R")){
                return new Reflector(name, perm);
            }

//            if (type.equals("N")){
                return new FixedRotor(name, perm);
//            }


//
//            return null; // FIXME
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        int numRotors = M.numRotors();
        String[] _newRotors = new String[numRotors];
        String _settings[] = settings.substring(2).split(" "); //do we want reflector???
        int i = 0;

        String plugs = "";
        String setting = "";

        ArrayList <Rotor> selectedRotors = M.get_selectedRotors();

        for (Rotor r: selectedRotors){
            hashRotor.put(r.name(), r);
        }

        for (String element : _settings){
            if (!hashRotor.containsKey(element)){
                if (element.charAt(0) != '('){
                    setting = element;
                }
                else{
                    plugs += element + " ";
                }
            }
            else{
                _newRotors[i] = element;
                i ++;
            }
        }

        M.insertRotors(_newRotors);
        M.setRotors(setting);
        M.setPlugboard(new Permutation(plugs, _alphabet));


        // FIXME
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 5){
            if (i + 5 > msg.length()){
                _output.print(msg.substring(i,msg.length()) + "\n");
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
