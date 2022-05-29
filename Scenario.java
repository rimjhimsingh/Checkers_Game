package a2.csd311.checkers;


import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.Map;


// Contains board configurations to test special moves.

public class Scenario {

    private final Map<String, Piece> redPieces;
    private final Map<String, Piece> blackPieces;
    private final JButton[][] buttonBoard;
    private final BufferedImage redPiece;
    private final BufferedImage blackPiece;

    Scenario(Board board) {
        redPieces = board.getRedPieces();
        blackPieces = board.getBlackPieces();
        buttonBoard = board.getButtonBoard();
        redPiece = board.getRedPiece();
        blackPiece = board.getBlackPiece();
    }

    public void multipleJumpsAI() {

        redPieces.put(String.valueOf(4) + ":" + String.valueOf(5), new Piece());
        buttonBoard[5][4].setIcon(new ImageIcon(redPiece));

        redPieces.put(String.valueOf(4) + ":" + String.valueOf(3), new Piece());
        buttonBoard[3][4].setIcon(new ImageIcon(redPiece));

        redPieces.put(String.valueOf(2) + ":" + String.valueOf(6), new Piece());
        buttonBoard[6][2].setIcon(new ImageIcon(redPiece));

        blackPieces.put(String.valueOf(3) + ":" + String.valueOf(6), new Piece());
        buttonBoard[6][3].setIcon(new ImageIcon(blackPiece));
    }

    public void multipleJumpsHuman() {

        blackPieces.put(String.valueOf(3) + ":" + String.valueOf(4), new Piece());
        buttonBoard[4][3].setIcon(new ImageIcon(blackPiece));

        blackPieces.put(String.valueOf(3) + ":" + String.valueOf(6), new Piece());
        buttonBoard[6][3].setIcon(new ImageIcon(blackPiece));

        blackPieces.put(String.valueOf(5) + ":" + String.valueOf(4), new Piece());
        buttonBoard[4][5].setIcon(new ImageIcon(blackPiece));

        redPieces.put(String.valueOf(4) + ":" + String.valueOf(3), new Piece());
        buttonBoard[3][4].setIcon(new ImageIcon(redPiece));
    }
}
