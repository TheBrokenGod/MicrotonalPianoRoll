package app;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Track;

class AudioDialog extends JDialog implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	
	private final JTextField lower;
	private final JTextField higher;
	private final JTextField steps;
	private final JTextField offset;
	private final JTextField keys;
	private final JButton confirm;
	private final Consumer<Track> callback;
	
	public AudioDialog(App app, Track initial, Consumer<Track> callback) {
		super(app, "Audio Frequencies", true);
		this.callback = callback;
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setContentPane(panel);
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		JPanel labels = new JPanel(new GridLayout(5, 1));
		JPanel inputs = new JPanel(new GridLayout(5, 1));
		labels.add(new JLabel("Lower frequency"));
		inputs.add(lower = new JTextField(Const.format(initial.lowerFrequency)));
		labels.add(new JLabel("Higher frequency"));
		inputs.add(higher = new JTextField(Const.format(initial.higherFrequency)));
		labels.add(new JLabel("Steps in between"));
		inputs.add(steps = new JTextField(Integer.toString(initial.stepsInBetween)));
		labels.add(new JLabel("First key offset"));
		inputs.add(offset = new JTextField(Integer.toString(initial.lowestOffset)));		
		labels.add(new JLabel("Number of keys"));
		inputs.add(keys = new JTextField(Integer.toString(initial.numKeys)));
		panel.add(labels);
		panel.add(Box.createRigidArea(new Dimension(20, 1)));
		panel.add(inputs);
		lower.addKeyListener(this);
		higher.addKeyListener(this);
		steps.addKeyListener(this);
		offset.addKeyListener(this);
		keys.addKeyListener(this);
		getContentPane().add(panel);
		getContentPane().add(Box.createRigidArea(new Dimension(1, 10)));
		panel = new JPanel(new GridLayout(1, 1));
		confirm = new JButton("OK");
		confirm.addActionListener(this);
		panel.add(confirm);
		getContentPane().add(panel);
		setSize(320, 200);
		setLocationRelativeTo(app);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			double lower = Double.parseDouble(this.lower.getText());
			double higher = Double.parseDouble(this.higher.getText());
			if(lower <= 0 || higher <= lower) {
				throw new IllegalArgumentException("Illegal range " + lower + " Hz - " + higher + " Hz");
			}
			int steps = Integer.parseInt(this.steps.getText());
			if(steps < 1) {
				throw new IllegalArgumentException("Steps in between should be greater than 0");
			}
			int offset = Integer.parseInt(this.offset.getText());
			int keys = Integer.parseInt(this.keys.getText());
			if(keys < 1) {
				throw new IllegalArgumentException("Number of keys should be greater than 0");
			}
			dispose();
			Track track = new Track(lower, higher, steps, offset, keys);
			// Add default content for new file
			Const.defaultTrack().copyTo(track);
			callback.accept(track);
		}
		catch(IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			dispose();
		}
		else if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			confirm.doClick();
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
