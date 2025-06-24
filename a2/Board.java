package a2;
public class Board {
	private String name;
	public static int BOARD_DIM = 8;
	private Position boardPieces[][];
	private String p1Name;
	private String p2Name;
	private String currentPName;
	
	public Board(String save_file) {
		this.name = save_file;
		this.boardPieces = new Position[Board.BOARD_DIM][Board.BOARD_DIM];		
	}
	public Board(String save_file, Position[][] boardPieces, String p1Name, String p2Name, String currentPName) {
		this.name = save_file.substring(4,save_file.length() - 4);
		this.p1Name = p1Name;
		this.p2Name = p2Name;
		this.currentPName = currentPName;
		this.boardPieces = boardPieces;	
	}
	private void initializeBoard() {
		for (int row = 0; row < BOARD_DIM; row++) {
			for (int col = 0; col < BOARD_DIM; col++) {
					if (row == 0 && col == 0 || row == 7 && col ==7 || row == 7 && col == 0 || row ==0 && col ==7 ) {
						Position newPosition = new Position(Position.UNPLAYABLE);
						boardPieces[row][col] = newPosition;
					}
					else {
						Position newPlayablePosition = new PlayablePosition(Position.EMPTY);
						boardPieces[row][col] = newPlayablePosition;	
					}
			}
		}
	}
	public void drawBoard(){

		for (int i = 0; i< BOARD_DIM; i++) {
			System.out.print(i);
		}
		System.out.print("\n");
//		
		for (int i = 0; i< BOARD_DIM +1; i++) {
			System.out.print("-");
			if (i == (BOARD_DIM )) {
				System.out.println("");
			}
		}
		
		for (int row = 0; row < BOARD_DIM; row++) {

			for (int col = 0; col < BOARD_DIM; col++) {
				System.out.print(boardPieces[row][col].getPiece()); 
			}
			System.out.print("|" + row );
			System.out.println("");
		}
	}
	public void takeTurn(Player currentPlayer) {
		System.out.println(currentPlayer.getName() + ", please enter a row and col number(ex 00).");
			boolean moveCompleted = false;
			while(!moveCompleted) {
				boolean outflanked = false;
				int[] move = currentPlayer.getPlayerMove();
				int rowInd = move[0];
				int colInd = move[1];
				
					// Check if position is empty, and check if position will outflank a piece and if it does then flip any piece in between
					if(boardPieces[rowInd][colInd].canPlay()) {
						if (checkOutFlank(rowInd,colInd,0,1,currentPlayer)) {
							flipPieces(rowInd,colInd,0,1,currentPlayer);
							outflanked =true;
						} 
						if (checkOutFlank(rowInd, colInd, 0, -1,currentPlayer)) {
							flipPieces(rowInd, colInd, 0, -1,currentPlayer);
							outflanked =true;
						}
						if (checkOutFlank(rowInd, colInd, -1, 0,currentPlayer)) {
							flipPieces(rowInd,colInd,-1,0,currentPlayer);
							outflanked =true;
						}
						if (checkOutFlank(rowInd, colInd, 1, 0,currentPlayer)) {
							flipPieces(rowInd, colInd, 1, 0,currentPlayer);
							outflanked =true;
						}
						if (checkOutFlank(rowInd, colInd, -1, 1,currentPlayer)) {
							flipPieces(rowInd, colInd, -1, 1,currentPlayer);
							outflanked =true;
						}
						if (checkOutFlank(rowInd, colInd, 1, 1,currentPlayer)) {
							flipPieces(rowInd, colInd, 1, 1,currentPlayer);
							outflanked =true;
						}
						if (checkOutFlank(rowInd, colInd, 1, -1,currentPlayer))	{
							flipPieces(rowInd, colInd, 1, -1, currentPlayer);
							outflanked =true;
						}
						if (checkOutFlank(rowInd, colInd, -1, -1,currentPlayer)){
							flipPieces(rowInd, colInd, -1, -1, currentPlayer);
							outflanked =true;	
						}		
						// only the current move outflanks opponent's pieces, then we set the piece down
						if (outflanked) {
							boardPieces[rowInd][colInd].setPiece(currentPlayer.getPiece());
							moveCompleted = true;
						} else {
							System.out.println("Move does not outflank your opponent's pieces, please play another move!");
						}
					}
					else {
					System.out.println("Spot is taken! please choose again");
				}		
			}
	}
	
