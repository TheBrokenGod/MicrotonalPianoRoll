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

	final int index;
	private final Piano piano;
	private final App app;
	private KeyState state;
	
	PianoKey(int index, Piano piano) {
		this.index = index;
		this.piano = piano;
		this.app = piano.app;
		setState(KeyState.Inactive);
		setBorder(Const.KEY_BORDER);
		setFocusable(false);
		// If editing enable key
		if(!app.isPlaying()) {
			addMouseListener(this);
			piano.cursorPos = null;
			piano.pressedOn = null;
		}
		// If playing one click will stop
		else {
			addActionListener(e -> piano.app.stopIfPlaying());
		}
	}

	private void setState(KeyState state) {
		this.state = state;
		repaint();
	}
	
	boolean isActive() {
		return state == KeyState.Active || state == KeyState.ActiveFocused;
	}

	void setActive(boolean activate) {
		if(activate) {
			switch(state)
			{
			case Inactive:
				app.synth.play(index);
				setState(KeyState.Active);
				break;
			case InactiveFocused:
				setState(KeyState.ActiveFocused);
				app.synth.play(index);
				break;
			default:
				throw new RuntimeException();
			}
		}
		else {
			switch(state)
			{
			case Active:
				app.synth.stop(index);
				setState(KeyState.Inactive);
				break;
			case ActiveFocused:
				setState(KeyState.InactiveFocused);
				app.synth.stop(index);
				break;
			default:
				throw new RuntimeException();
			}
		}
	}

	void playPress() {
		setState(KeyState.Active);
	}

	void playRelease() {
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
		piano.cursorPos = this;
		if(piano.pressedOn == null) {
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
				app.synth.play(index);
				break;
			case Active:
				setState(KeyState.ActiveToInactive);
				app.synth.stop(index);
				break;
			default:
				throw new RuntimeException();
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		piano.cursorPos = null;
		switch(state)
		{
		case InactiveToActive:
			app.synth.stop(index);
		case InactiveFocused:
			setState(KeyState.Inactive);
			break;
		case ActiveToInactive:
			app.synth.play(index);
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
		piano.pressedOn = this;
		switch(state)
		{
		case InactiveFocused:
			setState(KeyState.InactiveToActive);
			app.synth.play(index);
			break;
		case ActiveFocused:
			setState(KeyState.ActiveToInactive);
			app.synth.stop(index);
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
		piano.pressedOn = null;
		if(piano.cursorPos == null) {
			return;
		}
		switch(piano.cursorPos.state)
		{
		case InactiveToActive:
			piano.cursorPos.setState(KeyState.ActiveFocused);
			piano.cursorPos.setSelected(true);
			break;
		case ActiveToInactive:
			piano.cursorPos.setState(KeyState.InactiveFocused);
			piano.cursorPos.setSelected(false);
			break;
		default:
			throw new RuntimeException();
		}
	}
}
