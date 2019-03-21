package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import gui.Key.KeyState;
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
			add(keys[i] = new Key(i, interactive ? app.synth : null), 0);
		}
		revalidate();
	}
	
	void hold(Note note) {
		releaseKeys();
		note.forEach(key -> keys[key].setUIState(KeyState.Active));
	}
	
	void releaseKeys() {
		Arrays.stream(keys).forEach(key -> key.release());
	}
	
	public boolean hasHeldKey() {
		return Key.pressedOn != null;
	}
	
	public List<Integer> getHeldKeys() {
		return Arrays.stream(keys).filter(Key::isPlaying).map(Key::getIndex).collect(Collectors.toList());
	}
}
