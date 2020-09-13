package com.TH;

public class MineSweeperMain {

	public static void main(String[] args) {
		View mSView = new View();
		Model mSModel = new Model(10, 12, 9);
		Controller mSController = new Controller(mSView, mSModel);
		mSController.initialize();
		while(mSController.isRunning()) {
			mSController.newTurn();
		}
	}
}