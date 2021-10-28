// This file contains definitions for an OPTIONAL part of your project.  If you
// choose not to do the optional point, you can delete this file from your
// project.

// This file contains a SUGGESTION for the structure of your program.  You
// may change any of it, or add additional files to this directory (package),
// as long as you conform to the project specification.

// Comments that start with "//" are intended to be removed from your
// solutions.
package jump61;

import java.util.ArrayList;
import java.util.Random;

import static jump61.Side.*;

/** An automated Player.
 *  @author P. N. Hilfinger
 */
class AI extends Player {

    /** A new player of GAME initially COLOR that chooses moves automatically.
     *  SEED provides a random-number seed used for choosing moves.
     */
    AI(Game game, Side color, long seed) {
        super(game, color);
        _random = new Random(seed);
    }

    @Override
    String getMove() {
        Board board = getGame().getBoard();

        assert getSide() == board.whoseMove();
        int choice = searchForMove();
        getGame().reportMove(board.row(choice), board.col(choice));
        return String.format("%d %d", board.row(choice), board.col(choice));
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private int searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert getSide() == work.whoseMove();
        _foundMove = -1;

        for (int i = 0; i < work.size() * work.size() ; i ++) {
            if (work.isLegal(getSide(), i)) {
                Board newBoard = new Board(work);
                newBoard.addSpot(getSide(), newBoard.row(i), newBoard.col(i));

                if (newBoard.getWinner() == getSide()) {
                    _foundMove = i;
                    return _foundMove;
                }
            }
        }

        if (getSide() == RED) {
            minMax(work, 4, true, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else {  //is BLUE
            minMax(work, 4, true, -1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }


//
//        for (int i = 0; i < work.size() * work.size(); i ++) {
//            if (work.isLegal(getSide(), i)) {
//                _foundMove = i;
//            }
//        }
        return _foundMove;
    }

//
//    private ArrayList<Integer> validMoves(Side s, Board b) {
//        ArrayList <Integer> validSpots = new ArrayList<>();
//        for (int i = 0; i < b.size() * b.size(); i ++) {
//                if (b.get(i).getSide().equals(s) || b.get(i).getSide() == (WHITE)) {
//                    if (b.isLegal(s, i)) {
//                    validSpots.add(i);
//                }
//            }
//        }
//        return validSpots;
//    }

//    private ArrayList<Integer> validMovesBlue(Board b) {
//        Board work = new Board(getBoard());
//        ArrayList <Integer> validSpots = new ArrayList<>();
//        for (int i = 0; i < work.size() * work.size(); i ++) {
//            if (work.get(i).getSide().equals(BLUE) || work.get(i).getSide() == (WHITE)) {
//                validSpots.add(i);
//            }
//        }
//        return validSpots;
//    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int minMax(Board board, int depth, boolean saveMove,
                       int sense, int alpha, int beta) {

        if (sense == 1) { //maximizing player turn - RED's turn
            return maxPlayerValue(board, depth, saveMove, alpha, beta);
        }

        if (sense == -1) { //minimizing
            return minPlayerValue(board, depth, saveMove, alpha, beta);

        }

        return 0;
        //RED is the maximizer
        //BLUE is the minimizer
    }

    private int maxPlayerValue(Board board,  int depth, boolean saveMove, int alpha, int beta)
    {
        if (board.getWinner() != null || depth == 0) {
            return staticEval(board, Integer.MAX_VALUE);
        }
        int bestSoFar = Integer.MIN_VALUE;
        int bestElement = -1;
        for (int element = 0; element < board.size() * board.size(); element++) { //recurse through children of the position
            if (board.isLegal(RED, element)) {
                Board newBoard = new Board(board);
                newBoard.addSpot(RED, newBoard.row(element), newBoard.col(element));
                int response = minPlayerValue(newBoard, depth - 1, false, alpha, beta);
                if (response >= bestSoFar) {
                    bestSoFar = response;
                    bestElement = element;
                    alpha = Math.max(bestSoFar, alpha);
                    if (alpha >= beta) {
                        if (saveMove == true) {
                            _foundMove = element;
                        }
                        return bestSoFar;
                    }

                }
//                if (beta <= alpha || alpha == Integer.MAX_VALUE) {
//                    break;
//                }

            }
        }
        if (saveMove) {
            _foundMove = bestElement;
        }
        return bestSoFar;
    }

    private int minPlayerValue(Board board, int depth, boolean saveMove, int alpha, int beta) {
        if (board.getWinner() != null || depth == 0) {
            return staticEval(board, Integer.MAX_VALUE);
        }

        int bestSoFar = Integer.MAX_VALUE;
        int bestElement = -1;

        for (int element = 0; element < board.size() * board.size(); element ++) {

            if (board.isLegal(BLUE, element)) {
                Board newBoard = new Board(board);
                newBoard.addSpot(BLUE, newBoard.row(element), newBoard.col(element));

                int response = maxPlayerValue(newBoard, depth - 1, false, alpha, beta);

                if (response <= bestSoFar) {
                    bestSoFar = response;
                    bestElement = element;
                    beta = Math.min(beta, bestSoFar);
                    if (alpha >= beta) {
                        if (saveMove) {
                            _foundMove = element;
                        }
                        return bestSoFar;
                    }

                }
//                if (beta <= alpha || alpha == Integer.MIN_VALUE) {
//                    break;
//                }

            }
        }
        if (saveMove) {
            _foundMove = bestElement;
        }
        return bestSoFar;
    }

    /** Return a heuristic estimate of the value of board position B.
     *  Use WINNINGVALUE to indicate a win for Red and -WINNINGVALUE to
     *  indicate a win for Blue. */
    private int staticEval(Board b, int winningValue) {
        int test = b.numOfSide(RED) - b.numOfSide(BLUE);
        if (b.getWinner() == RED) {
            return winningValue;
        }
        if (b.getWinner() == BLUE) {
            return -winningValue;
        }
        return test; // FIXME
    }

    /** A random-number generator used for move selection. */
    private Random _random;

    /** Used to convey moves discovered by minMax. */
    private int _foundMove;
}

//first step, let the AI make random VALID moves
// figure out some easy version of board goodness
