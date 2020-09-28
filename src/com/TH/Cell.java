package com.TH;

/** 
 * Cell class for MineSweeper2
 * <p>Cell class objects are the representation of the tiles that populate the game board of MineSweeper2. </p>
 * 
 * @author Lemu
 *
 */
public class Cell {
	/**
	 * Enum type Mode that tells you the different states that the cells can take when the user interacts with the game. Possible values: 
	 * HIDDEN, EXPOSED and FLAGGED.
	 * @author Lemu
	 *
	 */
	public enum Mode {
		HIDDEN,
		EXPOSED,
		FLAGGED
	}
	/**
	 * Enum type Type that tells whether the cell contains a mine or is empty. Possible values: EMPTY, MINE.
	 * @author Lemu
	 *
	 */
	public enum Type{
		EMPTY,
		MINE
	}
	/**
	 * Enum type that tells if a cell is HIDDEN, EXPOSED or FLAGGED.
	 */
	private Mode mode;
	/**
	 * Enum type that tells whether the cell contains a mine or not.
	 */
	private Type type;
	/**
	 * Int adjacentMines is meant to tell the number of adjacent cells that hold a mine.
	 */
	private int adjacentMines;
	/**
	 * Int c is an index for column placement of the cell on the board.
	 */
	private int c;
	/**
	 * Int c is an index for the row index placement of the cell on the board. r starts at zero at the uppermost side of the board and increases moving down.
	 */
	private int r;
	/**Cell constructor that initialises instance variables mode, type, c and r directly from parameters. Number of adjacent mines is set to 0.
	 * 
	 * @param mode The mode of the cell.
	 * @param type The type of cell.
	 * @param c Column index.
	 * @param r Row index.
	 */
	public Cell(Mode mode, Type type, int c, int r) {
		this.type = type;
		this.mode = mode;
		this.c = c;
		this.r = r;
		this.adjacentMines = 0;
	}
	/**
	 * Chained cell constructor that takes in c and r, and calls other constructor together with Type EMPTY and Mode HIDDEN.
	 * @param c Column index.
	 * @param r Row index.
	 */
	public Cell(int c, int r) {
		this(Mode.HIDDEN, Type.EMPTY, c, r);
	}
	/**Get amount of adjacent cells of MINE Type.
	 * 
	 * @return Number of adjacent cells of {@code MINE} Type
	 */
	public int getAdjacentMines() {
		return this.adjacentMines;
	}
	/**Set amount of adjacent cell of MINE Type.
	 * 
	 * @param adjacentMines Number of adjacent cells of MINE Type.
	 */
	public void setAdjacentMines(int adjacentMines) {
		this.adjacentMines = adjacentMines;
	}
	/**
	 * Set the cell Type.
	 * @param type  Set the type of cell.
	 */
	public void setType(Type type) {
		this.type = type;
	}
	/**
	 * Get the type of cell.
	 * @return The type of cell.
	 */
	public Type getType() {
		return this.type;
	}
	/**
	 * Get the mode of the cell.
	 * @return The mode of the cell.
	 */
	public Mode getMode() {
		return this.mode;
	}
	/**
	 * Set the mode of the cell.
	 * @param mode the mode of the cell.
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	/**
	 * Get column index of the cell.
	 * @return column index.
	 */
	public int getC() {
		return this.c;
	}
	/**
	 * Get the row index of the cell.
	 * @return the row index of the cell.
	 */
	public int getR() {
		return this.r;
	}
	/**
	 * Set the column index of the cell.
	 * @param c the column index of the cell.
	 */
	public void setC(int c){
		this.c = c;
	}
	/**
	 * Set the row index of the cell.
	 * @param r the row index of the cell.
	 */
	public void setR(int r) {
		this.r = r;
	}
}
