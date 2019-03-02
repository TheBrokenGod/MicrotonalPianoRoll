package gui;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JToggleButton;

class Hole extends JToggleButton {

	private static final long serialVersionUID = 1L;
	
	private final boolean evenRow;
	
	public Hole(boolean evenRow) {
		this.evenRow = evenRow;
		setBorder(Const.HOLE_BORDER);
		setFocusable(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if(isSelected()) {
			g.setColor(evenRow ? Const.HOLE_COLOR_EVEN.darker() : Const.HOLE_COLOR_ODD.darker());
		}
		else {
			g.setColor(evenRow ? Const.HOLE_COLOR_EVEN : Const.HOLE_COLOR_ODD);
		}
		Insets border = getInsets();
		g.fillRect(border.left, border.top, getWidth() - border.left - border.right, getHeight() - border.top - border.bottom);
	}
}
