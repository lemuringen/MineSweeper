package com.TH;

public class View {

	/**This method prints out the MineSweeper board to the console, showing
	 * bricks as hidden, exposed (it shows what it contains or how many 
	 * adjacent mines there are) or flagged depending on inputs from the player
	 * and the mines randomly populating the board. Formating is done to visually
	 * co-relate indices with intended bricks. Currently supports 1-2 numbered 
	 * indexes and 1 lettered index.
	 */
	public void updateView() { //render the gameboard
		System.out.println();
		printIndexLetters();
		printIndexNumbersAndBoard(); //kinda unfocused but a little easier to read
	}
	private void printIndexNumbersAndBoard() {
		System.out.printf("%5s", "+");
		for(int i = 0; i < Model.getGameBoard()[0].length*3; i++) {
			System.out.print("-");
		}
		System.out.print("\n");
		for(int r = 0; r < Model.getGameBoard().length; r++) {
			System.out.printf("%3s", r);
			System.out.print(" | ");
			for(int c = 0; c < Model.getGameBoard()[0].length; c++) {
				System.out.printf("%-3s", (" " + Model.getGameBoard()[r][c]));

			}
			System.out.print("\n");
		}
	}
	private void printIndexLetters() { // works up to YZ(26*26 as it will parse A-Z before AA)
		System.out.printf("%6s", "");
		for(int i = 0; i < Model.getGameBoard()[0].length; i++) {
			int indexValue = i;	// numerical index value of letter
			String indexLetters = ""; // base to which we concatenate the letter based index symbols
			
			if(i <26) {
				System.out.printf( "%-3s", " " + (char)(i+65)); // if there's only 1 letter print it out directly
			}else {	// if there are multiple letters we convert it to a base-26 system (symbols A-Z) but decrease the value of the first letter by one. So after A-Z we get AA-AZ instead of BA-BZ
			do{ 
				if(indexValue/26 == 0) {
					indexValue--;
				}
				indexLetters = (char)((indexValue % 26)+65) + indexLetters;				
				indexValue = indexValue / 26;
			}while(indexValue!=0);
			System.out.printf( "%-3s", " " + indexLetters);
			}
		}
		System.out.print("\n");
	}
	public void printToConsole(String comms) {
		System.out.print("\n" + comms);
	}
}