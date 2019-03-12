package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.JPanel;

import main.Synth;
import model.Note;

public class Piano extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;

	private final Synth synth;
	private final Key[] keys;
	
	Piano(Synth synth, int numKeys) {
		this.synth = synth;
		keys = new Key[numKeys];
		setLayout(new GridLayout(numKeys, 1));
		for (int i = 0; i < numKeys; i++) {
			keys[i] = new Key(i);
			add(keys[i], 0);
		}
		setPreferredSize(new Dimension(Const.KEY_SIZE.width, numKeys * Const.KEY_SIZE.height));
	}
	
	void play(Note note) {
		stop();
		note.values.forEach(key -> keys[key].setSelected(true));
	}
	
	void stop() {
		Arrays.stream(keys).forEach(key -> key.setSelected(false));
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode())
		{
		case KeyEvent.VK_F1:
			synth.play(0);
			break;
		case KeyEvent.VK_F2:
			synth.play(1);
			break;
		case KeyEvent.VK_F3:
			synth.play(2);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		switch(arg0.getKeyCode())
		{
		case KeyEvent.VK_F1:
			synth.stop(0);
			break;
		case KeyEvent.VK_F2:
			synth.stop(1);
			break;
		case KeyEvent.VK_F3:
			synth.stop(2);
			break;
		}	
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