	public boolean checkCanPlay(Player currentPlayer) {
		for (int row = 0; row < this.BOARD_DIM; row++) {
			for (int col = 0; col < this.BOARD_DIM; col++) {
				if (this.boardPieces[row][col].canPlay()) {
					// check right, left, up, down, up-right, down-right, down-left, top-left 
					if (	checkOutFlank(row,col, 0, 1,currentPlayer)||
							checkOutFlank(row, col, 0, -1,currentPlayer) ||
							checkOutFlank(row, col, -1, 0,currentPlayer) ||
							checkOutFlank(row, col, 1, 0,currentPlayer) ||
							checkOutFlank(row, col, -1, 1,currentPlayer)||
							checkOutFlank(row, col, 1, 1,currentPlayer)||
							checkOutFlank(row, col, 1, -1,currentPlayer)||
							checkOutFlank(row, col, -1, -1,currentPlayer)){
						
							return true;
						} 
				}
			}
		}
		if (this.boardPieces[7][5].canPlay()) {
			// check right, left, up, down, up-right, down-right, down-left, top-left 
			if (	checkOutFlank(7,5, 0, 1,currentPlayer)||
					checkOutFlank(7, 5, 0, -1,currentPlayer) ||
					checkOutFlank(7, 5, -1, 0,currentPlayer) ||
					checkOutFlank(7, 5, 1, 0,currentPlayer) ||
					checkOutFlank(7, 5, -1, 1,currentPlayer)||
					checkOutFlank(7, 5, 1, 1,currentPlayer)||
					checkOutFlank(7, 5, 1, -1,currentPlayer)||
					checkOutFlank(7, 5, -1, -1,currentPlayer)){

					return true;
				} 
		}
			return false;
	} 
	
	private boolean checkOutFlank(int row, int col, int rowDir, int colDir, Player currentPlayer ) {
	
		char opponentSymbol = currentPlayer.getPiece() == Position.BLACK ? Position.WHITE : Position.BLACK;
		
		// if next piece isn't out of bound, then calculate.
		if ((row+rowDir) != -1 && (row+rowDir) != Board.BOARD_DIM &&  (col+colDir) != -1 && (col+colDir) != Board.BOARD_DIM && boardPieces[row+rowDir][col+colDir].getPiece() == opponentSymbol) {
		// If the next position on that diagonal is the opposite symbol,
							while (( (row+rowDir) <Board.BOARD_DIM)  && ((row+rowDir)>= 0) && ((col +colDir) < Board.BOARD_DIM) && ((col+colDir >= 0))) {
					row += rowDir;
					col += colDir;
					if (boardPieces[row][col].getPiece() == currentPlayer.getPiece()) {
						return true;
					} else if (boardPieces[row][col].getPiece() != Position.BLACK && boardPieces[row][col].getPiece() != Position.WHITE) {
						return false;
					}
				}
		}
		return false;
	}
	
	// flipPieces flips pieces in between, and it's only called when a move outflanks opponent's pieces in a given direction (based on rowDir,colDir)
	private void flipPieces(int row, int col, int rowDir, int colDir, Player currentPlayer) {
// 			while loop makes sure it checks within bounds, then flips piece if it's not current player's piece
			while (row < Board.BOARD_DIM && col <Board.BOARD_DIM && row > 0 && col > 0) {
				row += rowDir;
				col += colDir;				
				if (boardPieces[row][col].getPiece() == currentPlayer.getPiece()) {
					break;
				}else {
					this.boardPieces[row][col].setPiece(currentPlayer.getPiece());
				}
			}
	}	
	public String getP1Name() {
		return p1Name;
	}
	public void setP1Name(String p1Name) {
		this.p1Name = p1Name;
	}
	public String getP2Name() {
		return p2Name;
	}
	public void setP2Name(String p2Name) {
		this.p2Name = p2Name;
	}
	public String getCurrentPName() {
		return currentPName;
	}
	public void setCurrentPName(String currentPName) {
		this.currentPName = currentPName;
	}	
	public void setStartingBoard(int startingPos) {
		initializeBoard();
		// if starting position is 1, set Standard
				if (startingPos ==1) {
					boardPieces[3][3] = new Position(Position.WHITE);
					boardPieces[4][4] = new Position(Position.WHITE);

					boardPieces[4][3] = new Position(Position.BLACK);
					boardPieces[3][4] = new Position(Position.BLACK);
				} else if (startingPos ==2) {
					int array[][][] = {{{2,2},{3,2},{2,3},{3,3},{4,4},{4,5},{5,4},{5,5}},{{2,4},{3,4},{2,5},{3,5},{4,2},{4,3},{5,2},{5,3}}};
					//for each combination of the first array (white pieces)
					for (int[] combination: array[0]) {
						boardPieces[combination[0]][combination[1]] = new Position(Position.WHITE);		
					}
					for (int[] combination: array[1]) {
						boardPieces[combination[0]][combination[1]] = new Position(Position.BLACK);
					}
				}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Position[][] getBoardPieces() {
		return boardPieces;
	}
	public void setBoardPieces(Position[][] boardPieces) {
		this.boardPieces = boardPieces;
	}
}
