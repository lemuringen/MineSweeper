package com.TH;

public class Cell {
	public enum Mode {
	    HIDDEN,
	    EXPOSED,
	    FLAGGED
	}
	public enum Type{
		EMPTY,
		MINE
	}
	private Mode mode;
	private Type type;
	private int adjacentMines;
	private int c;
	private int r;
	
	public Cell(Mode mode, Type type, int c, int r) {
		this.type = type;
		this.mode = mode;
		this.c = c;
		this.r = r;
		this.adjacentMines = 0;
	}
	public Cell(int c, int r) {
		this(Mode.HIDDEN, Type.EMPTY, c, r);
	}
	public int getAdjacentMines() {
		return this.adjacentMines;
	}
	public void setAdjacentMines(int adjacentMines) {
		this.adjacentMines = adjacentMines;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Type getType() {
		return this.type;
	}
	public Mode getMode() {
		return this.mode;
	}
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	public int getC() {
		return this.c;
	}
	public int getR() {
		return this.r;
	}
	public void setC(int c){
		this.c = c;
	}
	public void setR(int r) {
		this.r = r;
	}
}
