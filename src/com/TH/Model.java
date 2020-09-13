package com.TH;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.TH.Cell.Mode;
/**<h1>Model class for MineSweeper2</h1>
 * Model is a data class for the game MineSweeper2. 
 * 
 * @author Lemu
 *
 */
public class Model {
private List<Cell> cellList;
private char[][] topographicMatrix;
private int boardSideX;
private int boardSideY;
private int mineDensity;
/**This constructor initialises a new empty list of Cell objects that represents the cells populating the
 * MineSweeper board as well as a empty char matrix that holds the visualisation of each cell state.
 * It takes in parameters to define the size of the game board and the proportion of mine containing cells.
 * 
 * @param boardSizeX	Horizontal size of game board.
 * @param boardSizeY	Vertical size of game board.
 * @param mineDensity	Proportion of mine containing cells.
 */
public Model(int boardSideX, int boardSideY, int mineDensity) {
	this.cellList = new ArrayList<Cell>();
	this.topographicMatrix = new char[boardSideY][boardSideX];
	this.boardSideX = boardSideX;
	this.boardSideY = boardSideY;
	this.mineDensity = mineDensity;
}
/** This constructor takes in parameters to define the size of the game board and the proportion of mine containing cells.
 * 	It's chained to another constructor that it passes all its parameters to.
 * 
 * @param boardSize		The side length of the game board. The game board will be quadratic.
 * @param mineDensity	Proportion of mine containing cells.
 */
public Model(int boardSide, int mineDensity){
	this(boardSide, boardSide, mineDensity);
}
//riskerar bli osymetriskt
public void addToCellList(Cell cell){
	this.cellList.add(cell);
}
public int getMineDensity() {
	return this.mineDensity;
}
public char[][] getTopographicMatrix(){
	return this.topographicMatrix;
}
/**
 * 
 * @param mineDensity percentual proportion of mine filled cells
 */
public void setMineDensity(int mineDensity) {
	
}
/**Getter method for the number of cells contained on the game board.
 * 
 * @return Number of cells on game board.
 */
public int getBoardSize() {
	return this.boardSideX * this.boardSideY;
}
public void setBoardSide(int boardSide) {
	this.boardSideX = boardSide;
	this.boardSideY = boardSide;
}
public void setBoardSideX(int boardSideX) {
	this.boardSideX = boardSideX;
}
public int getBoardSideX() {
	return this.boardSideX;
}
public int getBoardSideY() {
	return this.boardSideY;
}
public void setBoardSideY(int boardSideY) {
	this.boardSideY = boardSideY;
}
public void setTopographicMatrix(int r, int c, char top) {
	this.topographicMatrix[r][c] = top;
}

public List<Cell> getCellListCopy() {
	List<Cell> newCellList = new ArrayList<Cell>();
	for(int i = 0; i < this.cellList.size(); i++) {
		newCellList.add(cellList.get(i));
	}
	return newCellList;
}
public List<Cell> getCellList() {
	return this.cellList;
}

}
