import javax.swing.ImageIcon;

public class RookPiece extends ChessPiece {
	// 1 Arg constructor
	public RookPiece(char side) {
		super(side);
		if (side == 'W')
			this.setImg(new ImageIcon("WhiteRook.png").getImage());
		else
			this.setImg(new ImageIcon("BlackRook.png").getImage());
	}

	// Checks the legality of a move by using the initial position of a piece, where
	// to move, and the board in use
	@Override
	public boolean moveLegality(int[] pieceNota, int[] moveNota, ChessPiece[][] board) {
		if (!(board[moveNota[0]][moveNota[1]] == null || (board[moveNota[0]][moveNota[1]] instanceof ChessPiece
				&& board[moveNota[0]][moveNota[1]].getSide() != this.getSide()))) {
			return false;
		}

		int verticalMove = moveNota[0] - pieceNota[0];// Up (-) and Down (+)
		int horizontalMove = moveNota[1] - pieceNota[1];// Left (-) and Right (+)

		if (verticalMove == 0 && horizontalMove == 0)
			return false;

		if (verticalMove != 0) {
			int vertIncrement = verticalMove / Math.abs(verticalMove);

			int col = pieceNota[1];
			for (int row = pieceNota[0] + vertIncrement; Math.abs(moveNota[0] - row) > 0; row += vertIncrement)
				if (board[row][col] != null)
					return false;

			if (pieceNota[1] != moveNota[1])
				return false;
		} else {
			int horiIncrement = horizontalMove / Math.abs(horizontalMove);

			int row = pieceNota[0];
			for (int col = pieceNota[1] + horiIncrement; Math.abs(moveNota[1] - col) > 0; col += horiIncrement)
				if (board[row][col] != null)
					return false;

			if (pieceNota[0] != moveNota[0])
				return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "R";
	}

}