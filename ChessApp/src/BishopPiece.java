import javax.swing.ImageIcon;

public class BishopPiece extends ChessPiece {
	// 1 arg constructor
	public BishopPiece(char side) {
		super(side);
		if (side == 'W')
			this.setImg(new ImageIcon("WhiteBishop.png").getImage());
		else
			this.setImg(new ImageIcon("BlackBishop.png").getImage());
	}

	// Checks the legality of a move by using the initial position of a piece, where
	// to move, and the board in use
	@Override
	public boolean moveLegality(int[] pieceNota, int[] moveNota, ChessPiece[][] board) {
		// Move Place
		if (!(board[moveNota[0]][moveNota[1]] == null || (board[moveNota[0]][moveNota[1]] instanceof ChessPiece
				&& board[moveNota[0]][moveNota[1]].getSide() != this.getSide()))) {
			return false;
		}

		int verticalMove = moveNota[0] - pieceNota[0];// Up (-) and Down (+)
		int horizontalMove = moveNota[1] - pieceNota[1];// Left (-) and Right (+)

		if (verticalMove == 0 || horizontalMove == 0)
			return false;

		if (Math.abs(verticalMove) != Math.abs(horizontalMove)) {
			return false;
		}

		int vertIncrement = verticalMove / Math.abs(verticalMove);
		int horiIncrement = horizontalMove / Math.abs(horizontalMove);

		int col = pieceNota[1] + horiIncrement;
		for (int row = pieceNota[0] + vertIncrement; Math.abs(moveNota[0] - row) > 0; row += vertIncrement) {
			if (board[row][col] != null)
				return false;
			col += horiIncrement;
		}

		return true;
	}

	@Override
	public String toString() {
		return "B";
	}

}