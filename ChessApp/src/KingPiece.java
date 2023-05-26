import javax.swing.ImageIcon;

public class KingPiece extends ChessPiece {
	// 1 Arg constructor
	public KingPiece(char side) {
		super(side);
		if (side == 'W')
			this.setImg(new ImageIcon("WhiteKing.png").getImage());
		else
			this.setImg(new ImageIcon("BlackKing.png").getImage());
	}

	// Checks the legality of a move by using the initial position of a piece, where
	// to move, and the board in use
	@Override
	public boolean moveLegality(int[] pieceNota, int[] moveNota, ChessPiece[][] board) {
		return (board[moveNota[0]][moveNota[1]] == null || (board[moveNota[0]][moveNota[1]] instanceof ChessPiece
				&& board[moveNota[0]][moveNota[1]].getSide() != this.getSide()))
				&& ((moveNota[0] - pieceNota[0] <= 1 && moveNota[0] - pieceNota[0] >= -1)
						&& (moveNota[1] - pieceNota[1] <= 1 && moveNota[1] - pieceNota[1] >= -1));
	}

	@Override
	public String toString() {
		return "K";
	}

}