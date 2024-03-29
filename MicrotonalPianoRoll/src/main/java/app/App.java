package app;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.math3.util.ArithmeticUtils;
import org.xml.sax.SAXException;

import model.Measure;
import model.Note;
import model.NoteLength;
import model.Track;
import model.TrackReader;
import model.TrackWriter;

class App extends JFrame implements WindowListener {
	
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		try {
			new App();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	Track track;
	Menu bar;
	Piano piano;
	Synth synth;
	Roll roll;
	int measure;
	Player player;
	private File file;
	private boolean modified;

	App(Track track) {
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.LINE_AXIS));
		setContentPane(root);
		player = new Player(this);
		setTrack(track);
		setFile(null);
		try {
			setIconImage(ImageIO.read(getClass().getResource("/app/icon.png")));
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setVisible(true);
	}
	
	App() {
		this(Const.defaultTrack());
	}

	@Override
	public void windowClosing(WindowEvent e) {
		exit();
	}

	public void setTrack(Track track) {
		setVisible(false);
		if(piano != null) {
			synth.dispose();
		}
		this.track = track;
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
		// Keep inside screen boundaries
		Dimension frameSize = new Dimension(
			Math.min(getSize().width, Math.round(Const.APP_MAX_SCREEN_PERC * Toolkit.getDefaultToolkit().getScreenSize().width)),
			Math.min(getSize().height, Math.round(Const.APP_MAX_SCREEN_PERC * Toolkit.getDefaultToolkit().getScreenSize().height))
		);
		setSize(frameSize);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void setFile(File file) {
		this.file = file;
		setFileModified(false);
	}
	
	private void setFileModified(boolean modified) {
		this.modified = modified;
		updateTitle();
	}
	
	private void updateTitle() {
		String title = "Microtonal Piano Roll :: ";
		if(file != null) {
			title += file.getName() + (modified ? "*" : "") + " :: ";
		}
		title += track.toString();
		if(isPlaying()) {
			title += " :: Playing";
		}
		setTitle(title);
	}
	
	void newFile() {
		new AudioDialog(this, Const.defaultTrack(), this::doNewFile);
	}
	
	void doNewFile(Track track) {
		setTrack(track);
		setFile(null);
	}

	void openFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(Const.FILE_TYPE_FILTER);
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			doOpenFile(chooser.getSelectedFile());
		}
	}
	
	void doOpenFile(File file) {
		try {
			setTrack(new TrackReader(file).read());
			setFile(file);
		}
		catch (SAXException e) {
			JOptionPane.showMessageDialog(this, "Not a valid file", file.getName(), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	void reloadFile() {
		if(file != null) {
			doOpenFile(file);
		}
	}
	
	void saveFile() {
		if(file == null) {
			saveFileAs();
		}
		else {
			new TrackWriter(file).write(track);
			setFileModified(false);
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
		setFileModified(true);
	}
	
	void setOscillator() {
		String value = (String) JOptionPane.showInputDialog(this, "Please select the unit oscillator", null, JOptionPane.QUESTION_MESSAGE, null, Synth.availableOscillators(), synth.oscillator());
		if(value != null) {
			List<Integer> keys = piano.grabActiveKeys();
			synth.dispose();
			synth = new Synth(track, value);
			piano.activateKeys(keys);
		}
	}
	
	void startOrStop() {
		if(!isPlaying()) {
			player.start();
		}
		else {
			player.stop();
		}
		updateTitle();
	}
	
	boolean isPlaying() {
		return player.isPlaying();
	}
	
	void stopIfPlaying() {
		if(player.isPlaying()) {
			startOrStop();
		}
	}
	
	void exit() {
		if(modified && JOptionPane.showConfirmDialog(this, "Do you want so save changes?", null, JOptionPane.YES_NO_OPTION) == 0) {
			saveFile();
		}
		System.exit(0);
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
			setFileModified(true);
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
		int bpm = track.measure(measure).getBPM();
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
		setFileModified(true);
	}
	
	void deleteMeasure() {
		if(track.measuresCount() == 1) {
			return;
		}
		track.remove(measure);
		setMeasure(Math.min(measure, track.measuresCount() - 1));
		setFileModified(true);
	}
	
	void setTempoChange() {
		String input = JOptionPane.showInputDialog(this, "Please insert the new tempo", currentMeasure().getBPM());
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
				setFileModified(true);
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
		setFileModified(true);
	}
	
	void setMeasure(int measure) {
		this.measure = measure;
		roll.setMeasure(this, currentMeasure());
		bar.setMeasure(measure, measure == 0 || currentMeasure().getBPM() != track.measure(measure - 1).getBPM());
	}
	
	void resolutionChanged(String resolution) {
		Measure measure = track.measure(this.measure);
		// Remove empty notes at the end of the measure
		List<Note> removed = new ArrayList<>();
		for(int i = measure.notesCount() - 1; i >= 0 && measure.note(i).isEmpty(); i--) {
			removed.add(0, measure.remove(i));
		}
		// Find minimum calculus unit which fits all the defined lengths
		int minLength = NoteLength.NAME_TO_INV_LEN.values().stream().reduce((l1, l2) -> ArithmeticUtils.lcm(l1, l2)).get();
		// Calculate the available space in terms of the minimum unit
		int availableSpace = removed.stream().mapToInt(note -> minLength / note.length.inverse()).sum();
		// If the new resolution fits exactly then alter measure
		int resolutionSize = minLength / NoteLength.inverse(resolution);
		if(availableSpace > 0 && availableSpace % resolutionSize == 0) {
			measure.fill(resolution);
			setFileModified(true);
		}
		else {
			// If not restore
			measure.addAll(removed);
		}
		setMeasure(this.measure);	
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
		// Do nothing if the user is still holding a piano key
		if(piano.isPianoKeyBeingPressed()) {
			return;
		}
		List<Integer> keys = piano.grabActiveKeys();
		Note note = currentMeasure().firstEmptyNote();
		if(note != null) {
			// Register the new note and reload measure
			keys.forEach(value -> noteChanged(note, value, true));
			setMeasure(measure);
		}
	}
	
	public void releasePianoKeys() {
		if(piano.isPianoKeyBeingPressed()) {
			return;
		}
		piano.grabActiveKeys();
	}
	
	private void noteChanged(Note note, int value, boolean selected) {
		if(selected) {
			note.add(value);
		}
		else {
			note.remove(value);
		}
		setFileModified(true);
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {	
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}
