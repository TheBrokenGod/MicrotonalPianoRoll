package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

class RollValue extends JPanel {

	private static final long serialVersionUID = 1L;
	
	final NoteButton button;
	private double progress;
	
	public RollValue(App app, int note, int row, double length) {
		setLayout(new GridLayout(1, 1));
		add(button = new NoteButton(app, note, row));
		setPreferredSize(new Dimension((int) Math.round(length * Const.ROLL_SIZE.width), Const.ROLL_SIZE.height));
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
	
	class NoteButton extends JToggleButton implements ActionListener {

		private static final long serialVersionUID = 1L;

		private final App app;
		private final int note;
		private final int row;
		
		NoteButton(App app, int note, int row) {
			this.app = app;
			this.note = note;
			this.row = row;
			setBorder(Const.HOLE_BORDER);
			setFocusable(false);
			addActionListener(this);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			// Paint base color
			g.setColor(row % 2 == 0 ? Const.ROLLNOTE_COLOR_EVEN : Const.ROLLNOTE_COLOR_ODD);
			if(isSelected()) {
				g.setColor(g.getColor().darker());
			}
			Insets border = getInsets();
			Dimension size = new Dimension(getWidth() - border.left - border.right, getHeight() - border.top - border.bottom);
			g.fillRect(border.left, border.top, size.width, size.height);
			// If played paint progress
			if(isSelected()) {
				int width = (int) (progress * (getWidth() - border.left - border.right));
				g.setColor(row % 2 == 0 ? Const.ROLLNOTE_COLOR2_EVEN : Const.ROLLNOTE_COLOR2_ODD);
				g.fillRect(border.left, border.top, width, size.height);
			}
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			app.noteChanged(note, row, isSelected());
		}
	}
}
