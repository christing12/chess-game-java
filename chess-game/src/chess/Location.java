package chess;

import chess.Location;

public class Location {
	int row;
	int col;
	public Location(int row, int col){
		this.row = row;
		this.col = col;
	}
	public int getrow(){
		return row;
	}
	public int getcol(){
		return col;
	}
	public String toString() {
		return Integer.toString(row) + " " + Integer.toString(col);
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Location)) return false;
		Location other = (Location) o;
		return this.row == other.row && this.col == other.col;
	}
}
