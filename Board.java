package a2.csd311.checkers;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Board {

    private Human human;
    private AI ai;
    private Utility utility;

    private Map<String, Piece> redPieces = new HashMap<>();
    private Map<String, Piece> blackPieces = new HashMap<>();

    private BufferedImage redPiece;
    private BufferedImage redKingPiece;
    private BufferedImage blackPiece;
    private BufferedImage blackKingPiece;
    private BufferedImage whitePiece;
    private BufferedImage whiteKingPiece;
    private BufferedImage yellowPiece;
    private BufferedImage yellowKingPiece;

    private JButton[][] buttonBoard = new JButton[8][8];
    private JButton[] simpleMoveFields = new JButton[4];
    private JButton[] jumpFields = new JButton[4];

    private Color dark_brown = new Color(153, 153, 255);
    private Color light_brown = new Color(205, 255, 204);
    private Color neon_green = new Color(124, 250, 80);
    private TextField textField;

    public Board(Human human, AI ai) {
        this.human = human;
        this.ai = ai;
        this.utility = new Utility();
        initGUI();
    }

    public final void initGUI() {

        JPanel panel = new JPanel(new GridLayout(0, 8));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JFrame frame = new JFrame(" Rimjhim   Shivam   Sankalp   Checkers");
        frame.setResizable(false);
       

        Container contentPane = frame.getContentPane();
        contentPane.add(panel, BorderLayout.NORTH);
        textField = new TextField();
        textField.setText("Feedback:");
        textField.setEnabled(false);
        contentPane.add(textField, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        setMenus(menuBar);

        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        loadPictures();

        // add 64 button to the board
        for (int y = buttonBoard.length - 1; y >= 0; y--) {
            for (int x = 0; x < buttonBoard[y].length; x++) {

                JButton b = new JButton();
                b.setOpaque(true);
                b.setActionCommand(String.valueOf(x) + ":" + String.valueOf(y));
                b.setBorderPainted(false);
                b.addActionListener(human);
                buttonBoard[y][x] = b;
                panel.add(buttonBoard[y][x]);


                if ((x % 2 == 1 && y % 2 == 1) || (x % 2 == 0 && y % 2 == 0)) {
                    b.setBackground(light_brown);

                } else {
                    b.setBackground(dark_brown);
                    if (y < 3) {
                        redPieces.put(String.valueOf(x) + ":" + String.valueOf(y), new Piece());
                        buttonBoard[y][x].setIcon(new ImageIcon(redPiece));
                    }
                    if (y > 4) {
                        blackPieces.put(String.valueOf(x) + ":" + String.valueOf(y), new Piece());
                        buttonBoard[y][x].setIcon(new ImageIcon(blackPiece));
                    }
                }
            }
        }

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        setMovablePieces();
    }

     //Adds three menus to the game with sub-menus
    
    private void setMenus(JMenuBar menuBar) {

        JMenu gameMenu = new JMenu("New Game");
        JMenu difficultyMenu = new JMenu("Algorithm");
        JMenu helpMenu = new JMenu("Help");

        JRadioButtonMenuItem random = new JRadioButtonMenuItem("Random");

        JRadioButtonMenuItem minimax = new JRadioButtonMenuItem("Minimax : Depth 10");

        difficultyMenu.add(random);
 
        difficultyMenu.add(minimax);

        ButtonGroup group = new ButtonGroup();
        group.add(random);

        group.add(minimax);

        JMenuItem newGame = new JMenuItem(" Start New Game");
        gameMenu.add(newGame);
        newGame.addActionListener(e -> {
            redPieces = new HashMap<>();
            blackPieces = new HashMap<>();
            buttonBoard = new JButton[8][8];
            initGUI();
        });

        random.setSelected(true);

        random.addActionListener(e -> {
            ai.setDepth(AI.RANDOM);
        });

        minimax.addActionListener(e -> {
            ai.setDepth(AI.MINIMAX);
        });

        JMenuItem newRules = new JMenuItem("Rules");
        helpMenu.add(newRules);

        // opens the default browser to show a page of the rules
        newRules.addActionListener(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://cardgames.io/checkers/#rules"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        menuBar.add(gameMenu);
        menuBar.add(difficultyMenu);
        menuBar.add(helpMenu);

    }

    
      //Loads the piece pictures
     
    private void loadPictures() {
        try {
            whitePiece = ImageIO.read(new File("images/whitePiece.png"));
            redPiece = ImageIO.read(new File("images/redPiece.png"));
            redKingPiece = ImageIO.read(new File("images/redKing.png"));
            blackPiece = ImageIO.read(new File("images/blackPiece.png"));
            blackKingPiece = ImageIO.read(new File("images/blackKingPiece.png"));
            whiteKingPiece = ImageIO.read(new File("images/whiteKing.png"));
            yellowPiece = ImageIO.read(new File("images/yellowPiece.png"));
            yellowKingPiece = ImageIO.read(new File("images/yellowKingPiece.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

         //Finds the movable red pieces and gives them a different color.
    
    public void setMovablePieces() {

        utility.clearMoves();

        for (String redKey : redPieces.keySet()) {

            utility.successor(redKey, redPieces, blackPieces, true);
        }

        List<String[]> simpleMoves = utility.getSimpleMoves();
        List<String[]> jumpMoves = utility.getJumpMoves();

        if (!jumpMoves.isEmpty()) {
            setWhitePieces(jumpMoves);
        } else {
            setWhitePieces(simpleMoves);
        }
        if (redPieces.size() == 0) {
            textField.setText("Game Over!");
        }
        if (simpleMoves.isEmpty() && jumpMoves.isEmpty()) {
            textField.setText("Game Over");
        }
    }

    
     //Borders the red pieces with a white color.
     
    private void setWhitePieces(List<String[]> moves) {

        clearBoard();

        for (String[] move : moves) {
            String key = move[1];
            int[] xy = utility.getXY(key);
            boolean king = redPieces.get(key).isKing();
            if (king) {
                buttonBoard[xy[1]][xy[0]].setIcon(new ImageIcon(whiteKingPiece));

            } else {
                buttonBoard[xy[1]][xy[0]].setIcon(new ImageIcon(whitePiece));
            }
        }

    }

    // Clears the board from the green and red fields as well as white and yellow pieces.
     
    private void clearBoard() {
        for (int y = buttonBoard.length - 1; y >= 0; y--) {
            for (int x = 0; x < buttonBoard[y].length; x++) {
                if (!((x % 2 == 1 && y % 2 == 1) || (x % 2 == 0 && y % 2 == 0))) {
                    buttonBoard[y][x].setBackground(dark_brown);

                    String key = x + ":" + y;
                    if (redPieces.containsKey(key)) {

                        if (redPieces.get(key).isKing()) {
                            buttonBoard[y][x].setIcon(new ImageIcon(redKingPiece));

                        } else {
                            buttonBoard[y][x].setIcon(new ImageIcon(redPiece));
                        }
                    }
                }
            }
        }
    }

    
      //Cleans the green fields if there a jump move possible by on of the red pieces.
     
    public void resetGreenFields() {

        utility.clearMoves();

        for (String redKey : redPieces.keySet()) {
            utility.successor(redKey, redPieces, blackPieces, true);
        }
        if (!utility.getJumpMoves().isEmpty()) {
            for (int i = 0; i < simpleMoveFields.length; i++) {
                if (simpleMoveFields[i] != null) {
                    simpleMoveFields[i].setBackground(dark_brown);
                    simpleMoveFields[i] = null;
                }
            }
        }


    }

    public JButton[] getSimpleMoveFields() {
        return simpleMoveFields;
    }

    public JButton[] getJumpFields() {
        return jumpFields;
    }

    public void setSimpleMoveFields(JButton[] simpleMoveFields) {
        this.simpleMoveFields = simpleMoveFields;
    }

    public void setJumpFields(JButton[] jumpFields) {
        this.jumpFields = jumpFields;
    }

    public Map<String, Piece> getRedPieces() {
        return redPieces;
    }

    public Map<String, Piece> getBlackPieces() {
        return blackPieces;
    }

    public JButton[][] getButtonBoard() {
        return buttonBoard;
    }

    public BufferedImage getRedPiece() {
        return redPiece;
    }

    public BufferedImage getRedKingPiece() {
        return redKingPiece;
    }

    public BufferedImage getBlackPiece() {
        return blackPiece;
    }

    public BufferedImage getBlackKingPiece() {
        return blackKingPiece;
    }

    public BufferedImage getYellowPiece() {
        return yellowPiece;
    }

    public BufferedImage getYellowKingPiece() {
        return yellowKingPiece;
    }

    public Color getNeon_green() {
        return neon_green;
    }

    public TextField getTextField() {
        return textField;
    }

}