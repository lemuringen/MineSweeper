package com.TH;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.TH.Cell.Mode;
import com.TH.Cell.Type;

public class Controller {
	private View view;
	private boolean isRunning;
	private boolean isVictoryConditionMet;
	private Scanner scanner;

	public Controller(View view) {
		this.view = view;
		this.scanner = new Scanner(System.in);
		this.isRunning = false;
		this.isVictoryConditionMet = false;
	}
	public void initialize() { //the model ready and render the board
		view.printToConsole(Model.PlayerDialogue.INTRODUCTION.dialogue);
		Model.initModel(getWidthFromPlayer(), getHeightFromPlayer(), getMineDensityFromPlayer()); // initialise the game constants
		fillCellList();	//fill list with cells and randomise mine placement
		calculateAdjacentMines(); //calculate how many mines that are adjacent to each cell, then let each cell know this
		fillTopographicList(); // fill the list holding the viewable game board
		view.updateView();	//render board
		this.isRunning = true;		
	}
	public void newTurn() {// called each new turn
		doPlayerMove(); //get and process player input
		view.updateView(); // new render after player input
		if(victoryConditionsMet(Model.getCellList())) { // if game is won congratulate and quit
			this.isVictoryConditionMet = true;
			this.isRunning = false;
			view.printToConsole(Model.PlayerDialogue.PLAYER_WIN.dialogue);
		}else if(this.isRunning == false) { // if game lost say so and quit
			turnBoard(); //expose whole board
			view.updateView();
			view.printToConsole(Model.PlayerDialogue.PLAYER_LOOSE.dialogue);
		}
	}
	public int getWidthFromPlayer() { // how wide do we want the board
		int width;
		view.printToConsole(Model.PlayerDialogue.INPUT_WIDTH.dialogue);
		view.printToConsole(Model.PlayerDialogue.NEXT_MOVE.dialogue);		
		width = this.scanner.nextInt();
		scanner.nextLine();	//empty scanner
		if(width <= 0 || width > 676) { //width within limits?
			view.printToConsole(Model.PlayerDialogue.NOT_ALLOWED.dialogue);		
			return getWidthFromPlayer(); // if not try again recursively
		}else {
			return width;
		}	
	}
	public int getHeightFromPlayer() {
		int height;
		view.printToConsole(Model.PlayerDialogue.INPUT_HEIGHT.dialogue);
		view.printToConsole(Model.PlayerDialogue.NEXT_MOVE.dialogue);
		height = this.scanner.nextInt();
		scanner.nextLine();
		if(height <= 0 || height > 1000) {
			view.printToConsole(Model.PlayerDialogue.NOT_ALLOWED.dialogue);	
			return getHeightFromPlayer();
		}else{
			return height;
		}
	}
	public int getMineDensityFromPlayer() {
		int mineDensity;
		view.printToConsole(Model.PlayerDialogue.INPUT_DENSITY.dialogue);
		view.printToConsole(Model.PlayerDialogue.NEXT_MOVE.dialogue);
		mineDensity = this.scanner.nextInt();
		scanner.nextLine();
		if(mineDensity <= 0 || mineDensity >= 100) {
			view.printToConsole(Model.PlayerDialogue.NOT_ALLOWED.dialogue);	
			return getMineDensityFromPlayer();
		}else {
			return mineDensity;
		}
	}

	public String getInput() {
		String input = scanner.nextLine();
		return input.toUpperCase(); //easier to work with only upper case
	}

