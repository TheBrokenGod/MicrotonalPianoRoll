package app;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import model.NoteLength;

class RollHole extends JPanel {

	private static final long serialVersionUID = 1L;
	
	final NoteButton button;
	private double progress;
	
	public RollHole(App app, int noteInd, int rowInd, NoteLength length) {
		setLayout(new GridLayout(1, 1));
		add(button = new NoteButton(app, noteInd, rowInd));
		setPreferredSize(new Dimension((int) Math.round(length.logical() * Const.ROLL_SIZE.width), Const.ROLL_SIZE.height));
		progress = 0;
	}
	
	void setSelected(boolean selected) {
		button.setSelected(selected);
	}
	
	public void setProgress(double progress) {
		this.progress = progress;
		button.repaint();
	}
	
	private class NoteButton extends JToggleButton implements ActionListener {

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
			// Same behaviour as PianoKey
			if(!app.isPlaying()) {
				addActionListener(this);
			}
			else {
				addActionListener(e -> {
					setSelected(false);
					app.stopIfPlaying();
				});
			}
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
			app.rollHoleChanged(note, row, isSelected());
		}
	}
}
