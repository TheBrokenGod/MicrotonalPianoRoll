package app;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.List;

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

import model.NoteLength;
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
	
	private void buildMenus() {
		JMenu menu, sub, sub2;
		add(menu = new JMenu("Track"));
		menu.add(buildMenuItem("New", app::newFile, KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildMenuItem("Open", app::openFile, KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildMenuItem("Save", app::saveFile, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildMenuItem("Save as", app::saveFileAs, KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		menu.addSeparator();
		menu.add(buildMenuItem("Audio frequencies", app::setAudio, KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		menu.add(buildMenuItem("Generating oscillator", app::setOscillator, KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK));
		JMenuItem item = new JMenuItem("Play");
		item.addActionListener(e -> app.startOrStop());
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
		menu.add(item);
		menu.addSeparator();
		menu.add(buildMenuItem("Exit", () -> System.exit(0), KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));		
		add(menu = new JMenu("Navigation"));
		menu.add(sub = new JMenu("Measure"));
		sub.add(buildMenuItem("Previous", app::previousMeasure, KeyEvent.VK_LEFT, null));
		sub.add(buildMenuItem("Next / New", app::nextMeasure, KeyEvent.VK_RIGHT, null));
		sub.addSeparator();
		sub.add(buildMenuItem("First", app::firstMeasure, KeyEvent.VK_HOME, null));
		sub.add(buildMenuItem("Last", app::lastMeasure, KeyEvent.VK_END, null));
		sub.addSeparator();
		sub.add(buildMenuItem("By index", app::byIndex, KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));
		menu.add(sub = new JMenu("Tempo change"));
		sub.add(buildMenuItem("Previous", app::previousTempo, KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK));
		sub.add(buildMenuItem("Next", app::nextTempo, KeyEvent.VK_RIGHT, KeyEvent.CTRL_DOWN_MASK));
		add(menu = new JMenu("Composition"));
		menu.add(sub = new JMenu("Piano keys"));
		sub.add(buildMenuItem("Write into measure", app::writePianoKeysIntoRoll, KeyEvent.VK_ENTER, null));
		sub.add(buildMenuItem("Release", app::releasePianoKeys, KeyEvent.VK_ESCAPE, null));
		sub.addSeparator();
		sub.add(buildMenuItem("Move up", () -> app.piano.moveChordUp(), KeyEvent.VK_W, null));
		sub.add(buildMenuItem("Move down", () -> app.piano.moveChordDown(), KeyEvent.VK_S, null));
		sub.addSeparator();
		sub.add(buildMenuItem("Move highest up", () -> app.piano.moveHighestKeyUp(), KeyEvent.VK_E, null));
		sub.add(buildMenuItem("Move highest down", () -> app.piano.moveHighestKeyDown(), KeyEvent.VK_Q, null));
		sub.add(buildMenuItem("Move lowest up", () -> app.piano.moveLowestKeyUp(), KeyEvent.VK_D, null));
		sub.add(buildMenuItem("Move lowest down", () -> app.piano.moveLowestKeyDown(), KeyEvent.VK_A, null));
		menu.add(sub = new JMenu("Roll of notes"));
		sub.add(sub2 = new JMenu("Resolution"));
		for(String note : NoteLength.NAMES_NORMAL) {
			sub2.add(buildRadioItem(app, note, group));
		}
		sub2.addSeparator();
		for(String note : NoteLength.NAMES_THIRD) {
			sub2.add(buildRadioItem(app, note, group));
		}
		sub2.addSeparator();
		sub2.add(buildMenuItem("Increase", this::increaseResolution, KeyEvent.VK_UP, null));
		sub2.add(buildMenuItem("Decrease", this::decreaseResolution, KeyEvent.VK_DOWN, null));
		sub.addSeparator();
		sub.add(buildMenuItem("Insert measure", app::insertMeasure, KeyEvent.VK_INSERT, null));
		sub.add(buildMenuItem("Delete measure", app::deleteMeasure, KeyEvent.VK_DELETE, null));
		sub.addSeparator();
		sub.add(buildMenuItem("Set new tempo", app::setTempoChange, KeyEvent.VK_INSERT, KeyEvent.CTRL_DOWN_MASK));
		sub.add(buildMenuItem("Clear tempo", app::clearTempoChange, KeyEvent.VK_DELETE, KeyEvent.CTRL_DOWN_MASK));
	}
	
	private JMenuItem buildMenuItem(String text, Runnable action, Integer keyCode, Integer modifiers) {
		JMenuItem item = new JMenuItem(text);
		item.addActionListener(e -> {
			if(!app.isPlaying()) {
				action.run();
			}
			else {
				app.stopIfPlaying();
			}
		});
		if(keyCode != null) {
			item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers != null ? modifiers : 0));
		}
		return item;
	}
	
	private static JRadioButtonMenuItem buildRadioItem(App app, String text, ButtonGroup group) {
		JRadioButtonMenuItem radio = new JRadioButtonMenuItem(text);
		radio.getModel().setActionCommand(text);
		group.add(radio);
		radio.addActionListener(e -> {
			if(!app.isPlaying()) {
				app.resolutionChanged(e.getActionCommand());
			}
			else {
				app.stopIfPlaying();
			}
		});
		return radio;
	}
	
	void setMeasure(int measure, boolean tempoChange) {
		this.measure.setText("Measure " + (measure + 1) + (tempoChange ? "*" : "") + " of " + track.measuresCount() + "  ");
		// Keep resolution menu radio selection consistent with the last note in the measure
		getResolutionRadio(track.measure(measure).lastNote().length.name()).setSelected(true);
	}
	
	private AbstractButton getResolutionRadio(String resolution) {
		Enumeration<AbstractButton> buttons = group.getElements();
		while(buttons.hasMoreElements()) {
			AbstractButton button = buttons.nextElement();
			if(button.getText().equals(resolution)) {
				return button;
			}
		}
		return null;
	}
	
	String getResolution() {
		return group.getSelection().getActionCommand();
	}
	
	private void increaseResolution() {
		changeResolution(+1);
	}
	
	private void decreaseResolution() {
		changeResolution(-1);
	}
	
	private void changeResolution(int offset) {
		List<String> notes;
		if(NoteLength.NAMES_NORMAL.contains(getResolution())) {
			notes = NoteLength.NAMES_NORMAL;
		}
		else {
			notes = NoteLength.NAMES_THIRD;
		}
		int index = notes.indexOf(getResolution()) + offset;
		if(index < 0 || index >= notes.size()) {
			return;
		}
		getResolutionRadio(notes.get(index)).doClick();
	}
}
