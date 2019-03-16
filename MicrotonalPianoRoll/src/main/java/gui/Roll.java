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
	private Map<Note, List<RollValue>> notes;
	
	Roll(Track track) {
		this.numRows = track.numKeys;
		setLayout(new GridLayout(track.numKeys, 1));
		setPreferredSize(new Dimension(Const.ROLL_SIZE.width, track.numKeys * Const.ROLL_SIZE.height));
	}
	
	void setMeasure(App app, Measure measure) {
		removeAll();
		this.notes = new HashMap<>();
		measure.forEach(note -> notes.put(note, new ArrayList<>()));
		// Build a row for each key
		for(int i = 0; i < numRows; i++) {
			JPanel row = new JPanel();
			row.setLayout(new BoxLayout(row, BoxLayout.LINE_AXIS));
			// Row structure is that of the measure
			for(int j = 0; j < measure.notesCount(); j++) {
				// TODO refactor
				RollValue rollNote = new RollValue(app, j, i, measure.note(j).length.logical());
				if(measure.note(j).contains(i)) {
					rollNote.setSelected(true);
					notes.get(measure.note(j)).add(rollNote);
				}
				row.add(rollNote);
				notes.putIfAbsent(measure.note(j), new ArrayList<>());
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
