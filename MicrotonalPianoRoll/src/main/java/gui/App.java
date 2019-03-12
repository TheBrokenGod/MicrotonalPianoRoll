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
import net.sf.saxon.s9api.SaxonApiException;

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
		getContentPane().add(piano);
		getContentPane().add(Box.createRigidArea(new Dimension(10, 1)));
		getContentPane().add(roll);
		setMeasure(0);
		pack();
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
		if(measure == track.measures.size() - 1) {
			try {
				track.measures.add(new Measure().add(new Note("1", 60)));
			} 
			catch (SaxonApiException e) {
				throw new RuntimeException(e);
			}
		}
		setMeasure(measure + 1);
	}
	
	void firstMeasure() {
		setInteractive(true);
		setMeasure(0);
	}
	
	void lastMeasure() {
		setInteractive(true);
		setMeasure(track.measures.size() - 1);
	}
	
	void setMeasure(int measure) {
		this.measure = measure;
		roll.setMeasure(track.measures.get(measure));
		bar.setMeasure(track.measures.get(measure));
	}
}
