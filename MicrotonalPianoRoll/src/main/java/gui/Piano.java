package gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import model.Note;

public class Piano extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final Key[] keys;
	
	Piano(int numKeys) {
		keys = new Key[numKeys];
		setLayout(new GridLayout(numKeys, 1));
		for (int i = 0; i < numKeys; i++) {
			keys[i] = new Key(i);
			add(keys[i], 0);
		}
		setPreferredSize(new Dimension(Const.KEY_SIZE.width, numKeys * Const.KEY_SIZE.height));
	}
	
	void playNote(Note note) {
		for (int i = 0; i < keys.length; i++) {
			keys[i].setSelected(note != null && note.values.contains(i));
		}
	}
}
