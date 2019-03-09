package gui;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.jsyn.unitgen.SawtoothOscillator;

import main.Synth;
import model.Measure;
import model.Note;
import model.Track;

public class GUI extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	
	private Track track;
	private Synth synth;
	private Piano piano;
	private Roll roll;
	private Thread[] player;
	private Double playerStart;

	public GUI(Track track) {
		buildMenu();
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.LINE_AXIS));
		setContentPane(root);		
		setTrack(track);
		player = null;
		playerStart = null;
		addKeyListener(this);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void setTrack(Track track) {
		this.track = track;
		setTitle("Microtonal Piano Roll :: " + track.toString());
		if(synth != null) {
			synth.dispose();
		}
		synth = new Synth(track, SawtoothOscillator.class);
		piano = new Piano(track.numKeys);
		roll = new Roll(track.numKeys);
		getContentPane().add(piano);
		getContentPane().add(Box.createRigidArea(new Dimension(10, 1)));
		getContentPane().add(roll);
		pack();
	}
	
	private void buildMenu() {
		JMenuBar bar = new JMenuBar();
		setJMenuBar(bar);
		JMenu menu, sub;
		bar.add(menu = new JMenu("Track"));
		menu.add(buildItem("New", null, KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildItem("Open", null, KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildItem("Save", null, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildItem("Save as", null, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		menu.addSeparator();
		menu.add(buildItem("Set audio", null, KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildItem("Play", null, KeyEvent.VK_SPACE, 0));
		menu.addSeparator();
		menu.add(buildItem("Exit", null, null, null));		
		bar.add(menu = new JMenu("Navigation"));
		menu.add(sub = new JMenu("Measure"));
		sub.add(buildItem("Previous", null, KeyEvent.VK_LEFT, 0));
		sub.add(buildItem("Next", null, KeyEvent.VK_RIGHT, 0));
		sub.addSeparator();
		sub.add(buildItem("First", null, KeyEvent.VK_HOME, 0));
		sub.add(buildItem("Last", null, KeyEvent.VK_END, 0));
		sub.addSeparator();
		sub.add(buildItem("By index", null, KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
		menu.add(sub = new JMenu("Tempo change"));
		sub.add(buildItem("Previous", null, KeyEvent.VK_PAGE_UP, 0));
		sub.add(buildItem("Next", null, KeyEvent.VK_PAGE_DOWN, 0));
		bar.add(menu = new JMenu("Composition"));
		menu.add(sub = new JMenu("Resolution"));
		ButtonGroup res = new ButtonGroup();
		sub.add(buildRadioItem("1", res));
		sub.add(buildRadioItem("2", res));
		sub.add(buildRadioItem("4", res));
		sub.add(buildRadioItem("8", res));
		sub.add(buildRadioItem("16", res));
		sub.add(buildRadioItem("32", res));
		sub.addSeparator();
		sub.add(buildRadioItem("2T", res));
		sub.add(buildRadioItem("4T", res));
		sub.add(buildRadioItem("8T", res));
		sub.add(buildRadioItem("16T", res));
		sub.add(buildRadioItem("32T", res));
		sub.addSeparator();
		sub.add(buildItem("Increase", null, KeyEvent.VK_PLUS, KeyEvent.CTRL_DOWN_MASK));
		sub.add(buildItem("Decrease", null, KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK));
		menu.add(sub = new JMenu("Measure"));
		sub.add(buildItem("Insert", null, null, null));
		sub.add(buildItem("Delete", null, null, null));
		menu.add(sub = new JMenu("Tempo change"));
		sub.add(buildItem("Set", null, null, null));
		sub.add(buildItem("Clear", null, null, null));
		bar.add(menu = new JMenu("About"));
		menu.add(buildItem("Info", null, null, null));
	}
	
	private static JMenuItem buildItem(String text, ActionListener listener, Integer keyCode, Integer modifiers) {
		JMenuItem item = new JMenuItem(text);
		if(listener != null) {
			item.addActionListener(listener);
		}
		if(keyCode != null) {
			item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers != null ? modifiers : 0));
		}
		return item;
	}
	
	private static JRadioButtonMenuItem buildRadioItem(String text, ButtonGroup group) {
		JRadioButtonMenuItem radio = new JRadioButtonMenuItem(text);
		group.add(radio);
		return radio;
	}

	@Override	
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_S:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_SPACE:
			break;
		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void play() {
		player = new Thread[] {new Thread(this::audioPlay), new Thread(this::visualPlay)};
		player[0].setPriority(Thread.MAX_PRIORITY);
		player[1].setPriority(Thread.MAX_PRIORITY);
		playerStart = synth.getCurrentTime();
		player[0].start();
		player[1].start();
	}
	
	public void stop() throws InterruptedException {
		player[0].interrupt();
		player[1].interrupt();
		player[0].join();
		player[1].join();
		playerStart = null;
		player = null;
	}
	
	private void audioPlay() {
		double time = playerStart;
		try {
			for(Measure measure : track.measures) {
				for(Note note : measure.notes) {
					synth.releaseAll();
					note.values.forEach(key -> synth.hold(key));
					synth.sleepUntil(time += note.soundDuration());
				}
			}
		}
		catch (InterruptedException e) {
		}
		finally {
			synth.releaseAll();
		}
	}
	
	private void visualPlay() {
		double time = playerStart;
		try {
			// Rebuild roll for current measure
			for(Measure measure : track.measures) {
				SwingUtilities.invokeAndWait(() -> {
					roll.setMeasure(measure);
				});
				List<Note> notes = measure.notes;
				for(int i = 0; i < notes.size(); i++) {
					Note note = notes.get(i);
					// Hold piano keys of current note
					SwingUtilities.invokeAndWait(() -> {
						piano.playNote(note);
					});
					// Show note progress on roll
					double noteStart = time;
					double noteEnd = time + notes.get(i).soundDuration();
					while(synth.getCurrentTime() < noteEnd) {
						SwingUtilities.invokeAndWait(() -> {
							double progress = (synth.getCurrentTime() - noteStart) / (noteEnd - noteStart);
							roll.setProgress(note, progress);
						});
					}
					SwingUtilities.invokeAndWait(() -> {
						roll.setProgress(note, 1.0);
					});
					time = noteEnd;
				}
			}
		}
		catch (InterruptedException | InvocationTargetException e) {
		}
		finally {
			SwingUtilities.invokeLater(() -> {
				roll.setMeasure(track.measures.get(0));
				piano.playNote(null);
			});
		}
	}
}
