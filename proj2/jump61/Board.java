// This file contains a SUGGESTION for the structure of your program.  You
// may change any of it, or add additional files to this directory (package),
// as long as you conform to the project specification.

// Comments that start with "//" are intended to be removed from your
// solutions.
package jump61;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.Formatter;

import java.util.function.Consumer;

import static jump61.Side.*;
import static jump61.Square.square;

/** Represents the state of a Jump61 game.  Squares are indexed either by
 *  row and column (between 1 and size()), or by square number, numbering
 *  squares by rows, with squares in row 1 numbered from 0 to size()-1, in
 *  row 2 numbered from size() to 2*size() - 1, etc. (i.e., row-major order).
 *
 *  A Board may be given a notifier---a Consumer<Board> whose
 *  .accept method is called whenever the Board's contents are changed.
 *
 *  @author Cindy Yang
 */
class Board {

    /** An uninitialized Board.  Only for use by subtypes. */
    protected Board() {
        _notifier = NOP;
    }

    /** An N x N board in initial configuration. */
    Board(int N) {
        this();
        _myboard = new ArrayList<Square>(N*N);

        for (int i = 0; i < N*N; i ++) {
            _myboard.add(i, square(WHITE, 1));
        }
        boardHistory = new ArrayDeque<>();
        _jumping = false;
        // FIXME
    }

    /** A board whose initial contents are copied from BOARD0, but whose
     *  undo history is clear, and whose notifier does nothing. */
    Board(Board board0) {
        this(board0.size());
        // FIXME

        for (int i = 0; i < board0.size(); i ++) {
            _myboard.set(i, board0.get(i));
        }
        boardHistory.clear();
//        _boardHistory.clear();
        markUndo();
        _readonlyBoard = new ConstantBoard(this);
    }

    /** Returns a readonly version of this board. */
    Board readonlyBoard() {
        return _readonlyBoard;
    }

    /** (Re)initialize me to a cleared board with N squares on a side. Clears
     *  the undo history and sets the number of moves to 0. */
    void clear(int N) {
        // FIXME

        _myboard = new ArrayList<Square>(N*N);

        for (int i = 0; i < N * N; i ++) {
            _myboard.add(i, square(WHITE, 1));
        }
        boardHistory.clear();
//        _boardHistory.clear();
        markUndo();
        announce();
    }

    /** Copy the contents of BOARD into me. */
    void copy(Board board) {

        _myboard = new ArrayList<Square>();

        for (int i = 0; i < board.size() * board.size(); i ++) {
            _myboard.add(board.get(i));
        }
//        Board newB = new Board(1);
//        for (int i = 0; i < board.size() * board.size(); i ++) {
//            Square sq = square(board.get(i).getSide(), board.get(i).getSpots());
//            _myboard.set(i, sq);
//            newB.set(row(i), col(i), board.get(i));


        boardHistory.clear();

        //clear undo history, clear arraylist, set to new
    }

    /** Copy the contents of BOARD into me, without modifying my undo
     *  history. Assumes BOARD and I have the same size. */
    private void internalCopy(Board board) {
        assert size() == board.size();


        for (int i = 0; i < board.size() * board.size() ; i ++) {
            Square sq = square(board.get(i).getSide(), board.get(i).getSpots());
            _myboard.set(i, sq);
//            _copyBoard.set(i, board.get(i));
//            _myboard.add(sq);
        }
        // FIXME
    }

    /** Return the number of rows and of columns of THIS. */
    int size() {
        return (int) Math.sqrt(_myboard.size()); // FIXME
    }

    /** Returns the contents of the square at row R, column C
     *  1 <= R, C <= size (). */
    Square get(int r, int c) {
        return get(sqNum(r, c));
    }

    /** Returns the contents of square #N, numbering squares by rows, with
     *  squares in row 1 number 0 - size()-1, in row 2 numbered
     *  size() - 2*size() - 1, etc. */
    Square get(int n) {
        return _myboard.get(n); // FIXME
    }

    /** Returns the total number of spots on the board. */
    int numPieces() {
        int spots = 0;

        for (int i = 0; i < _myboard.size(); i ++) {
            spots += _myboard.get(i).getSpots();
        }
        return spots; // FIXME
    }

    /** Returns the Side of the player who would be next to move.  If the
     *  game is won, this will return the loser (assuming legal position). */
    Side whoseMove() {
        return ((numPieces() + size()) & 1) == 0 ? RED : BLUE;
    }

    /** Return true iff row R and column C denotes a valid square. */
    final boolean exists(int r, int c) {
        return 1 <= r && r <= size() && 1 <= c && c <= size();
    }

    /** Return true iff S is a valid square number. */
    final boolean exists(int s) {
        int N = size();
        return 0 <= s && s < N * N;
    }

    /** Return the row number for square #N. */
    final int row(int n) {
        return n / size() + 1;
    }

    /** Return the column number for square #N. */
    final int col(int n) {
        return n % size() + 1;
    }

    /** Return the square number of row R, column C. */
    final int sqNum(int r, int c) {
        return (c - 1) + (r - 1) * size();
    }

