package gui;

import java.awt.GridLayout;
import java.util.Set;

import javax.swing.JPanel;

class Keyboard extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final boolean vertical;
	private final Set<Key> activeKeys;
	
	Keyboard(int numKeys, boolean vertical) {
		this.vertical = vertical;
		this.activeKeys = new CheckedHashSet<>();
		setLayout(vertical ? new GridLayout(numKeys, 1) : new GridLayout(1, numKeys));
		for (int i = 0; i < numKeys; i++) {
			add(new Key(this, i));
		}
	}
	
	boolean isVertical() {
		return vertical;
	}
	
	void keyActive(Key key) {
		activeKeys.add(key);
	}
	
	void keyInactive(Key key) {
		activeKeys.remove(key);
	}
}
