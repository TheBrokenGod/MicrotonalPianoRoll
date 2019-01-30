package gui;

import static gui.Const.KEYS_BORDER;
import static gui.Const.ICON_HORIZONTAL;
import static gui.Const.KEYS_ICON_PLACEMENT;
import static gui.Const.KEYS_ICON_SIZE;
import static gui.Const.ICON_VERTICAL;
import static gui.Const.KEY_SIZE_HORIZONTAL;
import static gui.Const.KEY_SIZE_VERTICAL;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JToggleButton;

class Key extends JToggleButton implements MouseListener {

	private static final long serialVersionUID = 1L;
	private static enum KeyState {
		Inactive, InactiveFocused, InactiveToActive, ActiveToInactive, ActiveFocused, Active
	}
	
	private static Key cursorPos = null;
	private static Key pressedOn = null;

	private final Keyboard keyboard;
	private KeyState status;
	final double value;
	
	Key(Keyboard keyboard, double value) {
		this.keyboard = keyboard;
		this.value = value;
		setState(KeyState.Inactive);
		setPreferredSize(keyboard.isVertical() ? KEY_SIZE_VERTICAL : KEY_SIZE_HORIZONTAL);
		setBorder(KEYS_BORDER);
		setFocusable(false);
		addMouseListener(this);
	}
	
	private void setState(KeyState status) {
		this.status = status;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		switch(status)
		{
		case Inactive:
			g.setColor(Const.KEYS_COLORS[0]);
			break;
		case InactiveFocused:
			g.setColor(Const.KEYS_COLORS[1]);
			break;
		case InactiveToActive:
		case ActiveToInactive:
			g.setColor(Const.KEYS_COLORS[2]);
			break;
		case ActiveFocused:
			g.setColor(Const.KEYS_COLORS[3]);
			break;
		case Active:
			g.setColor(Const.KEYS_COLORS[4]);
			break;
		}
		Insets border = getInsets();
		g.fillRect(border.left, border.top, getWidth() - border.left - border.right, getHeight() - border.top - border.bottom);
		if(keyboard.isVertical()) {
			g.drawImage(ICON_VERTICAL, getWidth() / KEYS_ICON_PLACEMENT, (getHeight() - KEYS_ICON_SIZE) / 2, null);
		}
		else {
			g.drawImage(ICON_HORIZONTAL, (getWidth() - KEYS_ICON_SIZE) / 2, getHeight() - KEYS_ICON_SIZE - getHeight() / KEYS_ICON_PLACEMENT, null);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		cursorPos = this;
		if(pressedOn == null) {
			switch(status) 
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
			switch(status)
			{
			case Inactive:
				setState(KeyState.InactiveToActive);
				keyboard.keyActive(this);
				break;
			case Active:
				setState(KeyState.ActiveToInactive);
				keyboard.keyInactive(this);
				break;
			default:
				throw new RuntimeException();
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		cursorPos = null;
		switch(status)
		{
		case InactiveToActive:
			keyboard.keyInactive(this);
		case InactiveFocused:
			setState(KeyState.Inactive);
			break;
		case ActiveToInactive:
			keyboard.keyActive(this);
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
		switch(status)
		{
		case InactiveFocused:
			setState(KeyState.InactiveToActive);
			keyboard.keyActive(this);
			break;
		case ActiveFocused:
			setState(KeyState.ActiveToInactive);
			keyboard.keyInactive(this);
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
		switch(cursorPos.status)
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
