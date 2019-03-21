package gui;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JToggleButton;

import main.Synth;

public class Key extends JToggleButton implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	static enum KeyState {
		Inactive, InactiveFocused, InactiveToActive, ActiveToInactive, ActiveFocused, Active
	}	
	private static Key cursorPos = null;
	static Key pressedOn = null;

	private final int index;
	private final Synth synth;
	private KeyState state;
	
	Key(int index, Synth synth) {
		this.index = index;
		this.synth = synth;
		setUIState(KeyState.Inactive);
		setBorder(Const.KEY_BORDER);
		setFocusable(false);
		// If composing mode
		if(synth != null) {
			addMouseListener(this);
		}
	}

	void setUIState(KeyState state) {
		this.state = state;
		repaint();
	}
	
	static boolean keyHeld() {
		return pressedOn != null;
	}
	
	boolean isPlaying() {
		return state == KeyState.Active || state == KeyState.ActiveFocused;
	}
	
	int getIndex() {
		return index;
	}
	
	void release() {
		// Can be playback or interactive
		if(state == KeyState.Active) {
			setUIState(KeyState.Inactive);
		}
		// Can be only in interactive mode
		else if(state == KeyState.ActiveFocused) {
			setUIState(KeyState.InactiveFocused);
		}
		// If interactive
		if(synth != null) {
			synth.stop(index);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		switch(state)
		{
		case Inactive:
			g.setColor(Const.KEY_COLORS[0]);
			break;
		case InactiveFocused:
			g.setColor(Const.KEY_COLORS[1]);
			break;
		case InactiveToActive:
		case ActiveToInactive:
			g.setColor(Const.KEY_COLORS[2]);
			break;
		case ActiveFocused:
			g.setColor(Const.KEY_COLORS[3]);
			break;
		case Active:
			g.setColor(Const.KEY_COLORS[4]);
			break;
		}
		Insets border = getInsets();
		g.fillRect(border.left, border.top, getWidth() - border.left - border.right, getHeight() - border.top - border.bottom);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		cursorPos = this;
		if(pressedOn == null) {
			switch(state) 
			{
			case Inactive:
				setUIState(KeyState.InactiveFocused);
				break;
			case Active:
				setUIState(KeyState.ActiveFocused);
				break;
			default:
				throw new RuntimeException();
			}
		}
		else {
			switch(state)
			{
			case Inactive:
				setUIState(KeyState.InactiveToActive);
				synth.play(index);
				break;
			case Active:
				setUIState(KeyState.ActiveToInactive);
				synth.stop(index);
				break;
			default:
				throw new RuntimeException();
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		cursorPos = null;
		switch(state)
		{
		case InactiveToActive:
			synth.stop(index);
		case InactiveFocused:
			setUIState(KeyState.Inactive);
			break;
		case ActiveToInactive:
			synth.play(index);
		case ActiveFocused:
			setUIState(KeyState.Active);
			break;
		default:
			throw new RuntimeException();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() != MouseEvent.BUTTON1) {
			return;
		}
		pressedOn = this;
		switch(state)
		{
		case InactiveFocused:
			setUIState(KeyState.InactiveToActive);
			synth.play(index);
			break;
		case ActiveFocused:
			setUIState(KeyState.ActiveToInactive);
			synth.stop(index);
			break;
		default:
			throw new RuntimeException();
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() != MouseEvent.BUTTON1) {
			return;
		}
		pressedOn = null;
		if(cursorPos == null) {
			return;
		}
		switch(cursorPos.state)
		{
		case InactiveToActive:
			cursorPos.setUIState(KeyState.ActiveFocused);
			cursorPos.setSelected(true);
			break;
		case ActiveToInactive:
			cursorPos.setUIState(KeyState.InactiveFocused);
			cursorPos.setSelected(false);
			break;
		default:
			throw new RuntimeException();
		}
	}
}
