package com.TH;

public class PlayerMove {
	private int rowIndex;
	private int columnIndex;
	private boolean settingFlag;
	
	public PlayerMove(int rowIndex, int columnIndex, boolean settingFlag) {
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		this.settingFlag = settingFlag;
	}
	public PlayerMove(int rowIndex, char columnIndex, boolean settingFlag) {
		this(rowIndex, columnIndex-65, settingFlag);
	}
	public int getRowIndex() {
		return this.rowIndex;
	}
	public int getColumnIndex() {
		return this.columnIndex;
	}
	public boolean isSettingFlag() {
		return this.settingFlag;
	}
}
