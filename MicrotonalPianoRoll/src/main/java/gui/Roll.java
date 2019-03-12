package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import model.Measure;
import model.Note;
import model.Track;

class Roll extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final int numRows;
	private Map<Note, List<RollNote>> notes;
	
	Roll(Track track) {
		this.numRows = track.numKeys;
		setLayout(new GridLayout(track.numKeys, 1));
		setMeasure(track.measures.get(0));
		setPreferredSize(new Dimension(Const.ROLL_SIZE.width, track.numKeys * Const.ROLL_SIZE.height));
	}
	
	void setMeasure(Measure measure) {
		removeAll();
		this.notes = new HashMap<>();
		measure.notes.forEach(note -> notes.put(note, new ArrayList<>()));
		// Build a row for each key
		for(int i = 0; i < numRows; i++) {
			JPanel row = new JPanel();
			row.setLayout(new BoxLayout(row, BoxLayout.LINE_AXIS));
			// Row structure is that of the measure
			for(Note note : measure.notes) {
				RollNote rollNote = new RollNote(note.logicalLength(), i % 2 == 0);
				if(note.values.contains(i)) {
					rollNote.setSelected(true);
					notes.get(note).add(rollNote);
				}
				row.add(rollNote);
				notes.putIfAbsent(note, new ArrayList<>());
			}
			add(row, 0);
		}
		revalidate();
	}

	void setProgress(Note note, double progress) {
		notes.get(note).forEach(rollNote -> rollNote.setProgress(progress));
	}
	
	void clearProgress() {
		notes.values().forEach(list -> list.forEach(rollNote -> rollNote.setProgress(0)));
	}
}