    /** Return a string denoting move (ROW, COL)N. */
    String moveString(int row, int col) {
        return String.format("%d %d", row, col);
    }

    /** Return a string denoting move N. */
    String moveString(int n) {
        return String.format("%d %d", row(n), col(n));
    }

    /** Returns true iff it would currently be legal for PLAYER to add a spot
        to square at row R, column C. */
    boolean isLegal(Side player, int r, int c) {
        return isLegal(player, sqNum(r, c));
    }

    /** Returns true iff it would currently be legal for PLAYER to add a spot
     *  to square #N. */
    boolean isLegal(Side player, int n) {
            return player.playableSquare(_myboard.get(n).getSide());
         // FIXME
    }

    /** Returns true iff PLAYER is allowed to move at this point. */
    boolean isLegal(Side player) {
        if (whoseMove().equals(player) && getWinner() != null){
            return true;
        }
        return false;

//        return null; // FIXME
    }

    /** Returns the winner of the current position, if the game is over,
     *  and otherwise null. */
    final Side getWinner() {

        int numRed = numOfSide(RED);
        int numBlue = numOfSide(BLUE);

        if (numRed != 0 && numRed == size()) {
            return RED;
        }
        else if (numBlue != 0 && numBlue == size()) {
            return BLUE;
        }
        else {
            return null;
        }
        // FIXME
    }

    /** Return the number of squares of given SIDE. */
    int numOfSide(Side side) {
        int numSquares = 0;
        for (Square sideSquare: _myboard) {
            if (sideSquare.getSide().equals(side)) {
                numSquares += 1;
            }
        }
        return numSquares; // FIXME
    }

    /** Add a spot from PLAYER at row R, column C.  Assumes
     *  isLegal(PLAYER, R, C). */
    void addSpot(Side player, int r, int c) {
//        _myboard.get()
        //sqaure num RC
//        markUndo();
        if (getWinner() == null) {
            addSpot(player, sqNum(r, c));
        }

        // FIXME
    }

    /** Add a spot from PLAYER at square #N.  Assumes isLegal(PLAYER, N). */
    void addSpot(Side player, int n) {
        if (getWinner() == null) {
            if (_jumping == false) {
                markUndo();
            }
//        _boardHistory.add(this);
            int newSquare = _myboard.get(n).getSpots() + 1;
            int r = row(n);
            int c = col(n);
            set(r, c, newSquare, player);
            if (getWinner() == null) {
                jump(n);
            }

        }
        // FIXME
    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white). */
    void set(int r, int c, int num, Side player) {
        internalSet(r, c, num, player);
        announce();
    }

    /** Set the square at row R, column C to NUM spots (0 <= NUM), and give
     *  it color PLAYER if NUM > 0 (otherwise, white).  Does not announce
     *  changes. */
    private void internalSet(int r, int c, int num, Side player) {
        internalSet(sqNum(r, c), num, player);
    }

    /** Set the square #N to NUM spots (0 <= NUM), and give it color PLAYER
     *  if NUM > 0 (otherwise, white). Does not announce changes. */
    private void internalSet(int n, int num, Side player) {
        if (num == 0) {
            _myboard.set(n, square(WHITE, 0));
        }
        else {
            _myboard.set(n, square(player, num));
        }
//        internalSet(row, col, num, player);
//        if (num > 0) {
//            set(row, col, num, player);
//        }
        // FIXME
    }

    // There are two obvious ways to conduct a game-tree search in the AI.
    //
    // First, you can explore the consequences of a possible move from
    // position A by making a copy of the Board in position A, and then
    // modifying that copy. Since you retain position A, you can return to
    // it to try other moves from that position.
    //
    // Second, you can explore the consequences of a possible move from
    // position A by making that move on your Board and then, when your
    // analysis of the move is complete, undoing the move to return you to
    // position A. This method is more complicated to implement, but has
    // the advantage that it can be considerably faster than making copies
    // of the Board (you will need one copy per move tried, which will very
    // quickly be thrown away).

    /** Undo the effects of one move (that is, one addSpot command).  One
     *  can only undo back to the last point at which the undo history
     *  was cleared, or the construction of this Board. */
    void undo() {

        this.internalCopy(boardHistory.pop());
//        _myboard = convert(boardHistory.pop());
//        internalCopy(_myboard);
//        int historyLastIndex = _boardHistory.size() -1;
//        _myboard = convert(_boardHistory.get(historyLastIndex));
//        _boardHistory.remove(historyLastIndex);

        // FIXME
    }

    private ArrayList<Square> convert(Board b) {
        ArrayList <Square> lastHistory = new ArrayList<>();
        for (int i = 0; i < b.size() * b.size(); i ++) {
            Square sq = square(b.get(i).getSide(), b.get(i).getSpots());
            lastHistory.add(sq);
        }
        return lastHistory;
                // loop through board and add the squares
    }

//    private Board internalCopy2(ArrayList board) {
//        Board
//    }
    /** Record the beginning of a move in the undo history. */
    private void markUndo() {
        // FIXME
//        Board copy = copy(_myboard);
//        Board boardCopy = internalCopy2(_myboard);
        Board b = new Board(1);
        b.copy(this);
        boardHistory.push(b);
    }

