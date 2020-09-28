package com.TH;

public class MineSweeperMain {

	public static void main(String[] args) {
		View mSView = new View();
		Controller controller = new Controller(mSView);
		controller.initialize();
		while(controller.isRunning()) {
			controller.newTurn();
		}
		System.exit(1);
	}
}