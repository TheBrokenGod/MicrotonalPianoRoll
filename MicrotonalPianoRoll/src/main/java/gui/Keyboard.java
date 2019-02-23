package gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class Keyboard extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final Key[] keys;
	
	Keyboard(int numKeys) {
		keys = new Key[numKeys];
		for (int i = 0; i < numKeys; i++) {
			keys[i] = new Key(i);
		}
		setLayout(new GridLayout(numKeys, 1));
		for (int i = numKeys - 1; i >= 0; i--) {
			add(keys[i]);
		}
	}
	
	Key get(int key) {
		return (Key) getComponent(getComponentCount() - key - 1);
	}
}
