package a2;

import java.util.Scanner;

public class Player {
	private String name;
	private char piece;
	public Player(String name, char piece) {
		super();
		this.name = name;
		this.piece = piece;
	}
	public int[] getPlayerMove() {
		Scanner sc = new Scanner(System.in);
		while(true) {
			try {
				String input = sc.nextLine();
				int row = Character.getNumericValue(input.charAt(0));
				int col = Character.getNumericValue(input.charAt(1));
				if (input.length() != 2) {
					System.out.println("Please enter only two valid numbers");
				} else if (row >Board.BOARD_DIM || row <0 || col > Board.BOARD_DIM || col <0) {
					System.out.println("Please enter a valid row and column number.");
				}  else {
					int[] arr = {row,col};
					return arr;
				}
				
			}
			catch(NumberFormatException e){
				System.out.println("Please enter two valid number.");
			}
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public char getPiece() {
		return piece;
	}
	public void setpiece(char piece) {
		this.piece = piece;
	}
	public int getTurnAction() {
		Scanner sc = new Scanner(System.in);
		while(true) {
			try {
				int turnAction = Integer.valueOf(sc.nextLine());
				if (turnAction > 3 || turnAction <1) {
					System.out.println("Please input a valid number (1-3)");
				} else {
					return turnAction;
				}
			}catch(NumberFormatException e) {
				System.out.println("Please input a number (1-3).");
			}
		}
		
	}
	
	
}
