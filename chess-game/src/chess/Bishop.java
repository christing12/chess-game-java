package chess;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

public class Bishop extends Piece {
	private int value = 3;
	private Color color;
	public Bishop(Color c){
		super(c==Color.white? "WhiteBishop.png" : "BlackBishop.png");
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
		ArrayList<Location> positions = board.bishopMoves(this, loc, board);
		return positions;
	}

}
