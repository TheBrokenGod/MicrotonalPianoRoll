package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import model.Note;

public class Piano extends JPanel {

	private static final long serialVersionUID = 1L;

	final App app;
	private final Key[] keys;
	
	Piano(App app, int numKeys) {
		this.app = app;
		keys = new Key[numKeys];
		setLayout(new GridLayout(numKeys, 1));
		rebuildKeys(true);
		setPreferredSize(new Dimension(Const.KEY_SIZE.width, numKeys * Const.KEY_SIZE.height));
		setMaximumSize(new Dimension(Const.KEY_SIZE.width, Integer.MAX_VALUE));
	}

	void rebuildKeys(boolean interactive) {
		removeAll();
		for (int i = 0; i < keys.length; i++) {
			// In interactive mode keys will call start and stop on the synth
			add(keys[i] = new Key(i, interactive ? app.synth : null), 0);
		}
		revalidate();
	}
	
	void press(Note note) {
		release();
		note.forEach(key -> keys[key].press());
	}
	
	void release() {
		Arrays.stream(keys).forEach(key -> key.release());
	}
	
	boolean hasKeyBeingPressed() {
		return Key.pressedOn != null;
	}
	
	List<Integer> readActiveKeys() {
		List<Integer> keys = new ArrayList<>();
		Arrays.stream(this.keys).filter(Key::isActive).forEach(key -> {
			keys.add(key.index);
			key.setInactive();
		});
		return keys;
	}
}
