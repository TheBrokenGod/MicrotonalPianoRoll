package gui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jsyn.unitgen.SawtoothOscillator;

import main.Synth;
import model.Measure;
import model.Note;
import model.Track;

public class App extends JFrame {

	private static final long serialVersionUID = 1L;
	
	Track track;
	MenuBar bar;
	Piano piano;
	Synth synth;
	Roll roll;
	private Player player;
	private int measure;

	public App(Track track) {
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.LINE_AXIS));
		setContentPane(root);		
		setTrack(track);
		player = new Player(this);
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
		setJMenuBar(bar = new MenuBar(this, track));
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
		Track track = new Track(440, 880, 12, 0, 12);
		Measure measure = new Measure(60);
		fillMeasure(measure);
		track.add(measure);
		setTrack(track);
	}
	
	void fillMeasure(Measure measure) {
		while(!measure.isFull()) {
			measure.add(new Note(bar.getResolution()));
		}
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
			Measure measure = new Measure(track.lastMeasure().getBPM());
			fillMeasure(measure);
			track.add(measure);
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
	
	void resolutionChanged() {
		Measure measure = track.measure(this.measure);
		// Modify the length of all notes without values (not set) ending the measure
		for(int i = measure.notesCount() - 1; i >= 0 && measure.note(i).isEmpty(); i--) {
			measure.remove(i);
		}
		fillMeasure(measure);
		setMeasure(this.measure);
	}

	public void noteChanged(int i, int value, boolean selected) {
		Note note = track.measure(measure).note(i);
		if(selected) {
			note.add(value);
		}
		else {
			note.remove(value);
			// May be the rightmost previously enabled note in the measure
			if(note.isEmpty()) {
				resolutionChanged();
			}
		}
	}
}
