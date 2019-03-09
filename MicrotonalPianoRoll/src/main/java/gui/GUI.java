package gui;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

import com.jsyn.unitgen.SawtoothOscillator;

import main.Synth;
import model.Track;

public class GUI extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	
	Track track;
	Synth synth;
	Piano piano;
	Roll roll;
	private Player player;

	public GUI(Track track) {
		buildMenu();
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.LINE_AXIS));
		setContentPane(root);		
		setTrack(track);
		player = new Player(this);
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
		menu.add(buildMenuItem("New", null, KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildMenuItem("Open", null, KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildMenuItem("Save", null, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildMenuItem("Save as", null, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		menu.addSeparator();
		menu.add(buildMenuItem("Set audio", null, KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildMenuItem("Play", this::startOrStop, KeyEvent.VK_SPACE, 0));
		menu.addSeparator();
		menu.add(buildMenuItem("Exit", null, null, null));		
		bar.add(menu = new JMenu("Navigation"));
		menu.add(sub = new JMenu("Measure"));
		sub.add(buildMenuItem("Previous", null, KeyEvent.VK_LEFT, 0));
		sub.add(buildMenuItem("Next", null, KeyEvent.VK_RIGHT, 0));
		sub.addSeparator();
		sub.add(buildMenuItem("First", null, KeyEvent.VK_HOME, 0));
		sub.add(buildMenuItem("Last", null, KeyEvent.VK_END, 0));
		sub.addSeparator();
		sub.add(buildMenuItem("By index", null, KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
		menu.add(sub = new JMenu("Tempo change"));
		sub.add(buildMenuItem("Previous", null, KeyEvent.VK_PAGE_UP, 0));
		sub.add(buildMenuItem("Next", null, KeyEvent.VK_PAGE_DOWN, 0));
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
		sub.add(buildMenuItem("Increase", null, KeyEvent.VK_PLUS, KeyEvent.CTRL_DOWN_MASK));
		sub.add(buildMenuItem("Decrease", null, KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK));
		menu.add(sub = new JMenu("Measure"));
		sub.add(buildMenuItem("Insert", null, null, null));
		sub.add(buildMenuItem("Delete", null, null, null));
		menu.add(sub = new JMenu("Tempo change"));
		sub.add(buildMenuItem("Set", null, null, null));
		sub.add(buildMenuItem("Clear", null, null, null));
		bar.add(menu = new JMenu("About"));
		menu.add(buildMenuItem("Info", null, null, null));
	}
	
	private static JMenuItem buildMenuItem(String text, Runnable action, Integer keyCode, Integer modifiers) {
		JMenuItem item = new JMenuItem(text);
		if(action != null) {
			item.addActionListener(e -> action.run());
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
	
	private void startOrStop() {
		if(player.isPlaying()) {
			player.stop();
		}
		else {
			player.start();
		}
	}
}
