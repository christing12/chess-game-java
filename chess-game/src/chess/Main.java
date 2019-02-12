package chess;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		final int DISPLAY_WIDTH = 700;
		final int DISPLAY_HEIGHT = 700;
		Board chessboard = new Board();
		JFrame f = new JFrame();
		f.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		f.setTitle("This is My Chess Game");
		f.add(chessboard);
		f.setVisible(true);
	}

}
