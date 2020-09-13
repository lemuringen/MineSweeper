package com.TH;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.TH.Cell.Mode;
import com.TH.Cell.Type;

public class Controller {
	private View mSView;
	private Model mSModel;
	private InputController inputController;
	private boolean isRunning;
	private boolean isVictoryConditionMet;
	
	public enum PlayerDialogue{
		INTRODUCTION,
		NEXT_MOVE,
		INVALID_MOVE,
		PLAYER_WIN,
		PLAYER_LOOSE
	}
	
	public Controller(View view, Model model) {
	this.mSView = view;
	this.mSModel = model;
	this.inputController = new InputController();
	this.isRunning = false;
	this.isVictoryConditionMet = false;
	}
	public void initialize() {
		fillCellList();
		calculateAdjacentMines();
		filltopographicList();
		mSView.initializeView(mSModel.getTopographicMatrix());
		mSView.updateView();
		this.isRunning = true;
		mSView.printToConsole(playerDialogue(PlayerDialogue.INTRODUCTION));
	}
	public void newTurn() {
		doPlayerMove();
		mSView.updateView();
		if(victoryConditionsMet(mSModel.getCellList())) {
			this.isVictoryConditionMet = true;
			this.isRunning = false;
			mSView.printToConsole(playerDialogue(PlayerDialogue.PLAYER_WIN));
		}else if(this.isRunning == false) {
			turnBoard();
			mSView.updateView();
			mSView.printToConsole(playerDialogue(PlayerDialogue.PLAYER_LOOSE));
		}
	}
	public String playerDialogue(PlayerDialogue playerDialogue) {
		switch(playerDialogue) {
			case INTRODUCTION:
				return "Welcome to MineSweeper 2!";
			case NEXT_MOVE:
				return "Type in the column index (represented by letters) followed by the row index (represented by numbers) to activate a brick."
						+ "\n End with an 'F' if you want to flag the brick. Your input: ";
			case INVALID_MOVE:
				return "Invalid move. Try again: ";
			case PLAYER_WIN:
				return "Congratulations! You won.";
			case PLAYER_LOOSE:
				return "You loose!";
			default: return "Application error, unexpected parameter value";
		}
	}
	/**
	 * Should this logic really be in this class? alternatives?
	 */
	private void doPlayerMove() {
		mSView.printToConsole(playerDialogue(PlayerDialogue.NEXT_MOVE));
		String input = inputController.getInput();
		
		while(!inputController.isValidInput(input)) {
			mSView.printToConsole(playerDialogue(PlayerDialogue.INVALID_MOVE));
			input = inputController.getInput();
		}
		
		PlayerMove playerMove = inputController.getPlayerMove(input);
		int index;	
		index = playerMove.getRowIndex() * mSModel.getBoardSideX() + playerMove.getColumnIndex();
		activateCell(index, playerMove.isSettingFlag());
		if(checkIfLoosingMove(mSModel.getCellList().get(index))) {
			this.isRunning = false;
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
		for(int r = 0; r < mSModel.getBoardSideY(); r++) {
			for(int c = 0; c < mSModel.getBoardSideX(); c++) {
				cell = new Cell(c,r);
				setRandomType(cell);
				this.mSModel.addToCellList(cell);
			}		
		}
	}

	private void setRandomType(Cell cell) {
		Random rand = new Random();
		
		if(this.mSModel.getMineDensity()-rand.nextInt(100) > 0) {
			cell.setType(Type.MINE);
		}else {
			cell.setType(Type.EMPTY);
		}
	}
	/**Sets flagged if hidden, sets hidden if flagged,
	 * does nothing if exposed
	 * updated topographicMatrix for View (maybe should be separated?)
	 * 
	 * @param index
	 * @param settingFlag
	 */
	private void activateCell(int index, boolean settingFlag) {
		Cell cell = mSModel.getCellList().get(index);
		char[][] topographicMatrix = mSModel.getTopographicMatrix();
		if(cell.getMode() != Mode.EXPOSED) {
			if(settingFlag) {
				if(cell.getMode() == Mode.FLAGGED) {
					cell.setMode(Mode.HIDDEN);
					topographicMatrix[cell.getR()][cell.getC()] = '-';
				}else if(cell.getMode() == Mode.HIDDEN) {
					cell.setMode(Mode.FLAGGED);
					topographicMatrix[cell.getR()][cell.getC()] = 'F';
				}
				// maybe unneccesary to check for type 
			}else if(!settingFlag && cell.getType() == Type.MINE){
				cell.setMode(Mode.EXPOSED);
				if(this.isRunning) {
					topographicMatrix[cell.getR()][cell.getC()] = 'M';
				}else {
					topographicMatrix[cell.getR()][cell.getC()] = 'm';
				}
			}else if(!settingFlag && cell.getType() == Type.EMPTY) {
				cell.setMode(Mode.EXPOSED);
				topographicMatrix[cell.getR()][cell.getC()] = (char)(cell.getAdjacentMines()+48);
				if(cell.getAdjacentMines() == 0) {
					topographicMatrix[cell.getR()][cell.getC()] = '.';
					exposeAllSafeAdjacentCells(index);
				}else {
					topographicMatrix[cell.getR()][cell.getC()] = (char)(cell.getAdjacentMines()+48);
				}
			}
		}	
		//mSModel.setTopographicList(index, top);
	}
	//adjacent values must be set before executing
	private void filltopographicList() {
		List<Cell> cellList = mSModel.getCellList();
		Cell cell;
		for(int r = 0; r < mSModel.getBoardSideY(); r++) {
			for(int c = 0; c < mSModel.getBoardSideX(); c++) {
				cell = cellList.get(matrixIndexToListIndex(r, c, mSModel.getBoardSideX()));	
					switch(cell.getMode()) {
						case HIDDEN:
							mSModel.setTopographicMatrix(r, c, '-');
							break;
						case EXPOSED:
							switch(cell.getType()) {
								case EMPTY:
									mSModel.setTopographicMatrix(r, c, (char)(cell.getAdjacentMines()+48));
									break;
								case MINE:
									mSModel.setTopographicMatrix(r, c, 'X');
									break;
							}
						case FLAGGED:
							mSModel.setTopographicMatrix(r, c, 'F');
							break;
					}
			}
		}
	}
	private int matrixIndexToListIndex(int r, int c, int width) {
		return r*width+c;
	}
	private List<Cell> getAllAdjacentCells(Cell cell){
		List<Cell> adjacentCells = new ArrayList<Cell>();
		List<Cell> cellList = mSModel.getCellList();
		int boardSideX = mSModel.getBoardSideX();
		int index = matrixIndexToListIndex(cell.getR(), cell.getC(), boardSideX);
			
				// vi kollar så att det vi inte ligger på ytterkolumnerna med modulus
		if(index-(boardSideX+1) >= 0 && index % boardSideX != 0) {
			adjacentCells.add(cellList.get(index-(boardSideX+1)));
		}
		if(index-boardSideX >= 0) {
			adjacentCells.add(cellList.get(index-boardSideX));
		}
		if(index-(boardSideX-1) >= 0 && index%boardSideX != boardSideX - 1) {
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
	public void turnBoard() {
		List<Cell> cellList = mSModel.getCellList();
		for(int i = 0; i < cellList.size(); i++) {
			activateCell(i, false);
		}
	}
	private void calculateAdjacentMines() {
		List<Cell> cellList = mSModel.getCellList();
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
		List<Cell> cellList = mSModel.getCellList();
		List<Cell> adjacentCells = getAllAdjacentCells(cellList.get(index));
		Cell cell;
		int adjacentCellIndex;
		for(int i = 0; i < adjacentCells.size(); i++) {
			cell = adjacentCells.get(i);
			adjacentCellIndex = matrixIndexToListIndex(cell.getR(), cell.getC(), mSModel.getBoardSideX());
			activateCell(adjacentCellIndex, false);
		}							
	}

}
