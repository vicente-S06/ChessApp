import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ChessBoard {
	// Data fields
	private char turn;
	private ArrayList<String> movesList;
	private int moves;
	private ChessPiece[][] board;

	private int[] enPassantPieceLocation;
	private int enPassantPieceStart;
	private boolean enPassantPieceExists;

	private boolean gameFinished;

	// Sound Effects
	private static final String moveSoundPath = "moveSound.wav";
	private static final String promoteSoundPath = "promoteSound.wav";
	private static final String castleSoundPath = "castleSound.wav";
	private static final String captureSoundPath = "captureSound.wav";

	// Default constructor
	public ChessBoard() {

		gameFinished = false;
		movesList = new ArrayList<String>();
		moves = 0;
		turn = 'W';
		enPassantPieceStart = -2;
		enPassantPieceLocation = new int[2];
		enPassantPieceExists = false;

		// Creates the Board in a 2D array.
		board = new ChessPiece[8][8];
		// Pawns
		for (int i = 0; i < 8; i++) {
			board[1][i] = new PawnPiece('B');
			board[6][i] = new PawnPiece('W');
		}
		// Kings
		board[0][4] = new KingPiece('B');
		board[7][4] = new KingPiece('W');
		// Queens
		board[0][3] = new QueenPiece('B');
		board[7][3] = new QueenPiece('W');
		// Rooks
		board[0][0] = new RookPiece('B');
		board[7][0] = new RookPiece('W');
		board[0][7] = new RookPiece('B');
		board[7][7] = new RookPiece('W');
		// Knight
		board[0][1] = new KnightPiece('B');
		board[0][6] = new KnightPiece('B');
		board[7][1] = new KnightPiece('W');
		board[7][6] = new KnightPiece('W');
		// Bishop
		board[0][2] = new BishopPiece('B');
		board[0][5] = new BishopPiece('B');
		board[7][2] = new BishopPiece('W');
		board[7][5] = new BishopPiece('W');
	}

	// getters/setters
	public boolean getGameFinished() {
		return gameFinished;
	}
	public void setGameFinished(boolean b) {
		gameFinished = b;
	}

	public ChessPiece[][] getBoard() {
		return board;
	}

	public int getMoves() {
		return moves;
	}

	public char getTurn() {
		return turn;
	}

	public ArrayList<String> getMovesList() {
		return movesList;
	}


	/*
	 * Method: playSound()
	 * ============================================================================
	 * Precondition: valid .wav file exists in soundLocation
	 * ============================================================================
	 * Postcondition: Plays the corresponding sound
	 */
	private static void playSound(String soundLocation) {
		try {
			File soundPath = new File(soundLocation);
			if (soundPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInput);

				clip.setFramePosition(0);
				clip.start();
			} else {
				System.out.println("cant find file");
			}
		} catch (Exception ex) {
			System.out.println("Sound error");
		}

	}

	/*
	 * Method: playPromotionSound()
	 * ============================================================================
	 * Precondition: N/A
	 * ============================================================================
	 * Postcondition: Public method that plays the pawn promotion sound when called.
	 */
	public static void playPromotionSound() {
		playSound(promoteSoundPath);
	}

	/*
	 * Method: moveToNotation()
	 * ============================================================================
	 * Precondition: N/A
	 * ============================================================================
	 * Postcondition: Collects the indexes of the starting and ending positions of a
	 * piece, and creates/returns the correct chess move notation.
	 */
	private String moveToNotation(int[] startNota, int[] endNota) {
		String str = "";
		if (board[startNota[0]][startNota[1]] instanceof PawnPiece) {
			if (startNota[1] != endNota[1])
				str += (char) (97 + startNota[1]);
		} else {
			str += board[startNota[0]][startNota[1]].toString();
		}
		if (board[endNota[0]][endNota[1]] instanceof ChessPiece)
			str += "x";

		str += (char) (97 + endNota[1]);
		str += 8 - endNota[0];
		return str;
	}

	/*
	 * Method: enPassantCreate()
	 * ============================================================================
	 * Precondition: N/A
	 * ============================================================================
	 * Postcondition: Creates an instance of a piece that if taken by a pawn,
	 * deletes the pawn is it assigned to along with it.
	 */
	private void enPassantCreate(int[] startNota, int[] endNota) {

		if (startNota[0] > endNota[0]) {// white pawn
			board[endNota[0] + 1][endNota[1]] = new EnPassantPiece('W', endNota[0], endNota[1]);
			enPassantPieceLocation[0] = endNota[0] + 1;
			enPassantPieceLocation[1] = endNota[1];
		} else if (startNota[0] < endNota[0]) {// black pawn
			board[endNota[0] - 1][endNota[1]] = new EnPassantPiece('B', endNota[0], endNota[1]);
			enPassantPieceLocation[0] = endNota[0] - 1;
			enPassantPieceLocation[1] = endNota[1];
		}
		enPassantPieceStart = moves;
		enPassantPieceExists = true;
	}

	/*
	 * Method: move()
	 * ============================================================================
	 * Precondition: N/A
	 * ============================================================================
	 * Postcondition: Adds the move's notation to movesLisst. Also, checks if the
	 * match was won with this move, if so, add notation of winning to movesList,
	 * sets gameFinished to true. Furthermore, plays either standard moveSound or
	 * captureSound if a piece was captured. Then, updates the board and switches
	 * who's turn it is. If the move was not legal, print message.
	 */
	public void move(int[] startNota, int[] endNota) {
		ChessPiece store = board[startNota[0]][startNota[1]];

		// Castling Code
		if (board[startNota[0]][startNota[1]].getSide() == turn && isLegalCastling(startNota, endNota)) {
			// adds move to list of all made moves
			if (endNota[1] > startNota[1]) {
				movesList.add("O-O");
			} else {
				movesList.add("O-O-O");
			}

			playSound(castleSoundPath);

			if(store.getSide() == 'W') {
				turn = 'B';
			}else {
				turn = 'W';
			}
			moves++;
			
			if(enPassantPieceExists && enPassantPieceStart < moves) {
				board[enPassantPieceLocation[0]][enPassantPieceLocation[1]] = null;
			}
			
			// updates the board
			board[endNota[0]][endNota[1]] = store;
			board[startNota[0]][startNota[1]] = null;
			board[endNota[0]][endNota[1]].setHasMoved(true);

			// Standard Move Code
		} else if (board[startNota[0]][startNota[1]].getSide() == turn && moveIsLegal(startNota, endNota)) {
			// adds move to list of all made moves
			movesList.add(moveToNotation(startNota, endNota));

			// adds match end notation to movesList
			if (board[endNota[0]][endNota[1]] instanceof KingPiece) {
				movesList.set(movesList.size() - 1, movesList.get(movesList.size() - 1) + "#");
				if (board[endNota[0]][endNota[1]].getSide() == 'W') {
					movesList.add("0-1");
				} else {
					movesList.add("1-0");
				}
				gameFinished = true;
			}

			// Plays sound depending on capture or normal move
			if (board[endNota[0]][endNota[1]] instanceof ChessPiece) {
				playSound(captureSoundPath);
			} else {
				playSound(moveSoundPath);
			}
			
			if(board[endNota[0]][endNota[1]] instanceof EnPassantPiece) {
				EnPassantPiece piece = (EnPassantPiece)board[endNota[0]][endNota[1]];
				board[piece.getAssignedPawn()[0]][piece.getAssignedPawn()[1]] = null;
				enPassantPieceExists = false;
				piece = null;
			}
			
			if(store.getSide() == 'W') {
				turn = 'B';
			}else {
				turn = 'W';
			}
			moves++;
			
			if(enPassantPieceExists && enPassantPieceStart < moves) {
				board[enPassantPieceLocation[0]][enPassantPieceLocation[1]] = null;
			}

			// updates the board
			board[endNota[0]][endNota[1]] = store;
			board[startNota[0]][startNota[1]] = null;
			store.setHasMoved(true);

			if (store instanceof PawnPiece && Math.abs(startNota[0] - endNota[0]) == 2) {
				if (endNota[1] != 7 && board[endNota[0]][endNota[1] + 1] instanceof PawnPiece
						&& board[endNota[0]][endNota[1] + 1].getSide() != store.getSide()) {
					enPassantCreate(startNota, endNota);

				} else if (endNota[1] != 0 && board[endNota[0]][endNota[1] - 1] instanceof PawnPiece
						&& board[endNota[0]][endNota[1] - 1].getSide() != store.getSide()) {
					enPassantCreate(startNota, endNota);
				}
			}

		} else {// If move isn't legal, do nothing except print message.
			System.out.println("Move is not Legal.");
		}

		System.out.println("moves: " + moves);
	}

	/*
	 * Method: isLegalCastling()
	 * ============================================================================
	 * Precondition: N/A
	 * ============================================================================
	 * Checks if the move is a legal castle.
	 */
	private boolean isLegalCastling(int[] pieceNota, int[] moveNota) {
		// pieceNota is a King, hasn't moved, and only moves horizontally
		if (board[pieceNota[0]][pieceNota[1]] instanceof KingPiece && !board[pieceNota[0]][pieceNota[1]].getHasMoved()
				&& pieceNota[0] == moveNota[0] && pieceNota[1] != moveNota[1]) {

			int horizontalMove = moveNota[1] - pieceNota[1]; // Left (-) and Right (+)
			int horiIncrement = horizontalMove / Math.abs(horizontalMove);

			if (Math.abs(horizontalMove) != 2) {
				return false;
			}

			int[] rookPosition = new int[2];
			rookPosition[0] = pieceNota[0];

			if (horiIncrement > 0) {
				if (board[pieceNota[0]][7] == null
						|| (!(board[pieceNota[0]][7] instanceof RookPiece) || board[pieceNota[0]][7].getHasMoved())) {
					return false;
				}
				rookPosition[1] = 7;
			} else {
				if (board[pieceNota[0]][0] == null
						|| (!(board[pieceNota[0]][0] instanceof RookPiece) || board[pieceNota[0]][0].getHasMoved())) {
					return false;
				}
				rookPosition[1] = 0;
			}

			// Every square in between the rook and king is empty
			for (int col = pieceNota[1] + horiIncrement; Math.abs(moveNota[1] - col) > 0; col += horiIncrement) {
				if (board[pieceNota[0]][col] != null)
					return false;
			}

			board[rookPosition[0]][moveNota[1] + (horiIncrement * -1)] = board[rookPosition[0]][rookPosition[1]];
			board[rookPosition[0]][rookPosition[1]] = null;
			return true;
		}
		return false;
	}

	/*
	 * Method: moveIsLegal()
	 * ============================================================================
	 * Precondition: N/A
	 * ============================================================================
	 * Checks if the coordinates are inside the board, and then returns the result
	 * of calling the moveLegality method for the piece at pieceNota on the board.
	 */
	private boolean moveIsLegal(int[] pieceNota, int[] moveNota) {
		if ((moveNota[0] >= 8 || moveNota[0] < 0) || (moveNota[1] >= 8 || moveNota[1] < 0)
				|| board[pieceNota[0]][pieceNota[1]] == null
				|| (!(board[pieceNota[0]][pieceNota[1]] instanceof PawnPiece)
						&& board[moveNota[0]][moveNota[1]] instanceof EnPassantPiece)) {
			return false;
		}

		return board[pieceNota[0]][pieceNota[1]].moveLegality(pieceNota, moveNota, board);
	}

	@Override
	public String toString() {
		String result = "";
		for (int row = 0; row < board.length; row++) {
			result += (8 - row);
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] == null)
					result += "|" + " - ";
				else
					result += "|" + board[row][col];
			}
			result += "|" + "\n";
		}
		result += "   A   B   C   D   E   F   G   H  \n";
		return result;
	}

}