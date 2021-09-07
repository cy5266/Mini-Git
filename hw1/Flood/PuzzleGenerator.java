import java.util.Random;

/** A creator of random Flood puzzles.
 *  @author P. N. Hilfinger
 */
class PuzzleGenerator {

    /** A new PuzzleGenerator whose random-number source is seeded
     *  with SEED. */
    PuzzleGenerator(long seed) {
        _random = new Random(seed);
    }

    public Model getPuzzle(int width, int height, int ncolors, int extra) {
        int[][] puzzle = new int[width][height];
        for (int[] row : puzzle) {
            for (int col = 0; col < height; col += 1) {
                row[col] = _random.nextInt(9);
            }
        }
        Model model = new Model(puzzle, ncolors);
        /* For hw1, assume that each Flood puzzle has a limit of 100 moves.
        * This will have to be changed to a "smarter" estimate in the
        * project */
        model.setLimit(100 + extra);
        return model;
    }

    public void setSeed(long seed) {
        _random.setSeed(seed);
    }

    /** My PRNG. */
    private Random _random;

}
