package chess;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

public class Pawn extends Piece {
	private int value = 1;
	private int direction;
	private Color color;
	private boolean change = false;
	public Pawn(Color c){
		super(c==Color.white? "WhitePawn.png" : "BlackPawn.png");
		color = c;
		if(c.equals(Color.BLACK)){
			direction = -1;
		}
	}
	public boolean hasChanged() {
		return change;
	}
	public void setChange() {
		change = true;
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
		ArrayList<Location> positions = board.pawnMoves(this, loc, board);
		return positions;
	}
	

}