	private void doPlayerMove() {
		view.printToConsole(Model.PlayerDialogue.NEXT_MOVE.dialogue);
		PlayerMove playerMove;
		do{ 
			playerMove = new PlayerMove(getInput());
			switch (playerMove.getError()) {
			case INVALID_MOVE:
				view.printToConsole(Model.PlayerDialogue.INVALID_MOVE.dialogue);
				break;
			case INVALID_SYNTAX:
				view.printToConsole(Model.PlayerDialogue.INVALID_SYNTAX.dialogue);
				break;
			case UNKNOWN_COMMAND:
				view.printToConsole(Model.PlayerDialogue.UNKNOWN_COMMAND.dialogue);
				break;
			default: break;
			}
		}while(playerMove.getError() != PlayerMove.Error.NO_ERROR); // take input until we get a PlayerMove without errors(Error.NO_ERROR)

		if(playerMove.getCommand() == PlayerMove.Command.QUIT) {
			this.isRunning = false;
		}else {
			int index = matrixIndexToListIndex(playerMove.getRowIndex(), playerMove.getColumnIndex(), Model.getBoardSideX()); //translate between matrix and 1dimensional list
			activateCell(playerMove.getRowIndex(),playerMove.getColumnIndex(), playerMove.getCommand() == PlayerMove.Command.FLAG); // sweep or flag at given coordinates
			if(checkIfLoosingMove(Model.getCellList().get(index))) { // did we hit a mine?
				this.isRunning = false;
			}	
		}
	}
	private boolean checkIfLoosingMove(Cell activatedCell) {
		if(activatedCell.getType() == Type.MINE && activatedCell.getMode() == Mode.EXPOSED) {
			return true;
		}else {
			return false;
		}
	}
	private boolean victoryConditionsMet(List<Cell> cellList) {
		for(int i = 0; i < cellList.size(); i++) {
			if(cellList.get(i).getType() == Type.EMPTY && cellList.get(i).getMode() == Mode.HIDDEN) {
				return false;
			}
		}
		return true;
	}
	public boolean isRunning() {
		return this.isRunning;
	}
	public boolean isWon() {
		return this.isVictoryConditionMet;
	}
	private void fillCellList() {
		Cell cell;
		for(int r = 0; r < Model.getBoardSideY(); r++) {
			for(int c = 0; c < Model.getBoardSideX(); c++) {
				cell = new Cell(c,r);
				setRandomType(cell);
				Model.addToCellList(cell);
			}		
		}
	}

	private void setRandomType(Cell cell) {
		Random rand = new Random();

		if(Model.getMineDensity()-rand.nextInt(100) > 0) {
			cell.setType(Type.MINE);
		}else {
			cell.setType(Type.EMPTY);
		}
	}
	/**Sets cell to flagged if hidden, sets hidden if flagged, does nothing if exposed.
	 * Updates GameBoard for View.
	 * 
	 * @param index The index of the cell to be activated.
	 * @param settingFlag True if flag is to be set, false if not.
	 */
	private void activateCell(int index, boolean settingFlag) {
		Cell cell = Model.getCellList().get(index);
		if(cell.getMode() != Mode.EXPOSED) { //does nothing if exposed (this should already be checked)
			if(settingFlag) { //toggle flag
				if(cell.getMode() == Mode.FLAGGED) {
					cell.setMode(Mode.HIDDEN);
					Model.getGameBoard()[cell.getR()][cell.getC()] = '-';
				}else if(cell.getMode() == Mode.HIDDEN) {
					cell.setMode(Mode.FLAGGED);
					Model.getGameBoard()[cell.getR()][cell.getC()] = 'F';
				}
			}else if(!settingFlag && cell.getType() == Type.MINE){
				cell.setMode(Mode.EXPOSED);
				if(this.isRunning) { //If player sweepes mine
					Model.getGameBoard()[cell.getR()][cell.getC()] = 'M';
				}else { // for when turning board after game over, probably not a good solution
					Model.getGameBoard()[cell.getR()][cell.getC()] = 'm';
				}
			}else if(!settingFlag && cell.getType() == Type.EMPTY) { //turn empty cell
				cell.setMode(Mode.EXPOSED);
				Model.getGameBoard()[cell.getR()][cell.getC()] = (char)(cell.getAdjacentMines()+48);
				if(cell.getAdjacentMines() == 0) { //turn all adjacent cells to cell with zero adjacent mines
					Model.getGameBoard()[cell.getR()][cell.getC()] = '.';
					exposeAllSafeAdjacentCells(index);
				}else {
					Model.getGameBoard()[cell.getR()][cell.getC()] = (char)(cell.getAdjacentMines()+48);
				}
			}
		}	
	}
	private void activateCell(int row, int column, boolean settingFlag) {
		activateCell(row * Model.getBoardSideX() + column, settingFlag); // translate 2d list index to 1d
	}
	
