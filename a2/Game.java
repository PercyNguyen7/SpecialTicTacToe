package a2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Game {
	 private Board board;
	 private Player firstPlayer;
	 private Player secondPlayer;
	 private Player currentPlayer;
	 private String gameState;
	 private boolean gameLoaded;
		
	 //CONSTRUCTOR
	public Game(Player p1, Player p2){
		this.firstPlayer = p1;
		this.secondPlayer = p2;
		this.currentPlayer = p1;
		this.board = new Board("game");
		this.gameState = "play";
		this.gameLoaded = false;
	}
	public static void main(String[] args) {
		showMenu();
	}
	public static void showMenu() {
		System.out.println("1. Start a New Game"
				+ "\n" + "2. Load a Game" + "\n" + "3. Quit" );
		Scanner sc = new Scanner(System.in);
		while(true) {
			try {
				int menuChoice = Integer.valueOf(sc.nextLine());
					if (menuChoice < 0 || menuChoice >3){
						System.out.println("Please choose a number between 1-3.");
					// IF STARTING GAME
					} else if (menuChoice ==1) {
						System.out.println("Please input a name for Player 1.");
						String p1Name = sc.nextLine();
						System.out.println("Please input a name for Player 2.");
						String p2Name = sc.nextLine();
						Player p1 = new Player(p1Name, Position.BLACK);
						Player p2 = new Player(p2Name, Position.WHITE);
						
						Game g = new Game(p1,p2);
						g.start();
						break;
					}
					// IF LOADING GAME
					else if (menuChoice ==2) {
						while (true) {
							try {
								Board board = Game.load();
								Player p1 = new Player(board.getP1Name(),Position.BLACK);
								Player p2 = new Player(board.getP2Name(), Position.WHITE);
								Game savedG = new Game(p1,p2);
						
								savedG.setBoard(board);
								if (p1.getName().equals(board.getCurrentPName())) {
									savedG.setCurrentPlayer(p1);
								} else {
									savedG.setCurrentPlayer(p2);
								}
								savedG.setGameLoaded(true);
								savedG.play();
								break;
							} catch (IOException e) {
								System.out.println("File not found, please load a valid file name.");
								e.printStackTrace();
							}
						}
					}
					else if (menuChoice ==3) {
						System.out.println("Good call, go touch some grass!");
						break;
					}
					
			} catch (NumberFormatException e) {
				System.out.println("Please input a number (1-3).");
			}
		}
	}
	public void start() {
		// First ask for a number to load Starting Position.
		Scanner sc = new Scanner(System.in);
		System.out.println("Please choose your starting position" + "\n" + "1. Standard Starting Position" + "\n" + "2. Four-by-Four Starting Position");
		while(true) {
			try {
				int startingPosChoice = Integer.valueOf(sc.nextLine());
				if (startingPosChoice >2 || startingPosChoice <1) {
					System.out.println("Please choose a valid number (1-2).");
				} else {
					this.board.setStartingBoard(startingPosChoice);
					break;
				}
			}
			catch(NumberFormatException e){
				System.out.println("Please input a number (1-2).");
			}
		}
		play();
	}
	public void play() {
		while (gameState.equals("play")) {
			this.board.drawBoard();
			makeMove();
			checkGameOver(this.firstPlayer, this.secondPlayer);
			if(gameState.equals("play")) {
			switchPlayer();
			}
		}
		displayResults();
	}
	private void displayResults() {
		
		if (gameState.equals("conceded")){
			System.out.println("Player " + currentPlayer.getName() + " conceded the game.");
			if (currentPlayer.equals(firstPlayer) ) {
				System.out.println("Congratulations to player " + secondPlayer.getName()+ "who emerges victorious!");
			} else {
				System.out.println("Congratulations to player " + firstPlayer.getName() + "who emerges victorious");
			}
		} else if (gameState.equals("saved")) {
			 if (!gameLoaded) {
			        System.out.println("Your game has been saved to a new file entitled " + this.board.getName() + " in the src folder!");
			 } else {
			        System.out.println("The game has been saved in the same file as it was loaded, file entitled " + this.board.getName() );
			 }
		} else if (gameState.equals("over")) {
			int p1Score = 0;
			int p2Score = 0;
			for (int row = 0; row < Board.BOARD_DIM; row++) {
		       	 for (int col = 0; col < Board.BOARD_DIM; col++) {
		       		 if (this.board.getBoardPieces()[row][col].getPiece() == this.firstPlayer.getPiece()) {
		       			 p1Score++;
		       		 } else if (this.board.getBoardPieces()[row][col].getPiece() == this.secondPlayer.getPiece()){
		       			 p2Score++;
		       		 }
		       	 }
	        }
			if (p1Score > p2Score) {
				System.out.println(this.firstPlayer.getName() + " won with " + p1Score + " pieces while " + this.secondPlayer.getName() + " has "+ p2Score + " pieces!"  );
			
			} else if (p2Score > p1Score) {
				System.out.println(this.secondPlayer.getName() + " won with " + p2Score + " pieces while " + this.firstPlayer.getName() + " has "+ p1Score + " pieces!"  );
			
			} else if (p2Score == p1Score) {
				System.out.println("It's a draw, with both players each controlling " + p1Score + " pieces. Lame game." );
			}
		}
	}
	// check if both players can't play, then game is over
	private void checkGameOver(Player p1, Player p2) {
		if(!this.board.checkCanPlay(p1) && !this.board.checkCanPlay(p2)) {
			gameState = "over";
		} 
	}
	private void makeMove() {
		String thirdInstruction = "3. Forfeit your turn";
		if (this.board.checkCanPlay(currentPlayer)) {
			thirdInstruction = "3. Make a move";
		} 
		System.out.println(currentPlayer.getName() + "(" + currentPlayer.getPiece() + ")" + " to move" + "\n" + "1. Save" + "\n" + "2. Concede" + "\n" + thirdInstruction);	
		int playerMove = currentPlayer.getTurnAction();

		if (playerMove == 1) {
			save();
		}
		else if (playerMove == 2) {
			this.gameState = "conceded";
		}
		else if (playerMove ==3) {
			if (this.board.checkCanPlay(currentPlayer)) {
				this.board.takeTurn(currentPlayer);
			} else {
				System.out.println(currentPlayer.getName() + " forfeited their turn.");
			}
		}
	}
	private void switchPlayer() {
			currentPlayer = currentPlayer.equals(firstPlayer) ? secondPlayer : firstPlayer;
	}
	private void save() {
		String fileName;
		if (!gameLoaded) {
			int inc = 0; 

			while (true) {
					fileName  =  this.board.getName() + inc ;
				
		        // CREATE TEXT FILE with given name
			        try {
			            File newFile = new File("src/"+ fileName + ".txt");
			        	
			            if (newFile.createNewFile()) {
			              System.out.println("File created: " + newFile.getName());
			              
			              break;
			            } else {
			                inc++;
		//	              System.out.println("File already exists, please choose another file name!");
			            }
			          } catch (IOException e) {
			            System.out.println("An error occurred while trying to load game.");
			            e.printStackTrace();
			          }
			  }
		} else {
			// ONLY get game0
			fileName = this.board.getName();
		}
        // WRITE ON RECENT TEXT FILE
	        try {
	        	FileWriter myWriter = new FileWriter("src/"+ fileName + ".txt");
	         // Print PLAYER NAMES
	         	String savedGame = this.firstPlayer.getName() + "\n" + this.secondPlayer.getName() + "\n" + this.currentPlayer.getName() + "\n";
	         for (int i = 0; i< Board.BOARD_DIM; i++) {
	    		savedGame += i;
	    	}
	         savedGame += System.lineSeparator();
	         
	         for (int i = 0; i< Board.BOARD_DIM; i++) {
	 			savedGame+= "-";
	 			if (i == (Board.BOARD_DIM -1)) {
	 				savedGame += System.lineSeparator();
	 			}
	 		}
	         	
	         for (int row = 0; row < Board.BOARD_DIM; row++) {
	        	 for (int col = 0; col < Board.BOARD_DIM; col++) {
	        		 savedGame += this.board.getBoardPieces()[row][col].getPiece() ;
	        	 }
	        	savedGame += "|" + row; 
	        	savedGame += System.lineSeparator();
	         }
	         myWriter.write(savedGame);
	         myWriter.close();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("Error occured while writing on save file.");
	        }
	       this.board.setName(fileName); 
	    
		 // SWITCH gameSate to saved
		 this.gameState = "saved";
    }
	public static Board load() throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please input the file name");
		String file_name;
		// GET THE FILE NAME
			file_name = "src/"+ sc.nextLine();
			file_name += ".txt";
			
			FileReader myReader = new FileReader(file_name);
			BufferedReader br = new BufferedReader(myReader);
			
			Position[][] board = new Position[8][8];
			String p1Name="";
			String p2Name="";
			String currentPName = "";
			String line = br.readLine();
			int lineCount = 0;
			int row = 0;
			
			while (line != null) {
				if (lineCount == 0) {
					p1Name = line;
				} else if (lineCount == 1) {
					p2Name = line;
				} else if (lineCount == 2) {
						currentPName = line;
				} else if (lineCount >=5 ){		
					int boardDim = line.length() -2;
					for (int col= 0; col < boardDim; col++) {
						
						switch(line.charAt(col)) {
						case Position.EMPTY: 
							board[row][col] = new PlayablePosition(Position.EMPTY);
							break;
						case Position.BLACK:
							board[row][col] = new Position(Position.BLACK);
							break;
						case Position.WHITE: 
							board[row][col] = new Position(Position.WHITE);
							break;
						case Position.UNPLAYABLE:
							board[row][col] = new Position(Position.UNPLAYABLE);
							break;
						}
					}
					row++;
				}
				line = br.readLine();
				lineCount++;
			}
			myReader.close();
			return new Board(file_name, board, p1Name,p2Name,currentPName);
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public void setGameLoaded(boolean gameLoaded) {
		this.gameLoaded = gameLoaded;
	}
	private void setCurrentPlayer(Player player) {
		this.currentPlayer = player;
	}
}
