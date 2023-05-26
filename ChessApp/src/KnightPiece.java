import javax.swing.ImageIcon;

public class KnightPiece extends ChessPiece {
	// 1 arg constructor
	public KnightPiece(char side) {
		super(side);
		if (side == 'W')
			this.setImg(new ImageIcon("WhiteKnight.png").getImage());
		else
			this.setImg(new ImageIcon("BlackKnight.png").getImage());
	}

	// Checks the legality of a move by using the initial position of a piece, where
	// to move, and the board in use
	@Override
	public boolean moveLegality(int[] pieceNota, int[] moveNota, ChessPiece[][] board) {
		return (board[moveNota[0]][moveNota[1]] == null || (board[moveNota[0]][moveNota[1]] instanceof ChessPiece
				&& board[moveNota[0]][moveNota[1]].getSide() != this.getSide()))
				&& (((moveNota[0] - 1 == pieceNota[0] || moveNota[0] + 1 == pieceNota[0])
						&& (moveNota[1] - 2 == pieceNota[1] || moveNota[1] + 2 == pieceNota[1]))
						|| ((moveNota[0] - 2 == pieceNota[0] || moveNota[0] + 2 == pieceNota[0])
								&& (moveNota[1] - 1 == pieceNota[1] || moveNota[1] + 1 == pieceNota[1])));
	}

	@Override
	public String toString() {
		return "N";
	}

}