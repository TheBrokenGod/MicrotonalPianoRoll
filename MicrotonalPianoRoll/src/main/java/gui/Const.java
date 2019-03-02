package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class Const {

	static final Dimension KEY_SIZE = new Dimension(125, 38);
	static final int BORDER_THICKNESS = 1;
	static final Border KEY_BORDER = BorderFactory.createLineBorder(Color.DARK_GRAY, BORDER_THICKNESS);
	static final Color KEY_COLOR = Color.WHITE;
	static final Color KEY_HELD_COLOR = KEY_COLOR.darker();
	
	static final Dimension HOLE_SIZE_4 = new Dimension(2 * KEY_SIZE.width, KEY_SIZE.height);
	static final Border HOLE_BORDER = BorderFactory.createLineBorder(Color.GRAY, BORDER_THICKNESS);
	static final Color HOLE_COLOR_EVEN = new Color(0xEE, 0xEE, 0xEE);
	static final Color HOLE_COLOR_ODD = Color.WHITE;
	static final Image ICON;
	static {
		try {
			ICON = ImageIO.read(new File("key.png"));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	static final float PROGRESS_HEIGHT = 0.5f;
	static final Color PROGRESS_COLOR = Color.GREEN;
}
