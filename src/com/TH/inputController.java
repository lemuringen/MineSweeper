package com.TH;

import java.util.Scanner;

class InputController {
private Scanner scanner;

InputController(){
	this.scanner = new Scanner(System.in);
}

public PlayerMove getPlayerMove(String input) {
	if(input.length() >= 3 && isNumber(input.charAt(2))) {
		return new PlayerMove((input.charAt(1)-48) * 10 + (input.charAt(2)-48), input.charAt(0), input.endsWith("F"));
	}else {
		return new PlayerMove(input.charAt(1)-48, input.charAt(0), input.endsWith("F"));
	}
}

public String getInput() {
	String input = scanner.nextLine();
	return input.trim().toUpperCase();
}
/**First two chars will always be of the same types: [LETTER][NUMBER]. Further conditions are checked for other lengths
 * TODO KOLLAR INTE SÅ ATT INPUT MATCHAR BOARDSIZE
 * @param move
 * @return
 */
public boolean isValidInput(String move) {
	if(move.length() >= 2 && move.length() <= 4 ) {
		if(isCapitalLetter(move.charAt(0)) && isNumber(move.charAt(1)) ) {
			if(move.length() == 2) {
				return true;
			}
			else if(move.length() == 3 ) {
				if(move.charAt(2) == 'F' || isNumber(move.charAt(2)) ) {
					return true;
				}
			}else if(move.length() == 4 && isNumber(move.charAt(2)) && move.charAt(3) == 'F') {
				return true;
			}
		}
	}else if(move.equals("q") || move.equals("h")) {
		return true;
	}
	return false;
}
private boolean isCapitalLetter(char letter) {
	return letter-65 >= 0 && letter <= 90;
}
private boolean isNumber(char number) {
	return number-48 >= 0 && number <= 57;
}
}
