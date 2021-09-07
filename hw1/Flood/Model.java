/** A simplified version of the Model class from the Flood project.
 *  Represents the state of a flood puzzle. The cells have 0-based column and row numbers,
 *  and each contains a non-negative number indicating a color. This simplified
 *  version contains only basic functionality such as constructing a Model instance from
 *  a 2D array, and getting information such as the color at a particular cell
 *  or the move limit for the Flood puzzle.
 *
 *  Again, this is a much simplified version, intended to familiarize students
 *  with the strucutre of the Model class in the Flood project.
 *
 *  @author Henry Maier
 */
class Model {

    /** Limit on number of colors that may be used in a puzzle. */
    static final int MAX_COLORS = 10;

    /** A Model whose initial state is given by INITIAL with NCOLORS>2 colors.
     *  INITIAL[r][c] is the color row r and column c.
     *  Does not modify INITIAL, nor does the resulting Model share structure
     *  with it.
     */
    Model(int[][] initial, int ncolors) {
        _height = initial.length;
        _width = initial[0].length;
        _ncolors = ncolors;
        _limit = 0;
        _cells = new int[_height][_width];
        Utils.deepCopy(initial, _cells);
    }

    /** Returns the width (number of columns of cells) of the board. */
    final int width() {
        return _width;
    }

    /** Returns the height (number of rows of cells) of the board. */
    final int height() {
        return _height;
    }

    /** Returns number of colors in puzzle. */
    final int ncolors() {
        return _ncolors;
    }

    /** Return the current move limit. Initially 0 until changed by setLimit. */
    int limit() {
        return _limit;
    }

    /** Set the move limit to LIMIT. */
    void setLimit(int limit) {
        _limit = limit;
    }

    /** Returns true iff (ROW, COL) is a valid cell location. */
    final boolean isCell(int row, int col) {
        return 0 <= col && col < width() && 0 <= row && row < height();
    }

    /** Returns the contents of location (ROW, COL). */
    final int get(int row, int col) {
        return _cells[row][col];
    }

    /** Dimensions of board. */
    private int _width, _height;

    /** Number of colors. */
    private int _ncolors;

    /** The current board contents. */
    private int[][] _cells;

    /** Move limit. */
    private int _limit;

}


