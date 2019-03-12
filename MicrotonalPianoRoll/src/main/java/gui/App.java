package gui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jsyn.unitgen.SawtoothOscillator;

import main.Synth;
import model.Track;

public class App extends JFrame {

	private static final long serialVersionUID = 1L;
	
	Track track;
	MenuBar bar;
	Piano piano;
	Synth synth;
	Roll roll;
	private Player player;

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

	public void setTrack(Track track) {
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
			addKeyListener(piano);
		}
		else {
			removeKeyListener(piano);
		}
	}
}
