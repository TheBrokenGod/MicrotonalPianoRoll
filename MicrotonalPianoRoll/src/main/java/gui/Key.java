package gui;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JToggleButton;

public class Key extends JToggleButton {

	private static final long serialVersionUID = 1L;
	
	final int index;
	
	Key(int index) {
		this.index = index;
		setBorder(Const.KEY_BORDER);
		setFocusable(false);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if(isSelected()) {
			g.setColor(Const.KEY_HELD_COLOR);
		}
		else {
			g.setColor(Const.KEY_COLOR);			
		}
		Insets insets = getInsets();
		g.fillRect(insets.left, insets.top, getWidth() - insets.left - insets.right, getHeight() - insets.top - insets.bottom);
	}
}
