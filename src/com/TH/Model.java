package com.TH;

import java.util.ArrayList;
import java.util.List;

/**<h1>Model class for MineSweeper2</h1>
 * Model is a data class for the game MineSweeper2. Everything contained is fully static.
 * 
 * @author Lemu
 *
 */
public class Model {
	/**
	 * PlayerDialogue contains all the String dialogue that the user sees during the game.
	 * @author Lemu
	 *
	 */
	public enum PlayerDialogue{
		INTRODUCTION("Welcome to MineSweeper 2! "),
		NEXT_MOVE("> "),
		INVALID_MOVE("Invalid move. "),
		PLAYER_WIN("Well done! "),
		PLAYER_LOOSE("Game over! "),
		INVALID_SYNTAX("Invalid syntax. "),
		NOT_ALLOWED("Not allowed. "),
		UNKNOWN_COMMAND("Unknown command. "),
		INPUT_WIDTH("How wide do you want the gameboard? Put in a width between 1 and 676. "),
		INPUT_HEIGHT("How high do you want the gameboard? Put in a height between 1 and 1000. "),
		INPUT_DENSITY("What percentual proportion of mine filled tiles do you want? Put in a proportion between 1 and 99. ");

		public final String dialogue;

		private PlayerDialogue(String dialogue) {
			this.dialogue = dialogue;
		}
	}
	/**
	 * cellList contains all the cells that make up the game board. It is ordered left to right, top to bottom, as they appear on the game board.
	 */
	private static List<Cell> cellList;
	/**
	 * gameBoard holds the visual state of all cells. Meant to be used by View class.
	 */
	private static char[][] gameBoard;
	/**
	 * The width in number of cells of the board.
	 */
	private static int boardSideX;
	/**
	 * The height in number of cells of the board.
	 */
	private static int boardSideY;
	/**
	 * The percentual proportion of cells of type Cell.Type.MINE 
	 */
	private static int mineDensity;

	private Model(){}
	/**Initialises all the dimensions, the lists for internal and external game state representation as well as the supposed proportion of mine holding cells.
	 * No mines placed yet though.
	 * 
	 * @param boardSideX The width in cells of the game board.
	 * @param boardSideY The height in cells of the game board.
	 * @param mineDensity The percentual proportion of cells with mines on them.
	 */
	public static void initModel(int boardSideX, int boardSideY, int mineDensity) {
		Model.cellList = new ArrayList<Cell>();
		Model.gameBoard = new char[boardSideY][boardSideX];
		Model.boardSideX = boardSideX;
		Model.boardSideY = boardSideY;
		Model.mineDensity = mineDensity;
	}
	/**Initialises all the dimensions, the lists for internal and external game state representation as well as the supposed proportion of mine holding cells.
	 * No mines placed yet though. The board will be quadratic.
	 * 
	 * @param boardSide The width and height in cells of the game board.
	 * @param mineDensity The percentual proportion of cells with mines on them.
	 */
	public static void initModel(int boardSide, int mineDensity) {
		initModel(boardSide, boardSide, mineDensity);
	}
	/**
	 * Get the percentual proportion of mine filled cells.
	 * @return Percentual proportion of mine filled cells.
	 */
	public static int getMineDensity() {
		return Model.mineDensity;
	}
	/**Set the density of mine filled cells.
	 * @param mineDensity percentual proportion of mine filled cells
	 */
	public static void setMineDensity(int mineDensity) {

	}
	/**Get the number of cells contained on the game board.
	 * 
	 * @return Number of cells on game board.
	 */
	public static int getBoardSize() {
		return boardSideX * boardSideY;
	}
	/**
	 * Get the width of the game board.
	 * @return The number of cells on the width of the game board.
	 */
	public static int getBoardSideX() {
		return boardSideX;
	}
	/**
	 * Get the height of the game board.
	 * @return The number of cell on the height of the board.
	 */
	public static int getBoardSideY() {
		return boardSideY;
	}
	/**
	 * Get the representation of the game board that player is supposed to be able to see. Char elements are ordered the same way that they are to be viewed.
	 * @return An char matrix holding all the char symbols that the player is supposed to see at any given time.
	 */
	public static char[][] getGameBoard(){
		return Model.gameBoard;
	}
	/**
	 * Set an element in the char matrix that is supposed to hold the viewable part of the game board.
	 * @param r Row index for the element placement. Top to bottom with increasing value.
	 * @param c Column index for the element placement. Left to right with increasing value.
	 * @param top The char symbol to be placed.
	 */
	public static void setGameBoard(int r, int c, char top) {
		Model.gameBoard[r][c] = top;
	}
	/**
	 * Add cell to the cell-list that represents the current state of the board. Cell is added last.
	 * @param cell Cell that is to be added to the game board.
	 */
	public static void addToCellList(Cell cell){
		Model.cellList.add(cell);
	}
	/**
	 * Get the list holding all the cells of the game board.
	 * @return A list with all the games cells.
	 */
	public static List<Cell> getCellList() {
		return Model.cellList;
	}
	/**Get cell at a certain index in the cell list.
	 * 
	 * @param row Row index of the cell to be returned. Top to bottom with increasing value.
	 * @param column Column index of the cell to be returned. Left to right with increasing value.
	 * @return
	 */
	public static Cell getCellAt(int row, int column) {
		return Model.cellList.get(row * Model.getBoardSideX() + column);
	}
}
