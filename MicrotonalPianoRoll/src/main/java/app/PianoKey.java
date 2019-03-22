package app;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JToggleButton;

class PianoKey extends JToggleButton implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	private static enum KeyState {
		Inactive, InactiveFocused, InactiveToActive, ActiveToInactive, ActiveFocused, Active
	}	
	private static PianoKey cursorPos = null;
	static PianoKey pressedOn = null;

	final int index;
	private final Synth synth;
	private KeyState state;
	
	PianoKey(int index, App app) {
		this.index = index;
		this.synth = app.synth;
		setState(KeyState.Inactive);
		setBorder(Const.KEY_BORDER);
		setFocusable(false);
		// If editing mode enable keyboard
		if(!app.isPlaying()) {
			addMouseListener(this);
			cursorPos = null;
			pressedOn = null;
		}
		// If playing a click will simply stop
		else {
			addActionListener(e -> app.stopIfPlaying());
		}
	}

	private void setState(KeyState state) {
		this.state = state;
		repaint();
	}
	
	boolean isActive() {
		return state == KeyState.Active || state == KeyState.ActiveFocused;
	}

	void setInactive() {
		if(state == KeyState.Active) {
			setState(KeyState.Inactive);
		}
		else if(state == KeyState.ActiveFocused) {
			setState(KeyState.InactiveFocused);
		}
		synth.stop(index);
	}

	void press() {
		setState(KeyState.Active);
	}

	void release() {
		setState(KeyState.Inactive);		
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
				setState(KeyState.InactiveFocused);
				break;
			case Active:
				setState(KeyState.ActiveFocused);
				break;
			default:
				throw new RuntimeException();
			}
		}
		else {
			switch(state)
			{
			case Inactive:
				setState(KeyState.InactiveToActive);
				synth.play(index);
				break;
			case Active:
				setState(KeyState.ActiveToInactive);
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
			setState(KeyState.Inactive);
			break;
		case ActiveToInactive:
			synth.play(index);
		case ActiveFocused:
			setState(KeyState.Active);
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
			setState(KeyState.InactiveToActive);
			synth.play(index);
			break;
		case ActiveFocused:
			setState(KeyState.ActiveToInactive);
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
			cursorPos.setState(KeyState.ActiveFocused);
			cursorPos.setSelected(true);
			break;
		case ActiveToInactive:
			cursorPos.setState(KeyState.InactiveFocused);
			cursorPos.setSelected(false);
			break;
		default:
			throw new RuntimeException();
		}
	}
}