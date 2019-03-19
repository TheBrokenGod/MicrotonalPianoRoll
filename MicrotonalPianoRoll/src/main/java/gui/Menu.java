package gui;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import model.Track;

class Menu extends JMenuBar {

	private static final long serialVersionUID = 1L;
	
	private final App app;
	private final Track track;
	private final JLabel measure;
	private final ButtonGroup group;
	
	Menu(App app, Track track) {
		this.app = app;
		this.track = track;
		group = new ButtonGroup();
		buildMenus();
		JPanel panel = new JPanel(new GridLayout(1, 1));
		panel.setOpaque(false);
		panel.add(measure = new JLabel("", SwingConstants.RIGHT));
		add(panel);
	}
	
	void setMeasure(int measure, boolean tempoChange) {
		this.measure.setText("Measure " + (measure + 1) + (tempoChange ? "*" : "") + " of " + track.measuresCount() + " ");
		// Keep resolution radio consistent with the last note in the measure
		Enumeration<AbstractButton> buttons = group.getElements();
		while(buttons.hasMoreElements()) {
			AbstractButton res = buttons.nextElement();
			if(res.getText().equals(track.measure(measure).lastNote().length.name())) {
				res.setSelected(true);
				break;
			}
		}
	}
	
	String getResolution() {
		return group.getSelection().getActionCommand();
	}
	
	private void buildMenus() {
		JMenu menu, sub;
		add(menu = new JMenu("Track"));
		menu.add(buildMenuItem("New", app::newFile, KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildMenuItem("Open", app::openFile, KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildMenuItem("Save", app::saveFile, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildMenuItem("Save as", app::saveFileAs, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		menu.addSeparator();
		menu.add(buildMenuItem("Audio properties", app::setAudio, KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildMenuItem("Unit oscillator", app::setOscillator, KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildMenuItem("Play", app::startOrStop, KeyEvent.VK_SPACE, 0));
		menu.addSeparator();
		menu.add(buildMenuItem("Exit", () -> System.exit(0), KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));		
		add(menu = new JMenu("Navigation"));
		menu.add(sub = new JMenu("Measure"));
		sub.add(buildMenuItem("Previous", app::previousMeasure, KeyEvent.VK_LEFT, 0));
		sub.add(buildMenuItem("Next / New", app::nextMeasure, KeyEvent.VK_RIGHT, 0));
		sub.addSeparator();
		sub.add(buildMenuItem("First", app::firstMeasure, KeyEvent.VK_HOME, 0));
		sub.add(buildMenuItem("Last", app::lastMeasure, KeyEvent.VK_END, 0));
		sub.addSeparator();
		sub.add(buildMenuItem("By index", app::byIndex, KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));
		menu.add(sub = new JMenu("Tempo change"));
		sub.add(buildMenuItem("Previous", app::previousTempo, KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK));
		sub.add(buildMenuItem("Next", app::nextTempo, KeyEvent.VK_RIGHT, KeyEvent.CTRL_DOWN_MASK));
		add(menu = new JMenu("Composition"));
		menu.add(sub = new JMenu("Resolution"));
		sub.add(buildRadioItem(app, "1", group));
		sub.add(buildRadioItem(app, "2", group));
		sub.add(buildRadioItem(app, "4", group));
		sub.add(buildRadioItem(app, "8", group));
		sub.add(buildRadioItem(app, "16", group));
		sub.add(buildRadioItem(app, "32", group));
		sub.addSeparator();
		sub.add(buildRadioItem(app, "2T", group));
		sub.add(buildRadioItem(app, "4T", group));
		sub.add(buildRadioItem(app, "8T", group));
		sub.add(buildRadioItem(app, "16T", group));
		sub.add(buildRadioItem(app, "32T", group));
		sub.addSeparator();
		sub.add(buildMenuItem("Increase", null, KeyEvent.VK_PLUS, KeyEvent.CTRL_DOWN_MASK));
		sub.add(buildMenuItem("Decrease", null, KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK));
		menu.add(sub = new JMenu("Measure"));
		sub.add(buildMenuItem("Insert", app::insertMeasure, KeyEvent.VK_INSERT, null));
		sub.add(buildMenuItem("Delete", app::deleteMeasure, KeyEvent.VK_DELETE, null));
		menu.add(sub = new JMenu("Tempo change"));
		sub.add(buildMenuItem("Set", app::setTempoChange, KeyEvent.VK_INSERT, KeyEvent.CTRL_DOWN_MASK));
		sub.add(buildMenuItem("Clear", app::clearTempoChange, KeyEvent.VK_DELETE, KeyEvent.CTRL_DOWN_MASK));
		add(menu = new JMenu("About"));
		menu.add(buildMenuItem("Info", null, null, null));
	}
	
	private static JMenuItem buildMenuItem(String text, Runnable action, Integer keyCode, Integer modifiers) {
		JMenuItem item = new JMenuItem(text);
		if(action != null) {
			item.addActionListener(e -> action.run());
		}
		if(keyCode != null) {
			item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers != null ? modifiers : 0));
		}
		return item;
	}
	
	private static JRadioButtonMenuItem buildRadioItem(App app, String text, ButtonGroup group) {
		JRadioButtonMenuItem radio = new JRadioButtonMenuItem(text);
		radio.getModel().setActionCommand(text);
		group.add(radio);
		radio.addActionListener(e -> app.resolutionChanged());
		return radio;
	}
}
