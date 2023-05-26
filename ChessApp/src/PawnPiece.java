import javax.swing.*;

public class PawnPiece extends ChessPiece {

	// 1 Arg constructor
	public PawnPiece(char side) {
		super(side);
		if (side == 'W')
			this.setImg(new ImageIcon("WhitePawn.png").getImage());
		else
			this.setImg(new ImageIcon("BlackPawn.png").getImage());
	}

	// Checks the legality of a move by using the initial position of a piece, where
	// to move, and the board in use
	@Override
	public boolean moveLegality(int[] pieceNota, int[] moveNota, ChessPiece[][] board) {
		if (this.getSide() == 'W') {
			if ((pieceNota[0] - 1 == moveNota[0] || (pieceNota[0] - 2 == moveNota[0] && this.getHasMoved() == false
					&& board[moveNota[0] + 1][moveNota[1]] == null)) && pieceNota[1] == moveNota[1]
					&& board[moveNota[0]][moveNota[1]] == null) {
				return true;
			} else if ((board[moveNota[0]][moveNota[1]] instanceof ChessPiece
					&& board[moveNota[0]][moveNota[1]].getSide() == 'B')
					&& (pieceNota[0] - 1 == moveNota[0]
							&& (pieceNota[1] + 1 == moveNota[1] || pieceNota[1] - 1 == moveNota[1]))) {
				return true;
			}
		} else if (this.getSide() == 'B') {
			if ((pieceNota[0] + 1 == moveNota[0] || (pieceNota[0] + 2 == moveNota[0] && this.getHasMoved() == false
					&& board[moveNota[0] - 1][moveNota[1]] == null)) && pieceNota[1] == moveNota[1]
					&& board[moveNota[0]][moveNota[1]] == null) {
				return true;
			} else if ((board[moveNota[0]][moveNota[1]] instanceof ChessPiece
					&& board[moveNota[0]][moveNota[1]].getSide() == 'W')
					&& (pieceNota[0] + 1 == moveNota[0]
							&& (pieceNota[1] - 1 == moveNota[1] || pieceNota[1] + 1 == moveNota[1]))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "P";
	}
}
