package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

class RollNote extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final boolean evenRow;
	private final NoteButton button;
	private double progress;
	
	public RollNote(double noteLength, boolean evenRow) {
		this.evenRow = evenRow;
		setLayout(new GridLayout(1, 1));
		add(button = new NoteButton());
		setPreferredSize(new Dimension((int) Math.round(noteLength * Const.ROLL_SIZE.width), Const.ROLL_SIZE.height));
		progress = 0;
	}
	
	void setSelected(boolean selected) {
		button.setSelected(selected);
	}
	
	boolean isSelected() {
		return button.isSelected();
	}

	public void setProgress(double progress) {
		this.progress = progress;
		button.repaint();
	}
	
	private class NoteButton extends JToggleButton {

		private static final long serialVersionUID = 1L;
		
		NoteButton() { 
			setBorder(Const.HOLE_BORDER);
			setFocusable(false);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			// Paint base color
			g.setColor(evenRow ? Const.ROLLNOTE_COLOR_EVEN : Const.ROLLNOTE_COLOR_ODD);
			if(isSelected()) {
				g.setColor(g.getColor().darker());
			}
			Insets border = getInsets();
			Dimension size = new Dimension(getWidth() - border.left - border.right, getHeight() - border.top - border.bottom);
			g.fillRect(border.left, border.top, size.width, size.height);
			// If played paint progress
			if(isSelected()) {
				int width = (int) (progress * (getWidth() - border.left - border.right));
				g.setColor(evenRow ? Const.ROLLNOTE_COLOR2_EVEN : Const.ROLLNOTE_COLOR2_ODD);
				g.fillRect(border.left, border.top, width, size.height);
			}
		}
	}
}