	private void fillTopographicList() { //Fills the list with player viewable content. Adjacent values should be set before executing.
		List<Cell> cellList = Model.getCellList();
		Cell cell;
		for(int r = 0; r < Model.getBoardSideY(); r++) {
			for(int c = 0; c < Model.getBoardSideX(); c++) {
				cell = cellList.get(matrixIndexToListIndex(r, c, Model.getBoardSideX()));	
				switch(cell.getMode()) {
				case HIDDEN:
					Model.setGameBoard(r, c, '-');
					break;
				case EXPOSED:
					switch(cell.getType()) {
					case EMPTY:
						Model.setGameBoard(r, c, (char)(cell.getAdjacentMines()+48));
						break;
					case MINE:
						Model.setGameBoard(r, c, 'X');
						break;
					}
				case FLAGGED:
					Model.setGameBoard(r, c, 'F');
					break;
				}
			}
		}
	}
	private int matrixIndexToListIndex(int r, int c, int width) {
		return r*width+c;
	}
	private List<Cell> getAllAdjacentCells(Cell cell){ // return list with all cells adjacent to a cell, diagonally, horizontally, vertically
		List<Cell> adjacentCells = new ArrayList<Cell>();
		List<Cell> cellList = Model.getCellList();
		int boardSideX = Model.getBoardSideX();
		int index = matrixIndexToListIndex(cell.getR(), cell.getC(), boardSideX);

		// we are using a 1-dimensional list in to represent an matrix. We use modulus to check if we are at the outer columns.
		if(index-(boardSideX+1) >= 0 && index % boardSideX != 0) { //We want the cell one up and one to the left. First we check that we are at least one step in to second row... 
			adjacentCells.add(cellList.get(index-(boardSideX+1)));//...Next we check that we are not on the leftmost column with modulus.
		}
		if(index-boardSideX >= 0) {
			adjacentCells.add(cellList.get(index-boardSideX));
		}
		if(index-(boardSideX-1) >= 0 && index%boardSideX != boardSideX - 1) { //here we use modulus to check if we are the rightmost column
			adjacentCells.add(cellList.get(index-(boardSideX-1)));
		}
		if(index%boardSideX != 0) {
			adjacentCells.add(cellList.get(index-1));
		}
		if(index%boardSideX != boardSideX - 1) {
			adjacentCells.add(cellList.get(index+1));
		}
		if(index+(boardSideX-1) < cellList.size() && index % boardSideX != 0) {
			adjacentCells.add(cellList.get(index+(boardSideX-1)));
		}
		if(index+boardSideX < cellList.size()) {
			adjacentCells.add(cellList.get(index + boardSideX));
		}
		if(index+(boardSideX+1) < cellList.size() && index % boardSideX != boardSideX - 1) {
			adjacentCells.add(cellList.get(index+boardSideX+1));
		}	
		return adjacentCells;

	}
	public void turnBoard() { // expose all unexposed cells
		List<Cell> cellList = Model.getCellList();
		for(int i = 0; i < cellList.size(); i++) {
			activateCell(i, false);
		}
	}
	private void calculateAdjacentMines() {
		List<Cell> cellList = Model.getCellList();
		List<Cell> adjacentCells;
		Cell cell;
		int adjacentMines;
		for(int i = 0; i < cellList.size(); i++) {
			adjacentMines = 0;
			cell = cellList.get(i);
			adjacentCells = getAllAdjacentCells(cell);
			for(int j = 0; j < adjacentCells.size(); j++) {
				if(adjacentCells.get(j).getType() == Type.MINE) {
					adjacentMines++;
				}
			}
			cell.setAdjacentMines(adjacentMines);
		}			
	}
	private void exposeAllSafeAdjacentCells(int index) {
		List<Cell> cellList = Model.getCellList();
		List<Cell> adjacentCells = getAllAdjacentCells(cellList.get(index));
		Cell cell;
		int adjacentCellIndex;
		for(int i = 0; i < adjacentCells.size(); i++) {
			cell = adjacentCells.get(i);
			adjacentCellIndex = matrixIndexToListIndex(cell.getR(), cell.getC(), Model.getBoardSideX());
			activateCell(adjacentCellIndex, false); //expose all adjacent cells. If any of the cells have no adjacent mines, this activateCell() will call this method again creating a chainreaction
		}							
	}

}
