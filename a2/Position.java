package a2;

public class Position {
	private char piece;
	public static final char UNPLAYABLE = '*';
	public static final char EMPTY = '.';
	public static final char BLACK = 'B';
	public static final char WHITE = 'W';
	
	// CONSTRUCTOR
	public Position(char piece) {
		super();
		this.piece = piece;
	}
	
	// METHODS
	public boolean canPlay(){
		return false;
	}
	public char getPiece() {
		return piece;
	}

	public void setPiece(char piece) {
		this.piece = piece;
	}
}
