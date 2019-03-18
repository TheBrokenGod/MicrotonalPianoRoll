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
import javax.swing.filechooser.FileNameExtensionFilter;

import org.xml.sax.SAXException;

import com.jsyn.unitgen.SawtoothOscillator;

import main.Synth;
import model.Measure;
import model.Note;
import model.NoteLength;
import model.SongReader;
import model.Track;

public class App extends JFrame {

	private static final long serialVersionUID = 1L;
	
	Track track;
	Menu bar;
	Piano piano;
	Synth synth;
	Roll roll;
	private Player player;
	private int measure;
	private File current;

	public App(Track track) {
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.LINE_AXIS));
		setContentPane(root);		
		setTrack(track);
		player = new Player(this);
		current = null;
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	void setTrack(Track track) {
		if(piano != null) {
			removeKeyListener(piano);
			synth.dispose();
		}
		this.track = track;
		setTitle("Microtonal Piano Roll :: " + track.toString());
		setJMenuBar(bar = new Menu(this, track));
		synth = new Synth(track, SawtoothOscillator.class);
		addKeyListener(piano = new Piano(synth, track.numKeys));
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
		chooser.setFileFilter(new FileNameExtensionFilter("XML file (Microtonal Piano Roll)", "xml"));
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				setTrack(new SongReader(file).read());
			}
			catch (SAXException e) {
				JOptionPane.showMessageDialog(this, "Not a valid file", file.getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	void saveFile() {
		if(current == null) {
			saveFileAs();
		}
	}
	
	void saveFileAs() {
		current = null;
	}
	
	void setAudio() {
		new AudioDialog(this, this.track, this::doSetAudio);
	}
	
	void setOscillator() {
	}
	
	void doSetAudio(Track track) {
		// Keep previous notes
		this.track.copyTo(track);
		setTrack(track);
	}
	
	void startOrStop() {
		if(player.isPlaying()) {
			player.stop();
		}
		else {
			player.start();
		}
	}
	
	void setInteractive(boolean isInteractive) {
		if(isInteractive) {
			if(player.isPlaying()) {
				player.stop();
			}
			addKeyListener(piano);
		}
		else {
			removeKeyListener(piano);
		}
	}
	
	void previousMeasure() {
		setInteractive(true);
		if(measure > 0) {
			setMeasure(measure - 1);
		}
	}
	
	int currentMeasure() {
		return measure;
	}
	
	void nextMeasure() {
		setInteractive(true);
		if(measure == track.measuresCount() - 1) {
			track.add(new Measure(track.lastMeasure().getBPM(), bar.getResolution()));
		}
		setMeasure(measure + 1);
	}
	
	void firstMeasure() {
		setInteractive(true);
		setMeasure(0);
	}
	
	void lastMeasure() {
		setInteractive(true);
		setMeasure(track.measuresCount() - 1);
	}
	
	void setMeasure(int measure) {
		this.measure = measure;
		roll.setMeasure(this, track.measure(measure));
		bar.setMeasure(measure);
	}
	
	// TODO rework from scratch
	void resolutionChanged() {
		Measure measure = track.measure(this.measure);
		// Remove empty notes at the end of the measure
		List<Note> removed = new ArrayList<>();
		for(int i = measure.notesCount() - 1; i >= 0 && measure.note(i).isEmpty(); i--) {
			removed.add(0, measure.remove(i));
		}
		if(removed.size() > 0) {
			// Check if empty space can be filled with notes at new resolution
			if(10 * NoteLength.inverse(bar.getResolution()) % (int)Math.round(10 / measure.freeSpace()) == 0) {
				measure.fill(bar.getResolution());
			}
			else {
				// Otherwise restore
				measure.addAll(removed);
			}
		}
		setMeasure(this.measure);	
	}

	public void noteChanged(int i, int value, boolean selected) {
		Note note = track.measure(measure).note(i);
		if(selected) {
			note.add(value);
		}
		else {
			note.remove(value);
			// May be the new rightmost empty note
			if(note.isEmpty()) {
				resolutionChanged();
			}
		}
	}
}
