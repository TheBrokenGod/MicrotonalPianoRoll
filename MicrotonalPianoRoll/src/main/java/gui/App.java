package gui;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.xml.sax.SAXException;

import main.Synth;
import model.Measure;
import model.Note;
import model.NoteLength;
import model.Track;
import model.TrackReader;
import model.TrackWriter;

public class App extends JFrame {

	private static final long serialVersionUID = 1L;
	
	Track track;
	Menu bar;
	Piano piano;
	Synth synth;
	Roll roll;
	int measure;
	Player player;
	private File file;

	public App(Track track) {
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.LINE_AXIS));
		setContentPane(root);		
		setTrack(track);
		player = new Player(this);
		file = null;
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public App() {
		this(Const.DEFAULT_TRACK);
	}

	public void setTrack(Track track) {
		if(piano != null) {
			synth.dispose();
		}
		this.track = track;
		setTitle("Microtonal Piano Roll :: " + track.toString());
		setJMenuBar(bar = new Menu(this, track));
		synth = new Synth(track, synth != null ? synth.oscillator() : Synth.defaultOscillator());
		piano = new Piano(this, track.numKeys);
		roll = new Roll(track);
		getContentPane().removeAll();
		getContentPane().add(piano);
		getContentPane().add(Box.createRigidArea(new Dimension(10, 1)));
		getContentPane().add(roll);
		setMeasure(0);
		pack();
	}
	
	void newFile() {
		new AudioDialog(this, Const.DEFAULT_TRACK, this::doNewFile);
	}
	
	void doNewFile(Track track) {
		// Add a first empty measure
		track.add(new Measure(Const.DEFAULT_BPM, bar.getResolution()));
		setTrack(track);
	}

	void openFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(Const.FILE_TYPE_FILTER);
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				setTrack(new TrackReader(file).read());
			}
			catch (SAXException e) {
				JOptionPane.showMessageDialog(this, "Not a valid file", file.getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	void saveFile() {
		if(file == null) {
			saveFileAs();
		}
		else {
			try {
				new TrackWriter(file).write(track);
			}
			catch (SAXException e) {
				JOptionPane.showMessageDialog(this, "Error saving track", file.getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	void saveFileAs() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(Const.FILE_TYPE_FILTER);
		if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			if(!file.toString().endsWith(".xml")) {
				file = new File(file.toString() + ".xml");
			}
			saveFile();
		}
	}
	
	void setAudio() {
		new AudioDialog(this, this.track, this::doSetAudio);
	}
	
	void doSetAudio(Track track) {
		int highest = 0;
		// Check if enough keys to play
		for(Measure measure : this.track) {
			for(Note note : measure) {
				for(int value : note) {
					highest = Math.max(highest, value);
				}
			}
		}
		if(track.numKeys < highest + 1) {
			JOptionPane.showMessageDialog(this, "This track requires " + (highest + 1) + " keys", "", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// Keep previous notes
		this.track.copyTo(track);
		setTrack(track);
	}
	
	void setOscillator() {
		String value = (String) JOptionPane.showInputDialog(this, "Please select the unit oscillator", null, JOptionPane.QUESTION_MESSAGE, null, Synth.availableOscillators(), synth.oscillator());
		if(value != null) {
			synth.dispose();
			synth = new Synth(track, value);
		}
	}
	
	void startOrStop() {
		if(!stopIfPlaying()) {
			start();
		}
	}
	
	void start() {
		player.start();
	}
	
	boolean stopIfPlaying() {
		if(player.isPlaying()) {
			player.stop();
			return true;
		}
		return false;
	}
	
	void previousMeasure() {
		if(measure > 0) {
			setMeasure(measure - 1);
		}
	}
	
	Measure currentMeasure() {
		return track.measure(measure);
	}
	
	void nextMeasure() {
		if(measure == track.measuresCount() - 1) {
			track.add(new Measure(track.lastMeasure().getBPM(), bar.getResolution()));
		}
		setMeasure(measure + 1);
	}
	
	void firstMeasure() {
		setMeasure(0);
	}
	
	void lastMeasure() {
		setMeasure(track.measuresCount() - 1);
	}
	
	void byIndex() {
		String input = JOptionPane.showInputDialog(this, "Please insert measure index", Integer.toString(measure + 1));
		try {
			int index = Integer.parseInt(input) - 1;
			if(index >= 0 && index < track.measuresCount()) {
				setMeasure(index);
			}
		}
		catch(NumberFormatException e) {
		}
	}
	
	void previousTempo() {
		if(measure == 0) {
			return;
		}
		int measure = this.measure - 1;
		int bpm = currentMeasure().getBPM();
		// Move at the beginning of the tempo group of the previous measure 
		for (int i = measure - 1; i >= 0 && track.measure(i).getBPM() == bpm; i--) {
			measure = i;
		}
		setMeasure(measure);
	}
	
	void nextTempo() {
		// Move at the first measure where the tempo changes
		for (int i = measure; i < track.measuresCount(); i++) {
			if(track.measure(i).getBPM() != currentMeasure().getBPM()) {
				setMeasure(i);
				break;
			}
		}
	}
	
	void insertMeasure() {
		track.insert(measure, new Measure(currentMeasure().getBPM(), bar.getResolution()));
		setMeasure(measure);
	}
	
	void deleteMeasure() {
		if(track.measuresCount() == 1) {
			return;
		}
		track.remove(measure);
		setMeasure(Math.min(measure, track.measuresCount() - 1));
	}
	
	void setTempoChange() {
		String input = JOptionPane.showInputDialog(this, "Please insert new tempo", currentMeasure().getBPM());
		try {
			int bpm = Integer.parseInt(input);
			if(bpm > 0) {
				int old = currentMeasure().getBPM();
				// Set new value on all measures sharing the same BPM, starting from current position
				for (int i = measure; i < track.measuresCount() && track.measure(i).getBPM() == old; i++) {
					track.measure(i).setBPM(bpm);					
				}
				// Update bar text
				setMeasure(measure);
			}
		}
		catch(NumberFormatException e) {
		}
	}
	
	void clearTempoChange() {
		if(measure == 0) {
			return;
		}
		int old = currentMeasure().getBPM();
		// Undo tempo change on all measures previously affected by setTempoChange
		for (int i = measure; i < track.measuresCount() && track.measure(i).getBPM() == old; i++) {
			track.measure(i).setBPM(track.measure(i - 1).getBPM());			
		}
		setMeasure(measure);
	}
	
	void setMeasure(int measure) {
		this.measure = measure;
		roll.setMeasure(this, currentMeasure());
		bar.setMeasure(measure, measure == 0 || currentMeasure().getBPM() != track.measure(measure - 1).getBPM());
	}
	
	// TODO rework from scratch
	void resolutionChanged(String resolution) {
		Measure measure = track.measure(this.measure);
		// Remove empty notes at the end of the measure
		List<Note> removed = new ArrayList<>();
		for(int i = measure.notesCount() - 1; i >= 0 && measure.note(i).isEmpty(); i--) {
			removed.add(0, measure.remove(i));
		}
		if(removed.size() > 0) {
			// Check if empty space can be filled with notes at new resolution
			if(10 * NoteLength.inverse(resolution) % (int)Math.round(10 / measure.freeSpace()) == 0) {
				measure.fill(resolution);
			}
			else {
				// Otherwise restore
				measure.addAll(removed);
			}
		}
		setMeasure(this.measure);	
	}
	
	void showInfoDialog() {
		// TODO implement
	}

	void rollHoleChanged(int index, int value, boolean selected) {
		Note note = currentMeasure().note(index);
		noteChanged(note, value, selected);
		// May be the new rightmost empty note
		if(note.isEmpty()) {
			resolutionChanged(bar.getResolution());			
		}
	}

	public void writePianoKeysIntoRoll() {
		// Do nothing if triggered during playback or user is holding a piano key
		if(piano.hasHeldKey()) {
			return;
		}
		Note note = currentMeasure().firstEmptyNote();
		if(note != null) {
			piano.getHeldKeys().forEach(value -> noteChanged(note, value, true));
			// Update Roll GUI
			setMeasure(measure);
		}
		piano.releaseKeys();
	}
	
	public void releasePianoKeys() {
		if(piano.hasHeldKey()) {
			return;
		}
		piano.releaseKeys();
	}
	
	private void noteChanged(Note note, int value, boolean selected) {
		if(selected) {
			note.add(value);
		}
		else {
			note.remove(value);
		}
	}
}
