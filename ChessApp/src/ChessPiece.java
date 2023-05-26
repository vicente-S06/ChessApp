import java.awt.Image;
//Super class of all other chess pieces
public class ChessPiece {
	//Data fields
	private boolean hasMoved;
	private Image img;
	private char side;// W or B

	public ChessPiece(char side) {
		this.side = side;
		hasMoved = false;
	}

	public boolean moveLegality(int[] pieceNota, int[] moveNota, ChessPiece[][] board) {
		return false;
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public boolean getHasMoved() {
		return hasMoved;
	}

	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	public char getSide() {
		return side;
	}

	public void setSide(char side) {
		this.side = side;
	}

	@Override
	public String toString() {

		return "";
	}

}