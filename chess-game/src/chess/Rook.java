package chess;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

public class Rook extends Piece {
	private int value = 5;
	private Color color;
	public Rook(Color c){
		super(c==Color.white? "WhiteRook.png" : "BlackRook.png");
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
		ArrayList<Location> positions = board.rookMove(this, loc, board);
		/* for(int i = 0; i < 360; i += 90){
			positions.addAll(board.goStraight(loc, i));
		} */
		return positions; 
	}

}
