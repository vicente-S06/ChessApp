import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Chess extends JPanel {
    // Data fields
    private static final int WIDTH = 860;
    private static final int HEIGHT = 560;

    private Timer t;
    private Timer t2;
    private ChessBoard board;
    private boolean hasClickedOnPiece;
    private boolean pendingPromotion = false;

    private int[] startClick = new int[2];
    private int[] endClick = new int[2];
    private int time = 0;
    // test values will not appear when run with testingPhase
    // false
    private static String p1Name = "test", p2Name = "test2";
    private static char p1Side = 'W';
    private static int p1Score = 0, p2Score = 0;
    private static int p1Timer = 180; // In seconds
    private static int p2Timer = 180; // In seconds

    private static boolean testingPhase = true;

    public Chess() {
        // Declare data fields
        hasClickedOnPiece = false;
        board = new ChessBoard();

        // Start mouse input
        Mouse myMouse = new Mouse();
        addMouseListener(myMouse);

        // Start timer
        t = new Timer(20, new T1Listener());
        t.start();
        t2 = new Timer(1000, new T2Listener());
        t2.start();
    }

    // Draws everything on the JPanel
    public void paintComponent(Graphics g) {
        drawBoard(g);
        drawHighlight(g);
        drawNotation(g);
        drawPieces(g);
        drawPromotionMenu(g);
        drawTimers(g);
        drawEndGameScreen(g);
    }

    // Action lister purely used to repaint the board
    private class T1Listener implements ActionListener {

        // actionPerformed method will be executed every 20 milliseconds by Timer
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }

    // Keeps track of the game countdown timers
    private class T2Listener implements ActionListener {

        // actionPerformed method will be executed every 1000 milliseconds by Timer
        public void actionPerformed(ActionEvent e) {
            if (!board.getGameFinished()) {
                if (board.getTurn() == p1Side) {
                    p1Timer--;
                } else {
                    p2Timer--;
                }

                if (p1Timer == 0) {
                    if (p1Side == 'W')
                        board.getMovesList().add("0-1");
                    else
                        board.getMovesList().add("1-0");
                    p2Score++;
                    board.setGameFinished(true);
                } else if (p2Timer == 0) {
                    if (p1Side == 'W')
                        board.getMovesList().add("1-0");
                    else
                        board.getMovesList().add("0-1");
                    p1Score++;
                    board.setGameFinished(true);
                }
            }
        }
    }

    /*
     * Method: pawnPromotion()
     * ============================================================================
     * Precondition: pendingPromotion is true
     * ============================================================================
     * Postcondition: receives location of the pawn, as well as the Y coordinate of
     * the mouse to select the desired option to promote the pawn in the back row.
     * Then, it plays the promotionSound audio.
     */
    public void pawnPromotion(int[] location, int mouseY) {
        char s;
        if (location[0] == 0) {// white
            s = 'W';
        } else {// black
            s = 'B';
        }

        if (mouseY <= 64) {
            board.getBoard()[location[0]][location[1]] = new QueenPiece(s);
            board.getMovesList().set(board.getMovesList().size() - 1,
                    board.getMovesList().get(board.getMovesList().size() - 1) + "=Q");
        } else if (mouseY >= 70 && mouseY <= 134) {
            board.getBoard()[location[0]][location[1]] = new KnightPiece(s);
            board.getMovesList().set(board.getMovesList().size() - 1,
                    board.getMovesList().get(board.getMovesList().size() - 1) + "=N");
        } else if (mouseY >= 140 && mouseY <= 204) {
            board.getBoard()[location[0]][location[1]] = new RookPiece(s);
            board.getMovesList().set(board.getMovesList().size() - 1,
                    board.getMovesList().get(board.getMovesList().size() - 1) + "=R");
        } else if (mouseY >= 210) {
            board.getBoard()[location[0]][location[1]] = new BishopPiece(s);
            board.getMovesList().set(board.getMovesList().size() - 1,
                    board.getMovesList().get(board.getMovesList().size() - 1) + "=B");
        }

        board.getBoard()[location[0]][location[1]].setHasMoved(true);
        ChessBoard.playPromotionSound();
    }

    /*
     * Method: whitePawnInBackRow()
     * ============================================================================
     * Precondition: N/A
     * ============================================================================
     * Postcondition: Checks if there is a white pawn in the top row / h row.
     */
    public boolean whitePawnInBackRow() {
        for (int i = 0; i < board.getBoard()[0].length; i++) {
            if (board.getBoard()[0][i] instanceof PawnPiece) {
                pendingPromotion = true;
                return true;
            }
        }
        pendingPromotion = false;
        return false;
    }

    /*
     * Method: blackPawnInBackRow()
     * ============================================================================
     * Precondition: N/A
     * ============================================================================
     * Postcondition: Checks if there is a black pawn in the bottom row / a row.
     */
    public boolean blackPawnInBackRow() {
        for (int i = 0; i < board.getBoard()[0].length; i++) {
            if (board.getBoard()[7][i] instanceof PawnPiece) {
                pendingPromotion = true;
                return true;
            }
        }
        pendingPromotion = false;
        return false;
    }

    /*
     * Method: drawTimers()
     * ============================================================================
     * Precondition: N/A
     * ============================================================================
     * Postcondition: Draws the timers for each player
     */
    public void drawTimers(Graphics g) {
        // Top Timer
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRoundRect(652, 13, 120, 50, 10, 10);
        g.setColor(new Color(230, 230, 230));
        g.fillRoundRect(650, 10, 120, 50, 10, 10);

        // Bottom Timer
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRoundRect(652, 503, 120, 50, 10, 10);
        g.setColor(new Color(230, 230, 230));
        g.fillRoundRect(650, 500, 120, 50, 10, 10);

        g.setColor(Color.black);
        g.setFont(new Font("Nirmala UI", Font.BOLD, 40));
        if (p1Timer % 60 > 9) {
            g.drawString((p1Timer / 60) + ":" + (p1Timer % 60), 670, 540);
        } else {
            g.drawString((p1Timer / 60) + ":0" + (p1Timer % 60), 670, 540);
        }

        if (p2Timer % 60 > 9) {
            g.drawString((p2Timer / 60) + ":" + (p2Timer % 60), 670, 50);
        } else {
            g.drawString((p2Timer / 60) + ":0" + (p2Timer % 60), 670, 50);
        }
    }

    /*
     * Method: drawEndGameScreen()
     * ============================================================================
     * Precondition: the game has finished
     * ============================================================================
     * Postcondition: Draws the end game screen, along with the score and an option
     * to re-match.
     */
    public void drawEndGameScreen(Graphics g) {
        int menuHeight = 250;
        int menuWidth = 230;
        int x = 0;
        if (board.getGameFinished() || x == 1) {
            // Shaded Background
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, 560, 560);

            // Shadow
            g.setColor(new Color(0, 0, 0, 10));
            for (int i = 166; i <= 175; i++) {
                g.fillRoundRect(i, i - 10, menuWidth, menuHeight, 30, 30);
            }
            // White mainPiece
            g.setColor(new Color(240, 240, 240));
            g.fillRoundRect(165, 155, menuWidth, menuHeight, 30, 30);

            // Green Top
            g.setColor(new Color(124, 193, 110));
            g.fillRoundRect(165, 155, menuWidth, 40, 30, 30);
            g.fillRect(165, 175, menuWidth, 35);
            g.fillArc(165, 195, menuWidth, 30, 0, -180);

            // White or Black Won

            g.setFont(new Font("Nirmala UI", Font.BOLD, 30));
            if (x == 1 || board.getMovesList().get(board.getMovesList().size() - 1).equals("1-0")) {
                g.setColor(new Color(0, 0, 0, 50));
                g.drawString("White Won!", 198, 202);
                g.setColor(Color.white);
                g.drawString("White Won!", 197, 200);
            } else {
                g.setColor(new Color(0, 0, 0, 50));
                g.drawString("Black Won!", 200, 202);
                g.setColor(Color.white);
                g.drawString("Black Won!", 199, 200);
            }

            // Scores Draw
            g.setFont(new Font("Nirmala UI", Font.BOLD, 45));
            g.setColor(new Color(0, 0, 0, 50));
            g.drawString(p1Score + "  -  " + p2Score, 222, 283);
            g.setColor(new Color(50, 50, 50));
            g.drawString(p1Score + "  -  " + p2Score, 220, 280);

            // Coordinate Shift Dependent on Player Name Length
            int p1NameShift = (p1Name.length() - 3) * 4;
            int p2NameShift = (p2Name.length() - 3) * 4;
            // Player Names Draw
            g.setFont(new Font("Monospaced", Font.PLAIN, 15));
            g.setColor(new Color(0, 0, 0, 50));
            g.drawString(p1Name, 219 - p1NameShift, 302);
            g.drawString(p2Name, 311 - p2NameShift, 302);
            g.setColor(new Color(50, 50, 50));
            g.drawString(p1Name, 218 - p1NameShift, 300);
            g.drawString(p2Name, 310 - p2NameShift, 300);

            // Draw Rematch Button
            g.setColor(new Color(0, 0, 0, 50));
            g.fillRoundRect(232, 333, 100, 40, 30, 30);
            g.setColor(new Color(250, 180, 0));
            g.fillRoundRect(230, 330, 100, 40, 30, 30);

            g.setFont(new Font("Nirmala UI", Font.BOLD, 16));
            g.setColor(new Color(50, 50, 50));
            g.drawString("Rematch?", 243, 356);

        }
    }

    /*
     * Method: drawPromotionMenu()
     * ============================================================================
     * Precondition: pawn of either side in any back row
     * ============================================================================
     * Postcondition: Draws the pawn promotion side menu correspondent to which side
     * (White or Black).
     */
    public void drawPromotionMenu(Graphics g) {
        if (!board.getGameFinished())
            if (whitePawnInBackRow()) {
                // Shadow
                g.setColor(new Color(0, 0, 0, 10));
                for (int i = 1; i <= 7; i++) {
                    g.fillRoundRect(570 + i, 10 + i, 70, 280, 10, 10);
                }

                // White Menu
                g.setColor(new Color(220, 220, 220));
                g.fillRoundRect(570, 10, 70, 280, 10, 10);
                g.setColor(new Color(235, 235, 235));
                g.fillRoundRect(575, 15, 60, 270, 10, 10);

                // Piece Images
                g.drawImage(new ImageIcon("WhiteQueen.png").getImage(), 573, 13, 64, 64, null);
                g.drawImage(new ImageIcon("WhiteKnight.png").getImage(), 573, 83, 64, 64, null);
                g.drawImage(new ImageIcon("WhiteRook.png").getImage(), 573, 153, 64, 64, null);
                g.drawImage(new ImageIcon("WhiteBishop.png").getImage(), 573, 223, 64, 64, null);
            } else if (blackPawnInBackRow()) {
                // Shadow
                g.setColor(new Color(0, 0, 0, 10));
                for (int i = 1; i <= 7; i++) {
                    g.fillRoundRect(570 + i, 10 + i, 70, 280, 10, 10);
                }

                // WhiteMenu
                g.setColor(new Color(220, 220, 220));
                g.fillRoundRect(570, 10, 70, 280, 10, 10);
                g.setColor(new Color(235, 235, 235));
                g.fillRoundRect(575, 15, 60, 270, 10, 10);

                // Piece Images
                g.drawImage(new ImageIcon("BlackQueen.png").getImage(), 573, 13, 64, 64, null);
                g.drawImage(new ImageIcon("BlackKnight.png").getImage(), 573, 83, 64, 64, null);
                g.drawImage(new ImageIcon("BlackRook.png").getImage(), 573, 153, 64, 64, null);
                g.drawImage(new ImageIcon("BlackBishop.png").getImage(), 573, 223, 64, 64, null);
            }
    }

    /*
     * Method: drawHighlight()
     * ============================================================================
     * Precondition: hasClickedOnPiece is true
     * ============================================================================
     * Postcondition: Highlights the currently selected piece.
     */
    public void drawHighlight(Graphics g) {
        if (hasClickedOnPiece) {
            g.setColor(new Color(255, 255, 0, 137));
            g.fillRect(startClick[1] * 70, startClick[0] * 70, 70, 70);
        }
    }

    /*
     * Method: drawNotation()
     * ============================================================================
     * Precondition: N/A
     * ============================================================================
     * Postcondition: Draws the numbers and letters on the side of the board
     * correspondent to their correct notation.
     */
    public void drawNotation(Graphics g) {
        // Letters
        int letter = 97;
        g.setFont(new Font("COMIC_SANS", Font.BOLD, 15));
        for (int x = 57; x <= 560; x += 70) {
            g.setColor(new Color(245, 245, 220));
            g.drawString((char) letter + "", x, 554);
            letter++;

            x += 70;

            g.setColor(new Color(114, 163, 100));
            g.drawString((char) letter + "", x, 555);
            letter++;
        }

        // Numbers
        int numbers = 8;
        g.setFont(new Font("COMIC_SANS", Font.BOLD, 14));
        for (int y = 17; y <= 560; y += 70) {
            g.setColor(new Color(114, 163, 100));
            g.drawString(numbers + "", 5, y);
            numbers--;

            y += 70;

            g.setColor(new Color(245, 245, 220));
            g.drawString(numbers + "", 5, y);
            numbers--;
        }
    }

    /*
     * Method: drawBoard()
     * ============================================================================
     * Precondition: N/A
     * ============================================================================
     * Postcondition: Draws the board/grid where the pieces lie on, as well as the
     * side menu where move notations are drawn.
     */
    public void drawBoard(Graphics g) {// Draws the board and side UI
        g.setColor(new Color(40, 40, 40));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.DARK_GRAY);
        g.fillRoundRect(578, 70, 264, 420, 20, 20);

        // Squares
        g.setColor(new Color(245, 245, 220)); // beige
        for (int y = 0; y <= 490; y += 140)
            for (int x = 0; x <= 490; x += 140)
                g.fillRect(x, y, 70, 70);

        for (int y = 70; y <= 490; y += 140)
            for (int x = 70; x <= 490; x += 140)
                g.fillRect(x, y, 70, 70);

        g.setColor(new Color(114, 163, 100)); // green
        for (int y = 0; y <= 490; y += 140)
            for (int x = 70; x <= 490; x += 140)
                g.fillRect(x, y, 70, 70);

        for (int y = 70; y <= 490; y += 140)
            for (int x = 0; x <= 490; x += 140)
                g.fillRect(x, y, 70, 70);
    }

    /*
     * Method: drawPieces()
     * ============================================================================
     * Precondition: N/A
     * ============================================================================
     * Postcondition: Checks every square for a residing piece, and draws the Image
     * in the middle of the square.
     */
    public void drawPieces(Graphics g) { // Draws the images of each chess piece onto the board
        for (int row = 0; row <= 490; row += 70) {
            for (int col = 0; col <= 490; col += 70) {
                if (board.getBoard()[row / 70][col / 70] != null) {
                    if (board.getBoard()[row / 70][col / 70] instanceof EnPassantPiece) {
                        g.setColor(new Color(0, 0, 0, 50));
                        g.fillOval(col + 27, row + 28, 20, 20);
                        g.setColor(Color.red);
                        g.fillOval(col + 25, row + 25, 20, 20);
                    } else {
                        g.drawImage(board.getBoard()[row / 70][col / 70].getImg(), col + 3, row + 3, 64, 64, null);
                    }
                }
            }
        }

    }

    /*
     * Method: printMovesList()
     * ============================================================================
     * Precondition: N/A
     * ============================================================================
     * Postcondition: Prints the entire move notation list into the console.
     */
    public void printMovesList(ArrayList<String> list) {
        for (int m = 0; m < list.size(); m++) {
            System.out.print("   " + (m / 2 + 1) + ".\t");
            System.out.print(list.get(m));

            if (m < list.size() - 1) {
                m++;
                System.out.print("\t" + list.get(m));
            }
            System.out.println();
        }
    }

    /*
     * Method: updateScores()
     * ============================================================================
     * Precondition: Game has finished.
     * ============================================================================
     * Postcondition: Updates p1 and p2 scores by checking the last move in
     * movesList.
     */
    public void updateScores() {
        if (board.getMovesList().get(board.getMovesList().size() - 1).equals("1-0")) {
            if (p1Side == 'W')
                p1Score++;
            else
                p2Score++;
        } else {
            if (p1Side == 'W')
                p2Score++;
            else
                p1Score++;
        }
    }

    private class Mouse extends MouseAdapter {

        public void mousePressed(MouseEvent me) { // When clicked
            int mouseX = me.getX();
            int mouseY = me.getY();

            // receives left clicks during a running game.
            if (me.getButton() == MouseEvent.BUTTON1 && !board.getGameFinished()) {

                // gets initial position of piece on first click
                // moves the piece on second click
                // Only registers clicks on the chess board and if there is not a pending
                // promotion.
                if (!pendingPromotion && mouseX < 560 && mouseY < 560) {
                    if (hasClickedOnPiece == false && board.getBoard()[mouseY / 70][mouseX / 70] != null) {
                        startClick[0] = mouseY / 70;
                        startClick[1] = mouseX / 70;
                        hasClickedOnPiece = true;
                    } else if (hasClickedOnPiece && true) {
                        endClick[0] = mouseY / 70;
                        endClick[1] = mouseX / 70;
                        board.move(startClick, endClick);
                        if (board.getGameFinished()) {
                            updateScores();
                        }

                        // print movelist
                        System.out.println("===========================");
                        printMovesList(board.getMovesList());

                        hasClickedOnPiece = false;
                        System.out.println("===========================");
                    }
                } else if (pendingPromotion && (mouseX >= 573 && mouseX <= 637) && (mouseY >= 13 && mouseY <= 287)) {
                    // only able to click if pendingPromotion is true, and the mouse coordinates are
                    // within the Domain: [573, 637] and Range: [13, 287].
                    for (int i = 0; i < board.getBoard()[0].length; i++) {
                        if (board.getBoard()[0][i] instanceof PawnPiece) {
                            int[] location = { 0, i };
                            pawnPromotion(location, mouseY - 13);
                        }
                        if (board.getBoard()[7][i] instanceof PawnPiece) {
                            int[] location = { 7, i };
                            pawnPromotion(location, mouseY - 13);
                        }
                    }
                }
            }

        }
    }

    static JTextPane textPane;
    static JScrollPane scrollpane;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // Pre Match Information Gathering
        if (!testingPhase) {
            System.out.println("Names will get cut off if longer than 10 characters");
            do {
                System.out.println("     Enter player 1's Name: ");
                System.out.println("(Must be of atleast 2 characters)");
                p1Name = scan.next();
            } while (p1Name.length() < 2);

            do {
                System.out.println("     Enter player 2's Name: ");
                System.out.println("(Must be of atleast 2 characters)");
                p2Name = scan.next();
            } while (p2Name.length() < 2);

            if (p1Name.length() > 10)
                p1Name = p1Name.substring(0, 10);
            if (p2Name.length() > 10)
                p2Name = p2Name.substring(0, 10);

            do {
                System.out.println("  Enter player 1's Side: ");
                System.out.println("(Must be either 'W' or 'B')");
                p1Side = scan.next().toUpperCase().charAt(0);
            } while (p1Side != 'W' && p1Side != 'B');
        }

        // creating a JFrame object and setting title bar
        JFrame frame = new JFrame("Walmart Chess");
        // setting icon for title bar
        frame.setIconImage(new ImageIcon("BlackPawn.png").getImage());
        // setting size of JFrame
        frame.setSize(WIDTH + 16, HEIGHT + 39);
        // setting initial location of JFrame
        // Home: 800, 200 SchoolChrome: 450, 70
        frame.setLocation(450, 70);
        // allows JFrame to be closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // loads Chess JPanel into this JFrame (must match class name)
        frame.setContentPane(new Chess());

        // sets JFrame visible
        frame.setVisible(true);
    }
}