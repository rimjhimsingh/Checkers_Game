package a2.csd311.checkers;


import java.util.List;
import java.util.Map;


  //This class is responsible to search all possible board configuration for a given depth.
 
public class MiniMax {

    private Utility utility;
    private AI ai;
    private String[] bestMove;


    public MiniMax(Utility utility, AI ai) {
        this.utility = utility;
        this.ai = ai;
    }
    public int miniMax(Map<String, Piece> redPieces, Map<String, Piece> blackPieces, int depth, int max, int min, boolean human) {

        ai.recursionCounter++;

        // get possible moves
        utility.getPossibleMoves(redPieces, blackPieces, human);
        List<String[]> jumpMoves = utility.getJumpMoves();
        List<String[]> simpleMoves = utility.getSimpleMoves();

        // return the heuristic if depth == 0 or node is a terminal node
        if (depth == 0 || (simpleMoves.isEmpty() && jumpMoves.isEmpty())) {
            return heuristic(redPieces, blackPieces);
        }

        // force jump
        List<String[]> possibleMoves;
        possibleMoves = simpleMoves;
        if (!jumpMoves.isEmpty()) {
            possibleMoves = jumpMoves;
        }

        String[] tempBestMove = null;
        if (!human) {

            int score = Integer.MIN_VALUE;

            for (String[] move : possibleMoves) {

                // a deep copy of the HashMap is necessary for the simulation of the board configurations
                Map<String, Piece> copyRedPieces = utility.deepCopyMap(redPieces);
                Map<String, Piece> copyBlackPieces = utility.deepCopyMap(blackPieces);

                ai.makeMove(move, copyRedPieces, copyBlackPieces, false, false);
                score = miniMax(copyRedPieces, copyBlackPieces, depth - 1, max, min, true);

                // save the best score
                if (max <= score) {
                    max = score;
                    tempBestMove = move;
                }
                // apply pruning
                if (min <= max) {
                    break;
                }
            }
            bestMove = tempBestMove;
            return score;
        } else {
            int score = Integer.MAX_VALUE;
            for (String[] move : possibleMoves) {

                // a deep copy of the HashMap is necessary for the simulation of the board configurations
                Map<String, Piece> copyRedPieces = utility.deepCopyMap(redPieces);
                Map<String, Piece> copyBlackPieces = utility.deepCopyMap(blackPieces);

                ai.makeMove(move, copyRedPieces, copyBlackPieces, false, true);

                utility.getPossibleMoves(copyRedPieces, copyBlackPieces, true);
                jumpMoves = utility.getJumpMoves();
                if (!jumpMoves.isEmpty()) {
                    ai.makeMove(jumpMoves.get(0), copyRedPieces, copyBlackPieces, false, true);
                }

                score = miniMax(copyRedPieces, copyBlackPieces, depth - 1, max, min, false);

                // save the lowest score
                if (score <= min) {
                    min = score;
                    tempBestMove = move;
                }
                // apply pruning
                if (min <= max) {
                    break;
                }
            }
            bestMove = tempBestMove;
            return score;
        }
    }

    // Calculates the heuristic to determine a board's value.
     
    private int heuristic(Map<String, Piece> redPieces, Map<String, Piece> blackPieces) {
        int diffBlackRed = blackPieces.size() - redPieces.size();
        int numberKings = 0;
        for (String black : blackPieces.keySet()) {
            if (blackPieces.get(black).isKing()) {
                numberKings++;
            }
        }
        return diffBlackRed + numberKings;
    }

    public String[] getBestMove() {
        return bestMove;
    }
}
