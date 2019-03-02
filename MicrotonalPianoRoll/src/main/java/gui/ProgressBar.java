package gui;

import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class ProgressBar extends JPanel {

	private static final long serialVersionUID = 1L;
	
	final Roll roll;
	
	public ProgressBar(Roll roll) {
		this.roll = roll;
		setLayout(new GridLayout(1, 1));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		float fillHeight = getHeight() * Const.PROGRESS_HEIGHT;
		g.setColor(Const.PROGRESS_COLOR);
		g.fillRect(0, Math.round((getHeight() - fillHeight) / 2), getWidth() - Const.BORDER_THICKNESS, Math.round(fillHeight));
	}
}
