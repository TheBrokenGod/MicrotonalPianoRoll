package app;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import model.Note;

class Piano extends JPanel {

	private static final long serialVersionUID = 1L;

	final App app;
	private final PianoKey[] keys;
	PianoKey cursorPos;
	PianoKey pressedOn;
	
	Piano(App app, int numKeys) {
		this.app = app;
		keys = new PianoKey[numKeys];
		setLayout(new GridLayout(numKeys, 1));
		rebuildKeys();
		setPreferredSize(new Dimension(Const.KEY_SIZE.width, numKeys * Const.KEY_SIZE.height));
		setMaximumSize(new Dimension(Const.KEY_SIZE.width, Integer.MAX_VALUE));
	}

	void rebuildKeys() {
		removeAll();
		for (int i = 0; i < keys.length; i++) {
			add(keys[i] = new PianoKey(i, this), 0);
		}
		revalidate();
	}
	
	private List<Integer> getActiveKeys() {
		List<Integer> indices = Arrays.stream(keys).filter(PianoKey::isActive).map(key -> key.index).collect(Collectors.toList());
		Collections.sort(indices);
		return indices;
	}
	
	void activateKeys(List<Integer> keys) {
		keys.forEach(key -> this.keys[key].setActive(true));
	}

	boolean isPianoKeyBeingPressed() {
		return pressedOn != null;
	}

	void moveChordUp() {
		// Skip if a key is beign mouse pressed
		if(isPianoKeyBeingPressed()) {
			return;
		}
		List<Integer> indices = getActiveKeys();
		if(indices.isEmpty()) {
			return;
		}
		// Move the whole chord one semitone up
		if(indices.get(indices.size() - 1) < keys.length - 1) {
			indices.forEach(index -> keys[index].setActive(false));
			indices.forEach(index -> keys[index + 1].setActive(true));
		}
	}

	void moveChordDown() {
		if(isPianoKeyBeingPressed()) {
			return;
		}
		List<Integer> indices = getActiveKeys();
		if(indices.isEmpty()) {
			return;
		}
		if(indices.get(0) > 0) {
			indices.forEach(index -> keys[index].setActive(false));
			indices.forEach(index -> keys[index - 1].setActive(true));
		}
	}

	void moveHighestKeyUp() {
		if(isPianoKeyBeingPressed()) {
			return;
		}
		List<Integer> indices = getActiveKeys();
		if(indices.isEmpty()) {
			return;
		}
		int highest = indices.get(indices.size() - 1);
		if(highest < keys.length - 1) {
			keys[highest].setActive(false);
			keys[highest + 1].setActive(true);			
		}
	}

	void moveHighestKeyDown() {
		if(isPianoKeyBeingPressed()) {
			return;
		}
		List<Integer> indices = getActiveKeys();
		if(indices.isEmpty()) {
			return;
		}
		int highest = indices.get(indices.size() - 1);
		// Move down the highest note without overlapping the lower one
		if(indices.size() == 1 || highest > indices.get(indices.size() - 2) + 1) {
			keys[highest].setActive(false);
			keys[highest - 1].setActive(true);			
		}
	}

	void moveLowestKeyUp() {
		if(isPianoKeyBeingPressed()) {
			return;
		}
		List<Integer> indices = getActiveKeys();
		if(indices.isEmpty()) {
			return;
		}
		int lowest = indices.get(0);
		if(indices.size() == 1 || lowest < indices.get(1) - 1) {
			keys[lowest].setActive(false);	
			keys[lowest + 1].setActive(true);
		}
	}

	void moveLowestKeyDown() {
		if(isPianoKeyBeingPressed()) {
			return;
		}
		List<Integer> indices = getActiveKeys();
		if(indices.isEmpty()) {
			return;
		}
		int lowest = indices.get(0);
		if(lowest > 0) {
			keys[lowest].setActive(false);
			keys[lowest - 1].setActive(true);
		}
	}

	List<Integer> grabActiveKeys() {
		List<Integer> keys = new ArrayList<>();
		// Grab active keys from piano then release them
		Arrays.stream(this.keys).filter(PianoKey::isActive).forEach(key -> {
			keys.add(key.index);
			key.setActive(false);
		});
		return keys;
	}	

	void playPress(Note note) {
		playRelease();
		note.forEach(key -> keys[key].playPress());
	}

	void playRelease() {
		Arrays.stream(keys).forEach(key -> key.playRelease());
	}
}
