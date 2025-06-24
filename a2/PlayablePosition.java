package a2;

public class PlayablePosition extends Position {
	
	public PlayablePosition(char piece) {
		super(piece);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canPlay(){
		if (super.getPiece() == EMPTY) {
			return true;
		}
		else return false;
	}
}
