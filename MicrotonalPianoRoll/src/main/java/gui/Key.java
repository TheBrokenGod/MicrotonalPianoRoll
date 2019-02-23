package gui;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JToggleButton;

public class Key extends JToggleButton {

	private static final long serialVersionUID = 1L;
	
	Key(int index) {
		setPreferredSize(Const.KEY_SIZE);
		setBorder(Const.KEY_BORDER);
		setFocusable(false);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if(isSelected()) {
			g.setColor(Const.KEY_PLAYING_COLOR);
		}
		else {
			g.setColor(Const.KEY_MUTE_COLOR);			
		}
		Insets border = getInsets();
		g.fillRect(border.left, border.top, getWidth() - border.left - border.right, getHeight() - border.top - border.bottom);
		g.drawImage(Const.ICON, (int) (getWidth() * Const.KEY_ICON_PLACEMENT), (getHeight() - Const.KEY_ICON_SIZE) / 2, null);
	}
}
