package chess;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

public class Queen extends Piece {
	private int value = 9;
	private Color color;
	public Queen(Color c){
		super(c==Color.white? "WhiteQueen.png" : "BlackQueen.png");
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
		ArrayList<Location> positions = board.queenMove(this, loc, board);
		/* for(int i = 0; i < 360; i += 45 ){
			positions.addAll(board.goStraight(loc, i));
		} */
		
		return positions;
	}


	

}
