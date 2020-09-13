package com.TH;

public class View {
	char[][] topographicMatrix;

	public void initializeView(char[][] topographicMatrix) {
		this.topographicMatrix = topographicMatrix;
	}
	/**This method prints out the MineSweeper board to the console, showing
	 * bricks as turned, unturned or flagged depending on inputs from the player
	 * and the mines randomly populating the board. Formating is done to visually
	 * co-relate indexes with intended bricks. Currently supports 1-2 numbered 
	 * indexes and 1 lettered index.
	 * 
	 */
	public void updateView() {
		System.out.println();
		System.out.printf("%6s", "");
		for(int i = 0; i < topographicMatrix[0].length; i++) {
			System.out.print(" " + (char)(i+65) + " ");
		}
		System.out.print("\n");
		System.out.printf("%5s", "+");
		for(int i = 0; i < topographicMatrix[0].length*3; i++) {
			System.out.print("-");
		}
		System.out.print("\n");
		for(int r = 0; r < topographicMatrix.length; r++) {
			System.out.printf("%3s", r);
			System.out.print(" | ");
			for(int c = 0; c < topographicMatrix[0].length; c++) {
				//System.out.print(" " + topographicMatrix[r][c] + " ");
				System.out.printf("%-3s", (" " + topographicMatrix[r][c]));
				
			}
			System.out.print("\n");
		}

	}
	public void printToConsole(String comms) {
		System.out.print("\n" + comms);
	}
}