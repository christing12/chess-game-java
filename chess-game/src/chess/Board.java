package chess;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;


import javax.swing.JPanel;

public class Board extends JPanel implements MouseListener, MouseMotionListener{
	private double width;
	private double height;
	private double squareSize;
	private static int mouseX, mouseY, newMouseX, newMouseY;
	private static GameState gameState = GameState.WHITE_TURN;
	public ArrayList<Location> selectedLocs = new ArrayList<Location>();
	public Location selectedPiece;
	Piece[][] board = new Piece[8][8];
	private Location selectedLocation;
	private int xAnchor, yAnchor;
	private int xDrag, yDrag;
	public Board(){
		super();
		reset();
		selectedLocation = null;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);


	}
	public Piece[][] getBoard() {
		return board;
	}

	public void reset(){
		board[0][0] = new Rook(Color.BLACK);
		board[0][1] = new Knight(Color.BLACK);
		board[0][2] = new Bishop(Color.BLACK);
		board[0][3] = new Queen(Color.BLACK);
		board[0][4] = new King(Color.BLACK);
		board[0][5] = new Bishop(Color.BLACK);
		board[0][6] = new Knight(Color.BLACK);
		board[0][7] = new Rook(Color.BLACK);
		for(int i = 0; i < 8; i++){
			board[1][i] = new Pawn(Color.BLACK);
		}

		for(int j = 2; j < 6; j++){
			for(int i = 0; i < 8; i++){
				board[j][i] = null;
			}
		}

		for(int i = 0; i < 8; i++){
			board[6][i] = new Pawn(Color.WHITE);
		}
		board[7][0] = new Rook(Color.WHITE);
		board[7][1] = new Knight(Color.WHITE);
		board[7][2] = new Bishop(Color.WHITE);
		board[7][4] = new King(Color.WHITE);
		board[7][3] = new Queen(Color.WHITE);
		board[7][5] = new Bishop(Color.WHITE);
		board[7][6] = new Knight(Color.WHITE);
		board[7][7] = new Rook(Color.WHITE);
	}
	public void paint(Graphics g) {
		width = getWidth();
		height = getHeight();
		squareSize = Math.min(width, height)/8.0;

		Graphics2D g2d = (Graphics2D) g;
		//if(getState() == GameState.BLACK_TURN || getState() == GameState.WHITE_TURN) {
			paintBoard(g2d);
			//testing
			if(selectedLocation != null) {
				ArrayList<Location> selectMoveLocs = getPiece(selectedLocation).moveLocs(selectedLocation, this);
				for (Location l : selectMoveLocs) {
					if(theoreticalCheck(selectedLocation.col, selectedLocation.row, l.col, l.row)) {
						g2d.setColor(Color.decode("#98fb98"));
						g2d.fillRect((int) (l.getcol()*squareSize), (int) (l.getrow()*squareSize), (int) squareSize, (int) squareSize);

					}
				} 
			}

			paintPieces(g2d); 
		//}
		if(getState() == GameState.GAME_OVER) {
			g2d.setColor(new Color(0, 0, 0, 100));
			FontMetrics metric = g2d.getFontMetrics();
			g2d.fillRect(0, 0, getWidth(), getHeight());
			g2d.setColor(Color.white);
			g2d.fillRect(getWidth()/2 , getHeight()/2 - metric.getHeight(), metric.stringWidth("GAME OVER"), metric.getHeight());
			g2d.setColor(Color.black);
			g2d.drawString("GAME OVER", getWidth() / 2, getHeight() / 2);
		}
		

	}
	public boolean isCheckMate(Color c) {
		ArrayList<Location> teamMoves = new ArrayList<Location>();
		ArrayList<Location> teamLocs = getTeam(c);
		for(Location l : teamLocs) {
			ArrayList<Location> pieceLocs = getPiece(l).moveLocs(l, this);
			for(Location loc : pieceLocs) {
				if(theoreticalCheck(l.col, l.row, loc.col, loc.row)) {
					return false;
				}
			}
			//teamMoves.addAll(pieceLocs);

		}
		return true;
		
	}
	public void paintBoard(Graphics2D g) {
		boolean isBlack = false;
		for(int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col ++) {
				if(isBlack) {
					g.setColor(Color.lightGray);
					g.fillRect((int) (col*squareSize), (int) (row*squareSize), (int) squareSize, (int) squareSize);
				}
				else {
					g.setColor(Color.white);
					g.fillRect((int) (col*squareSize), (int) (row*squareSize), (int) squareSize, (int) squareSize);
				}
				isBlack = !isBlack;
			}
			isBlack = !isBlack;
		}

	}
	public void paintPieces(Graphics2D g) {
		for(int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col ++) {
				if(board[row][col] == null) {
					continue;
				}
				else {
					Image i = board[row][col].getImage();
					if (i == null) {
						System.out.println("meh!");
						continue;
					}
					else {
						AffineTransform trans = new AffineTransform();
						if(board[row][col].isSelected()) {
							trans.translate(xDrag, yDrag);
						}
						trans.translate((col*squareSize), (row*squareSize));
						trans.scale(squareSize/i.getWidth(null), squareSize/i.getHeight(null));
						g.drawImage(i, trans, null);
					}
				}
			}
		}

	}
	public ArrayList<Location> getTeamMoves(Color c) {
		ArrayList<Location> teamMoves = new ArrayList<Location>();
		ArrayList<Location> teamLocs = getTeam(c);
		for(Location l : teamLocs) {
			ArrayList<Location> pieceLocs = getPiece(l).moveLocs(l, this);
			teamMoves.addAll(pieceLocs);
		}
		return teamMoves;
	}
	public ArrayList<Location> getTeam(Color c) {
		ArrayList<Location> teamLocations = new ArrayList<Location>();
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[0].length; col++) {
				Location loc = new Location(row, col);
				if(!empty(loc)) {
					if(resident(loc, c) == 1) {
						teamLocations.add(loc);
					}
				}
			}
		}
		return teamLocations;
	}
	public Location getKing(Color c) {
		ArrayList<Location> teamLocs = getTeam(c);
		Location kingLoc = null;
		for(Location l : teamLocs) {
			if(getPiece(l).getValue() == 100) {
				kingLoc = new Location(l.getrow(), l.getcol());
			}
		}
		return kingLoc;
	}
	public Piece getPiece(Location loc){
		return board[loc.row][loc.col];
	}
	public void setPiece(Location loc, Piece piece){
		board[loc.row][loc.col] = piece;
	}
	public void movePiece(Location from, Location to){
		if(getPiece(from) instanceof Pawn) {
			getPiece(from).setChange();
		}
		setPiece(to, getPiece(from));
		setPiece(from, null);
		Color checkCheckmateColor = Color.black;
		if(getPiece(to).getColor().equals(Color.black)) {
			checkCheckmateColor = Color.white;
		}
		if(isCheckMate(checkCheckmateColor)) {
			gameOver();
			System.out.println("You suck");
		}
	}
	private void gameOver() {
		setState(GameState.GAME_OVER);
		repaint();
		
	}
	public Boolean valid(Location loc){
		return loc.row >= 0 && loc.row < 8 && loc.col >= 0 && loc.col < 8;
	}
	public Boolean empty(Location loc){
		return board[loc.row][loc.col] == null;
	}
	public ArrayList<Location> knightMove(Location loc, Piece p){
		ArrayList<Location> posLocs = new ArrayList<Location>();
		ArrayList<Location> finalLocs = new ArrayList<Location>();
		int row = loc.row;
		int col = loc.col;
		posLocs.add(new Location(row + 2, col + 1));
		posLocs.add(new Location(row - 2, col + 1));
		posLocs.add(new Location(row + 2, col - 1));
		posLocs.add(new Location(row - 2, col - 1));
		posLocs.add(new Location(row + 1, col + 2));
		posLocs.add(new Location(row + 1, col - 2));
		posLocs.add(new Location(row - 1, col + 2));
		posLocs.add(new Location(row - 1, col - 2));
		for(Location l : posLocs) {
			if(valid(l)) {
				if(resident(l, p.getColor()) == -1 || resident(l, p.getColor()) == 0) {
					finalLocs.add(l);
				}
			}
		}
		return finalLocs;
	}
	public ArrayList<Location> teamObstruction (Piece p, Location loc, Board b) {
		ArrayList<Location> completeList = queenMove(p, loc, b);
		for(Location l : completeList) {
			if(empty(l)) {
				continue;
			}
			if(b.getPiece(l).getColor().equals(p.getColor())) {
				completeList.remove(b.getPiece(l).getColor());
			}
		}
		/* ArrayList<Location> completeList = new ArrayList<Location>();
		// Location looker = new Location(loc.getrow(), loc.getcol());
		if(p.getValue() == 9) {
			completeList = queenMove(p, loc, b);
			for(Location l : completeList) {
				if(b.getPiece(l) != null) {
					completeList.remove(l);
				}
			}
		} */
		return completeList;
	}
	public int resident(Location loc, Color c) {
		if (empty(loc)) {
			return 0;
		}
		else if(getPiece(loc).getColor().equals(c)) {
			return 1;
		}
		else return -1;

	}
	public ArrayList<Location> rookMove(Piece p, Location loc, Board b) {
		ArrayList<Location> posMoves = new ArrayList<Location>();
		Location north = new Location(loc.getrow() - 1, loc.getcol());
		while(valid(north)) {
			if(resident(north, p.getColor()) == -1) {
				posMoves.add(north);
				break;
			}
			else if(resident(north, p.getColor()) == 1) {
				break;
			} 
			else {
				posMoves.add(north);
				north = new Location (north.getrow() - 1, north.getcol());
			}

		}
		Location east = new Location(loc.getrow(), loc.getcol() + 1);
		while(valid(east)) {
			if(resident(east, p.getColor()) == -1) {
				posMoves.add(east);
				break;
			}
			else if(resident(east, p.getColor()) == 1) {
				break;
			} 
			else {
				posMoves.add(east);
				east = new Location (east.getrow(), east.getcol() + 1);
			}

		} 
		Location west = new Location(loc.getrow(), loc.getcol() - 1);
		while(valid(west)) {
			if(resident(west, p.getColor()) == -1) {
				posMoves.add(west);
				break;
			}
			else if(resident(west, p.getColor()) == 1) {
				break;
			} 
			else {
				posMoves.add(west);
				west = new Location (west.getrow() , west.getcol() - 1);
			}

		}

		Location south = new Location(loc.getrow() + 1, loc.getcol());
		while(valid(south)) {
			if(resident(south, p.getColor()) == -1) {
				posMoves.add(south);
				break;
			}
			else if(resident(south, p.getColor()) == 1) {
				break;
			} 
			else {
				posMoves.add(south);
				south = new Location (south.getrow() + 1, south.getcol());
			}

		} 
		return posMoves;
	} 
	public ArrayList<Location> kingMove(Piece p, Location loc, Board b) {
		ArrayList<Location> posMoves = new ArrayList<Location>();
		for(int row = -1; row <= 1; row ++) {
			for (int col = -1; col <= 1; col++) {
				if(row == 0 && col == 0) {
					continue;
				}
				Location loca = new Location (loc.getrow() + row, loc.getcol() + col);
				if(valid(loca)) {
					if(resident(loca, p.getColor()) == -1 || resident(loca, p.getColor()) == 0) {
						posMoves.add(loca);
					}
				}
			}
		}
		return posMoves;
	}
	public ArrayList<Location> pawnMoves (Pawn p, Location loc, Board b) {
		ArrayList<Location> posMoves = new ArrayList<Location>();
		if(p.getColor().equals(Color.white)) {
			Location loc2 = new Location(loc.getrow() - 1, loc.getcol());
			if(valid(loc2)) {
				if(empty(loc2)){
					posMoves.add(loc2);
				}
			}
			if(!p.hasChanged()) {
				Location loca = new Location(loc.getrow() - 2, loc.getcol());
				if(valid(loca)) {
					if(empty(loca) && empty(loc2)) {
						posMoves.add(loca);
					}
				}
			}
			Location rightDiag = new Location(loc.getrow() - 1, loc.getcol() + 1);
			if(valid(rightDiag)) {
				if(resident(rightDiag, p.getColor()) == -1) {
					posMoves.add(rightDiag);
				}
			}
			Location leftDiag = new Location(loc.getrow() - 1, loc.getcol() -1);
			if(valid(leftDiag)) {
				if(resident(leftDiag, p.getColor()) == -1) {
					posMoves.add(leftDiag);
				}
			}
		}

		if(p.getColor().equals(Color.BLACK)) {
			Location loc2 = new Location(loc.getrow() + 1, loc.getcol());
			if(valid(loc2)) {
				if(empty(loc2)) {
					posMoves.add(loc2);
				}
			}
			if(!p.hasChanged()) {
				Location loca = new Location(loc.getrow() + 2, loc.getcol());
				if(valid(loca)) {
					if(empty(loca) && empty(loc2)) {
						posMoves.add(loca);
					}
				}
			}
			Location rightDiag = new Location(loc.getrow() + 1, loc.getcol() + 1);
			if(valid(rightDiag)) {
				if(resident(rightDiag, p.getColor()) == -1) {
					posMoves.add(rightDiag);
				}
			}
			Location leftDiag = new Location(loc.getrow() + 1, loc.getcol() -1);
			if(valid(leftDiag)) {
				if(resident(leftDiag, p.getColor()) == -1) {
					posMoves.add(leftDiag);
				}
			}
		}
		return posMoves;
	}
	public ArrayList<Location> bishopMoves (Piece p, Location loc, Board b) {
		ArrayList<Location> posMoves = new ArrayList<Location>();
		Location northwest = new Location(loc.getrow() - 1, loc.getcol() - 1);
		while(valid(northwest)) {
			if(resident(northwest, p.getColor()) == -1) {
				posMoves.add(northwest);
				break;
			}
			else if(resident(northwest, p.getColor()) == 1) {
				break;
			}
			else {
				posMoves.add(northwest);
				northwest = new Location (northwest.getrow() - 1, northwest.getcol() - 1);
			}
		}
		Location northeast = new Location(loc.getrow() - 1, loc.getcol() + 1);
		while(valid(northeast)) {
			if(resident(northeast, p.getColor()) == -1) {
				posMoves.add(northeast);
				break;
			}
			else if(resident(northeast, p.getColor()) == 1) {
				break;
			}
			else {
				posMoves.add(northeast);
				northeast = new Location (northeast.getrow() - 1, northeast.getcol() + 1);
			}
		}
		Location southeast = new Location(loc.getrow() + 1, loc.getcol() + 1);
		while(valid(southeast)) {
			if(resident(southeast, p.getColor()) == -1) {
				posMoves.add(southeast);
				break;
			}
			else if(resident(southeast, p.getColor()) == 1) {
				break;
			}
			else {
				posMoves.add(southeast);
				southeast = new Location (southeast.getrow() + 1, southeast.getcol() + 1);
			}
		}
		Location southwest = new Location(loc.getrow() + 1, loc.getcol() -1 );
		while(valid(southwest)) {
			if(resident(southwest, p.getColor()) == -1) {
				posMoves.add(southwest);
				break;
			}
			else if(resident(southwest, p.getColor()) == 1) {
				break;
			}
			else {
				posMoves.add(southwest);
				southwest = new Location (southwest.getrow() + 1, southwest.getcol() - 1);
			}
		}
		return posMoves;
	}
	public ArrayList<Location> queenMove(Piece p, Location loc, Board b) {
		ArrayList<Location> posMoves = new ArrayList<Location>();
		for(int row = -1; row <= 1; row ++) {
			for(int col = -1; col <= 1; col ++) {
				if(row == 0 && col == 0) {
					continue;
				}
				Location loca = new Location (loc.getrow() + row, loc.getcol() + col);
				while(valid(loca)) {
					if(resident(loca, p.getColor()) == -1) {
						posMoves.add(loca);
						break;
					}
					if(resident(loca, p.getColor()) == 1) {
						break;
					}
					posMoves.add(loca);
					loca = new Location (loca.getrow() + row, loca.getcol() + col);
				}

			}
		}
		return posMoves;
	}
	public boolean validGridPoint(int xCord, int yCord) {
		return(xCord < getWidth() && yCord < getHeight());
	}
	public boolean isChecked(King k, Location loc) {
		boolean kingCheck = pieceSafe(k, loc);
		return kingCheck;
	}
	public synchronized boolean theoreticalCheck(int xfrom, int yfrom, int xto, int yto) {
		Piece[][] temp = board;
		Piece[][] clone = new Piece[8][8];
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[row].length; col++) {
				clone[row][col] = board[row][col];
			}
		}
		Piece tempPiece = clone[yfrom][xfrom];
		clone[yfrom][xfrom] = null;
		clone[yto][xto] = tempPiece;
		this.board = clone;
		boolean isKingSafe = true;
		try{
			isKingSafe = checkForCheck(getPiece(new Location(yto, xto)).getColor());
		}
		catch (Exception e){
			this.board = temp;
			return false;
		}
		this.board = temp;
		return isKingSafe;
	}
	public boolean checkForCheck(Color c) {
		// position of the Colored c king;
		Location kingLoc = getKing(c);
		if(pieceSafe(getPiece(kingLoc), kingLoc)) {
			return true;
		}
		else {
			return false;
		}
	}
	public boolean pieceSafe(Piece p, Location loc) {
		ArrayList<Location> enemyMoveLocs = new ArrayList<Location>();
		if(p.getColor().equals(Color.white)) {
			enemyMoveLocs = getTeamMoves(Color.black);
			for(Location l : enemyMoveLocs) {
				if(l.equals(loc)) {
					return false;
				}
			}
		}
		if(p.getColor().equals(Color.black)) {
			enemyMoveLocs = getTeamMoves(Color.white);
			for(Location l : enemyMoveLocs) {
				if(l.equals(loc)) {
					return false;
				}
			}
		}
		return true;
	}
	public static GameState getState() {
		return gameState;
	}
	public static void setState(GameState state) {
		gameState = state;
	}
	@Override
	public void mouseClicked(MouseEvent e){
	}
	public void updateGame() {
		switch(this.getState()) {
		case WHITE_TURN:
				setState(GameState.BLACK_TURN);
				break;
		case BLACK_TURN:
				setState(GameState.WHITE_TURN);
				break;
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(validGridPoint(e.getX(), e.getY())) {
			xAnchor = e.getX();
			yAnchor = e.getY();
			mouseX = (int) (e.getX()/squareSize);
			mouseY = (int) (e.getY()/squareSize);
			Location pressedLocation = new Location(mouseY, mouseX);
			Piece p = getPiece(pressedLocation);
			if(p == null) {
				return;
			}
			if (gameState==GameState.WHITE_TURN && p.getColor().equals(Color.white)
					||
					gameState==GameState.BLACK_TURN && p.getColor().equals(Color.black))
			{
				selectedLocation = pressedLocation;
				p.setSelected(true);
				repaint();
			}
		} 

	}
	@Override
	public void mouseReleased(MouseEvent e) {
		Piece p;
		Location position;
		ArrayList<Location> movers = new ArrayList<Location>();
		if(selectedLocation == null) {
			return;
		}

		if(validGridPoint(e.getX(), e.getY())) {
			newMouseX = e.getX();
			newMouseY = e.getY();
			position = new Location((int)(newMouseY / squareSize), (int)(newMouseX / squareSize));
			if(valid(position)) {
				if(!empty(selectedLocation)){
					getPiece(selectedLocation).setSelected(false);
					xDrag = 0;
					yDrag = 0;
					p = this.getPiece(selectedLocation);
					movers = p.moveLocs(selectedLocation, this);
					for (int i = 0; i < movers.size(); i++) {
						if(!theoreticalCheck(selectedLocation.col, selectedLocation.row, position.col, position.row)) {
							movers.remove(i);
							i--;
						}
					}
					if(movers.contains(position)) {
						movePiece(selectedLocation, position);
						updateGame();
						repaint();
					}
				}
			}
			repaint();
		}
		selectedLocation = null;

	}
	public void safetynet() {
		/*
		Piece p;
		Location position;
		ArrayList<Location> movers = new ArrayList<Location>();
		if(validGridPoint(e.getX(), e.getY())) {
			System.out.println(selectedLocation.toString());
			newMouseX = e.getX();
			newMouseY = e.getY();
			position = new Location((int)(newMouseY / squareSize), (int)(newMouseX / squareSize));
			System.out.println("Position: " + position.toString());
			if(valid(position)) {
				if(!empty(selectedLocation)){
					getPiece(selectedLocation).setSelected(false);
					xDrag = 0;
					yDrag = 0;
					System.out.println("i'm working");
					p = this.getPiece(selectedLocation);
					movers = p.moveLocs(selectedLocation, this);
					System.out.println("movers = " + movers + "; position = " + position);
					if(movers.contains(position)) {
						System.out.println("I'm moving");
						movePiece(selectedLocation, position);
						updateGame();
						repaint();
					}
				}
			}
			repaint();
		}
		selectedLocation = null; */
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseDragged(MouseEvent e) {
		xDrag = e.getX() - xAnchor;
		yDrag = e.getY() - yAnchor;
		if(selectedLocation == null) {
			return;
		}
		repaint();

	}
	@Override
	public void mouseMoved(MouseEvent e) {

		//repaint();

	}

}
