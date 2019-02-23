package gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.jsyn.unitgen.SawtoothOscillator;

import main.KeySynth;
import model.Note;
import model.Track;

public class GUI extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	
	private final Track track;
	private final KeySynth synth;
	private final Keyboard keyboard;
	private Thread player;

	public GUI(Track track) {		
		super("Microtonal Piano Roll :: ");
		this.track = track;
		synth = new KeySynth(track.audio, SawtoothOscillator.class);
		keyboard = new Keyboard(track.audio.numKeys);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(keyboard, BorderLayout.EAST);
		setContentPane(panel);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(this);
		setVisible(true);
		player = null;
		pack();
	}
	
//	private static String format(double frequency) {
//		return String.format("%.1f", frequency);
//	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_S:
			System.err.println("S pressed");
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_SPACE:
			System.err.println("SPACE released");
			break;
		default:
			System.err.println("released");
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void play() {
		player = new Thread(this::doPlay);
		player.setPriority(Thread.MAX_PRIORITY);
		player.start();
	}
	
	public void stop() throws InterruptedException {
		player.interrupt();
		player.join();
		player = null;
	}
	
	private void doPlay() {
		Optional<Note> prev = Optional.empty();
		try {
			double time = synth.getCurrentTime();
			for(Note note : track.notes) {
				setKeyState(prev, false);
				setKeyState(prev = Optional.of(note), true);
				synth.sleepUntil(time += note.duration());
			}
		}
		catch (InterruptedException e) {
		}
		finally {
			try {
				setKeyState(prev, false);
			}
			catch (InterruptedException e) {
			}			
		}
	}
	
	private void setKeyState(Optional<Note> note, boolean held) throws InterruptedException {
		if(!note.isPresent()) {
			return;
		}
		for(int key : note.get().values) {
			synth.setKeyState(key, held);	
			try {
				SwingUtilities.invokeAndWait(() -> {
					keyboard.get(key).setSelected(held);
				});
			}
			catch(InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
