package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

import model.Track;

public class Const {

	static final Track DEFAULT_TRACK = new Track(440, 880, 12, -12, 12);
	static final int DEFAULT_BPM = 60;
	
	static final Dimension KEY_SIZE = new Dimension(125, 38);
	static final int BORDER_THICKNESS = 1;
	static final Border KEY_BORDER = BorderFactory.createLineBorder(Color.DARK_GRAY, BORDER_THICKNESS);
	static final Color KEY_COLOR = Color.WHITE;
	static final Color KEY_HELD_COLOR = KEY_COLOR.darker();
	
	static final Dimension ROLL_SIZE = new Dimension(4 * KEY_SIZE.width, KEY_SIZE.height);
	static final Border HOLE_BORDER = BorderFactory.createLineBorder(Color.GRAY, BORDER_THICKNESS);
	static final Color ROLLNOTE_COLOR_EVEN = new Color(0xEE, 0xEE, 0xEE);
	static final Color ROLLNOTE_COLOR_ODD = Color.WHITE;
	static final Color ROLLNOTE_COLOR2_EVEN = new Color(0x55, 0xEE, 0x55);
	static final Color ROLLNOTE_COLOR2_ODD = new Color(0x66, 0xFF, 0x66);
	static final Image ICON;
	static {
		try {
			ICON = ImageIO.read(new File("key.png"));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	static String format(double value) {
		return String.format("%.3f", value);
	}
}
