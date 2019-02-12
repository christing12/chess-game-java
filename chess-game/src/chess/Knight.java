package chess;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

public class Knight extends Piece {
	private int value = 3;
	private Color color;
	public Knight(Color c){
		super(c==Color.white? "WhiteKnight.png" : "BlackKnight.png");
		color = c;
	}
	@Override
	public int getValue() {
		return value;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public ArrayList<Location> moveLocs(Location loc, Board board) {
		ArrayList<Location> moveLocs = board.knightMove(loc, this);
		return moveLocs;
	}
}
