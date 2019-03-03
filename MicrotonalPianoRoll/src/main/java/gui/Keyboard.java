package gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Keyboard extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final Key[] keys;
	final JLabel measure;
	
	Keyboard(int numKeys) {
		super(new GridLayout(numKeys + 1, 1));
		keys = new Key[numKeys];
		for (int i = 0; i < numKeys; i++) {
			keys[i] = new Key(i);
		}
		add(measure = new JLabel("measure / tot", SwingConstants.CENTER));
		for (int i = numKeys - 1; i >= 0; i--) {
			add(keys[i]);
		}
		setPreferredSize(new Dimension(Const.KEY_SIZE.width, (numKeys + 1) * Const.KEY_SIZE.height));
	}
	
	void deselectAll() {
		for (int i = 0; i < keys.length; i++) {
			keys[i].setSelected(false);
		}
	}
	
	void select(int key) {
		keys[key].setSelected(true);
	}
}
