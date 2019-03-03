package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import model.Measure;

class Roll extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final int numRows;
	final ProgressBar progress;
	
	Roll(int numRows) {
		setLayout(new GridLayout(numRows + 1, 1));
		progress = new ProgressBar(this);
		this.numRows = numRows;
		setPreferredSize(new Dimension(Const.ROLL_SIZE.width, (numRows + 1) * Const.ROLL_SIZE.height));
	}
	
	public void setActiveMeasure(Measure measure) {
		removeAll();
		add(progress);
		for(int i = 0; i < numRows; i++) {
			JPanel row = new JPanel();
			row.setLayout(new BoxLayout(row, BoxLayout.LINE_AXIS));
			for (int j = 0; j < measure.notes.size(); j++) {
				row.add(buildRollHole(measure.notes.get(j).logicalLength(), i % 2 == 0));				
			}
			add(row);
		}
		revalidate();
	}
	
	private static Component buildRollHole(double length, boolean evenRow) {
		JPanel hole = new JPanel(new GridLayout(1, 1));
		hole.setPreferredSize(new Dimension((int) Math.round(length * Const.ROLL_SIZE.width), Const.ROLL_SIZE.height));
		hole.add(new Hole(evenRow));
		return hole;
	}
}
