package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

class Roll extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final int numRows;
	final ProgressBar progress;
	
	Roll(int numRows, int numCols) {
		super(new GridLayout(numRows + 1, 1));
		progress = new ProgressBar(this);
		this.numRows = numRows;
		rebuild(numCols);
		setPreferredSize(new Dimension(4 * Const.HOLE_SIZE_4.width, (numRows + 1) * Const.HOLE_SIZE_4.height));
	}
	
	void rebuild(int numCols) {
		removeAll();
		add(progress);
		for(int i = 0; i < numRows; i++) {
			JPanel row = new JPanel();
			row.setLayout(new BoxLayout(row, BoxLayout.LINE_AXIS));
			for (int j = 0; j < numCols; j++) {
				row.add(buildRollHole(i % 2 == 0));
			}
			add(row);
		}
	}
	
	private static Component buildRollHole(boolean evenRow) {
		JPanel hole = new JPanel(new GridLayout(1, 1));
		hole.add(new Hole(evenRow));
		return hole;
	}
}