    /** Add DELTASPOTS spots of side PLAYER to row R, column C,
     *  updating counts of numbers of squares of each color. */
    private void simpleAdd(Side player, int r, int c, int deltaSpots) {
        internalSet(r, c, deltaSpots + get(r, c).getSpots(), player);
    }

    /** Add DELTASPOTS spots of color PLAYER to square #N,
     *  updating counts of numbers of squares of each color. */
    private void simpleAdd(Side player, int n, int deltaSpots) {
        internalSet(n, deltaSpots + get(n).getSpots(), player);
    }

    /** Used in jump to keep track of squares needing processing.  Allocated
     *  here to cut down on allocations. */
    private final ArrayDeque<Integer> _workQueue = new ArrayDeque<>();

    /** Do all jumping on this board, assuming that initially, S is the only
     *  square that might be over-full. */
    private void jump(int S) {
        // FIXME

        ArrayList<Integer> validSpots = neighborSpots(S);
        if (neighbors(S) < _myboard.get(S).getSpots() && getWinner() == null) {
            set(row(S), col(S), _myboard.get(S).getSpots() - neighbors(S), _myboard.get(S).getSide());
            Side player = get(S).getSide();
            for (int i : validSpots) {
                _jumping = true;
                if (getWinner() == null) {
                    addSpot(player, i);
                }
            }
        }

        _jumping = false;
        //went through ortohogal neighbors and check if valid, and then add spot to add of them
        // check if overfilled & winner is null
        // if it is call helper function

    }

    private ArrayList<Integer> neighborSpots(int S) {
        ArrayList <Integer> validIndex = new ArrayList<>();
        if (exists(row(S) - 1, col(S))) {
            validIndex.add(sqNum(row(S) - 1, col(S)));
        }
        if (exists(row(S) + 1, col(S))) {
            validIndex.add(sqNum(row(S) + 1, col(S)));
        }
        if (exists(row(S), col(S) - 1)) {
            validIndex.add(sqNum(row(S), col(S) - 1));
        }
        if (exists(row(S), col(S) + 1)) {
            validIndex.add(sqNum(row(S), col(S) + 1));
        }
        return validIndex;
    }

    /** Returns my dumped representation. */
    @Override
    public String toString() {

        Formatter out = new Formatter();

        out.format("===%n");
        for (int i = 1; i < size() + 1; i ++) {
            out.format("    ");
            for (int j = 1; j < size() + 1; j ++) {
                String player = "";

                if (get(i, j).getSide() == RED) {
                    player = "r";
                }
                else if (get(i, j).getSide() == BLUE) {
                    player = "b";
                }
                else if (get(i, j).getSide() == WHITE) {
                    player = "-";
                }

                out.format("%d%s ", get(i, j).getSpots(), player);
            }
            out.format("%n");
        }
        out.format("===%n");
        return out.toString();
        // FIXME
    }

    /** Returns an external rendition of me, suitable for human-readable
     *  textual display, with row and column numbers.  This is distinct
     *  from the dumped representation (returned by toString). */
    public String toDisplayString() {
        String[] lines = toString().trim().split("\\R");
        Formatter out = new Formatter();
        for (int i = 1; i + 1 < lines.length; i += 1) {
            out.format("%2d %s%n", i, lines[i].trim());
        }
        out.format("  ");
        for (int i = 1; i <= size(); i += 1) {
            out.format("%3d", i);
        }
        return out.toString();
    }

    /** Returns the number of neighbors of the square at row R, column C. */
    int neighbors(int r, int c) {
        int size = size();
        int n;
        n = 0;
        if (r > 1) {
            n += 1;
        }
        if (c > 1) {
            n += 1;
        }
        if (r < size) {
            n += 1;
        }
        if (c < size) {
            n += 1;
        }
        return n;
    }

    /** Returns the number of neighbors of square #N. */
    int neighbors(int n) {
        return neighbors(row(n), col(n));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Board)) {
            return false;
        } else {
            Board B = (Board) obj;
            return this == obj;  // FIXME
        }
    }

    @Override
    public int hashCode() {
        return numPieces();
    }

    /** Set my notifier to NOTIFY. */
    public void setNotifier(Consumer<Board> notify) {
        _notifier = notify;
        announce();
    }

    public Board getLastState() {
        return boardHistory.pop();
    }

    public void setLastState(Board b) {
        boardHistory.add(b);
    }

    /** Take any action that has been set for a change in my state. */
    private void announce() {
        _notifier.accept(this);
    }

    /** A notifier that does nothing. */
    private static final Consumer<Board> NOP = (s) -> { };

    /** A read-only version of this Board. */
    private ConstantBoard _readonlyBoard;

    /** Use _notifier.accept(B) to announce changes to this board. */
    private Consumer<Board> _notifier;

    private ArrayList<Square> _myboard;

    private ArrayDeque<Board> boardHistory;

    private boolean _jumping;
    // FIXME: other instance variables here.

}
