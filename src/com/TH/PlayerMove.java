package com.TH;

import com.TH.Cell.Mode;
/**
 * PlayerMove is used interperate the player commands during gameplay.
 * @author Lemu
 *
 */
public class PlayerMove {
	/**
	 * The row index of the cell the move concerns.
	 */
	private int rowIndex;
	/**
	 * The column index of the cell the move concerns.
	 */
	private int columnIndex;
	/**
	 * The enum type command of the move.
	 */
	private Command command;
	/**
	 * The enum type error mode of the move.
	 */
	private Error error = Error.NO_ERROR;
	/**
	 * Command contains the possible interaction types that the player has during gameplay. It is read as the first symbol of a player move.
	 */
	public enum Command{
		SWEEP,
		FLAG,
		QUIT,
	}
	/**
	 * Error contains the error types that a move can give.
	 * @author Lemu
	 *
	 */
	public enum Error{
		INVALID_MOVE,
		INVALID_SYNTAX,
		UNKNOWN_COMMAND,
		NO_ERROR
	}
	/**
	 * The constructor will check the input for errors. Only one error will register. Will not always initialise all variables.
	 * @param input The player move.
	 */
	public PlayerMove(String input) {
		if(!isValidSyntax(input)) {
			this.error = Error.INVALID_SYNTAX;
		}
		else if(!isKnownCommand(input)) {
			this.error = Error.UNKNOWN_COMMAND;
		}else {
			this.command = calculateCommand(input); // we now know that the command is valid
			if(this.command != Command.QUIT) {
				this.rowIndex = calculateRowIndex(input);
				this.columnIndex = calculateColumnIndex(input);
				if(isValidMove(this.columnIndex, this.rowIndex, this.command)) {
					error = Error.NO_ERROR;	// valid move
				}else {
					error = Error.INVALID_MOVE;	//invalid_move is checked last as it relies on knowing the coordinates from the player move.
				}
			}else {
				error = Error.NO_ERROR;	//quitting game
			}
		}
	}

	public int getRowIndex() {
		return this.rowIndex;
	}
	public int getColumnIndex() {
		return this.columnIndex;
	}
	public Error getError() {
		return this.error;
	}
	public Command getCommand() {
		return this.command;
	}

	/**
	 * Will search the input string for a group of consecutively ordered integers from the third index. The value their number make will
	 * be taken as the row index. It is dependent on having syntax validity checked beforehand.
	 * @param input The player move.
	 * @return Row index contained in the players move.
	 */
	private static int calculateRowIndex(String input) { 
		char[] inputChar = new char[input.length()];
		input.getChars(3, inputChar.length, inputChar, 0); // 3 should be first index to potentially hold a number
		int rowIndex = 0;
		for(int i = 0; i < inputChar.length ; i++) {
			if(isNumber(inputChar[i])) {
				rowIndex = rowIndex * 10 + inputChar[i]-48; // building the number from individual chars
			}
		}
		return rowIndex;
	}
	private static Command calculateCommand(String input) { // Match first letter in input with corresponding command. Command validity need to already be checked.
		switch(input.charAt(0)) {
		case 'R':
			return Command.SWEEP;
		case 'F':
			return Command.FLAG;
		case 'Q':
			return Command.QUIT;
		default: return null;	//TODO
		}
	}
	private static boolean isValidMove(int columnIndex, int rowIndex, Command command) {
		if(rowIndex < Model.getBoardSideY() && columnIndex < Model.getBoardSideX()) { //is the move within bounds
			if(Model.getCellAt(rowIndex, columnIndex).getMode() == Mode.EXPOSED && (command == Command.FLAG || command == Command.SWEEP)) { //are we trying to turn or flag an exposed cell
				return false;
			} 
			if(Model.getCellAt(rowIndex, columnIndex).getMode() == Mode.FLAGGED && command == Command.SWEEP) { // are we trying to turn a flagged cell
				return false;
			}
			return true; // valid move
		}else{
			return false;
		}
	}
	private static int calculateColumnIndex(String input) { // get the column index contained in the input, it is symbolised by letter
		char[] inputChar = new char[input.length()];
		input.getChars(2, inputChar.length, inputChar, 0);	// 2 should be first index to hold an index letter
		int columnIndex = 0;
		for(int i = 0; i < inputChar.length ; i++) {
			if(isCapitalLetter(inputChar[i])) {
				if(i == 0 && isCapitalLetter(inputChar[1])) {
					columnIndex = (inputChar[i]-65)+1;	//an ugly fix for bigger board than 26, indexes will advance as: A-..-Z-AA-..-AZ-..-YZ
				}else {
					columnIndex = columnIndex * 26 + inputChar[i]-65;	// 26 letters in english alphabet.
				}
			}
		}
		return columnIndex;
	}
	private static boolean isValidSyntax(String input) {
		char[] inputChar = new char[input.length()];
		input.getChars(0, inputChar.length, inputChar, 0);
		if(inputChar.length > 0 && isCapitalLetter(inputChar[0])){	//is input length 1 or more and is the first symbol in input a letter (letters are automatically capitalised.
			if(inputChar.length >= 4 && inputChar[1] == ' ' && isCapitalLetter(inputChar[2])) { //is input longer than 4(shortest possible move excl. quit) and 2nd symbol a blank space
				for(int i = 3; i < inputChar.length; i++) {
					if(isNumber(inputChar[i])) {
						for(int j = i; j <inputChar.length; j++) {
							if(!isNumber(inputChar[j])) {
								return false;	//Returns false if other character types than numbers at the end
							}
						}
						return true;	// Returns true when input is a letter followed by blank followed by at least one consecutive letter followed by at least one consecutive number
					}else if(!isCapitalLetter(inputChar[i])) {
						return false;	// Returns false when character is neither letter nor number
					}
				}			
			}else if(inputChar.length == 1) {
				return true;	// Returns true if only one letter (potentially a command)
			}
		}
		return false;	
	}

	private static boolean isKnownCommand(String input) {
		char command = input.charAt(0);
		return command == 'F' || command == 'R' || command == 'Q';
	}
	private static boolean isCapitalLetter(char letter) {
		return letter-65 >= 0 && letter <= 90;
	}
	private static boolean isNumber(char number) {
		return number-48 >= 0 && number <= 57;
	}
}
