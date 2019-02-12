package chess;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

public class King extends Piece {
	private int value = 100;
	private Color color;
	public King(Color c){
		super(c==Color.white? "WhiteKing.png" : "BlackKing.png");
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
		ArrayList<Location> posLocs = board.kingMove(this, loc, board);
		return posLocs;
	}


}
