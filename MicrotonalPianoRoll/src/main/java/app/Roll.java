package app;

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
	private Map<Note, List<RollHole>> notes;
	
	Roll(Track track) {
		this.numRows = track.numKeys;
		setLayout(new GridLayout(track.numKeys, 1));
		setPreferredSize(new Dimension(Const.ROLL_SIZE.width, track.numKeys * Const.ROLL_SIZE.height));
	}
	
	void setMeasure(App app, Measure measure) {
		removeAll();
		notes = new HashMap<>();
		// Map notes' values with corresponding GUI roll holes
		measure.forEach(note -> notes.put(note, new ArrayList<>()));
		// Build a row for each piano key
		for(int i = 0; i < numRows; i++) {
			add(buildRow(app, measure, i), 0);
		}
		revalidate();
	}
	
	private JPanel buildRow(App app, Measure measure, int rowInd) {
		JPanel row = new JPanel();
		row.setLayout(new BoxLayout(row, BoxLayout.LINE_AXIS));
		// Each row has the structure of the notes in the measure
		for(int noteInd = 0; noteInd < measure.notesCount(); noteInd++) {
			RollHole hole = new RollHole(app, noteInd, rowInd, measure.note(noteInd).length);
			if(measure.note(noteInd).contains(rowInd)) {
				hole.setSelected(true);
				notes.get(measure.note(noteInd)).add(hole);
			}
			row.add(hole);
		}
		return row;
	}

	void setProgress(Note note, double progress) {
		notes.get(note).forEach(rollNote -> rollNote.setProgress(progress));
	}

	void clearProgress() {
		notes.values().forEach(list -> list.forEach(rollNote -> rollNote.setProgress(0)));
	}
}
