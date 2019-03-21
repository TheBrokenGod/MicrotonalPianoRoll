package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Track;

public class Const {

	static final Track DEFAULT_TRACK = new Track(440, 880, 12, -12, 12);
	static final int DEFAULT_BPM = 60;
	
	static final FileFilter FILE_TYPE_FILTER = new FileNameExtensionFilter("Microtonal Piano Roll XML file", "xml");
	static final Dimension KEY_SIZE = new Dimension(128, 36);
	static final int BORDER_THICKNESS = 1;
	static final Border KEY_BORDER = BorderFactory.createLineBorder(Color.DARK_GRAY, BORDER_THICKNESS);
	static final Color[] KEY_COLORS = {Color.WHITE, Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY, Color.BLACK};
	static final int KEY_ICON_SIZE = 22;
	static final int KEY_ICON_PLACEMENT = 12;
	
	static final Dimension ROLL_SIZE = new Dimension(4 * 192, KEY_SIZE.height);
	static final Border HOLE_BORDER = BorderFactory.createLineBorder(Color.GRAY, BORDER_THICKNESS);
	static final Color ROLLNOTE_COLOR_EVEN = new Color(0xEE, 0xEE, 0xEE);
	static final Color ROLLNOTE_COLOR_ODD = Color.WHITE;
	static final Color ROLLNOTE_COLOR2_EVEN = new Color(0x55, 0xEE, 0x55);
	static final Color ROLLNOTE_COLOR2_ODD = new Color(0x66, 0xFF, 0x66);
	
	static String format(double value) {
		return String.format("%.3f", value);
	}
}
