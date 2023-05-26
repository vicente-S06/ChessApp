import javax.swing.ImageIcon;

public class EnPassantPiece extends ChessPiece {
	// data fields
	private int[] assignedPawn = new int[2];

	// 1 Arg constructor
	public EnPassantPiece(char side, int asPawnRow, int asPawnCol) {
		super(side);
		assignedPawn[0] = asPawnRow;
		assignedPawn[1] = asPawnCol;

		this.setImg(new ImageIcon("redDot.png").getImage());
	}

	public int[] getAssignedPawn() {
		return assignedPawn;
	}

}