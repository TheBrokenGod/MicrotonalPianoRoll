package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

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
	
	void play(Set<Integer> chord) {
		for (int i = 0; i < keys.length; i++) {
			keys[i].setSelected(chord.contains(i));
		}
	}
	
	public void mute() {
		play(new HashSet<>());
	}
}
