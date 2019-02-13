package gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Audio;

public class GUI extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	public static enum Orientation {
		Horizontal, Vertical
	}
	
	private final Keyboard keyboard;

	public GUI(Audio audio) {		
		super("Microtonal Piano Roll :: " + audio.keysCount() + " keys :: " + audio.lowestFrequency() + "Hz - " + audio.highestFrequency() + "Hz");
		keyboard = new Keyboard(audio.keysCount(), false);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(keyboard, BorderLayout.EAST);
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(this);
		setVisible(true);
	}

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
}
