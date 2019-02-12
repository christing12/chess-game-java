package chess;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public abstract class Piece {
	private Image image;
	private boolean selected;
	public abstract int getValue();
	public abstract Color getColor();
	public void setChange(){
		
	}
	public  Image getImage(){
		return image;
	}
	public abstract ArrayList<Location> moveLocs(Location loc, Board board);
	public BufferedImage loadImage(String filename) {
		try {
			BufferedImage i = ImageIO.read(new File(filename));
			System.out.println(i);
			return i;
		}
		catch(Exception e) {
			System.err.println("Failed to load resource filename" + filename);
			return null;
		}
	}
	
	public Piece(String imageName) {
		image = loadImage(imageName);
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
