package gui;

import java.awt.GridLayout;
import java.util.Set;

import javax.swing.JPanel;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillator;

import main.KeySynth;
import model.Audio;

class Keyboard extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final boolean vertical;
	private final Set<Key> activeKeys;
	Synthesizer jsyn = JSyn.createSynthesizer();
	LineOut out = new LineOut();
	private final KeySynth synth;
	
	Keyboard(Audio audio, boolean vertical) {
		this.vertical = vertical;
		this.activeKeys = new CheckedHashSet<>();
		setLayout(vertical ? new GridLayout(audio.numKeys, 1) : new GridLayout(1, audio.numKeys));
		for (int i = 0; i < audio.numKeys; i++) {
			add(new Key(this, i));
		}
		// TODO refactor
		synth = new KeySynth(audio, SawtoothOscillator.class);
		synth.addTo(jsyn);
		jsyn.add(out);
		synth.output.connect(0, out.input, 0);
		synth.output.connect(0, out.input, 1);
		jsyn.start();
		out.start();
	}
	
	boolean isVertical() {
		return vertical;
	}
	
	void keyActive(Key key) {
		synth.enable(key.index);
		System.err.println("enable " + key);
	}
	
	void keyInactive(Key key) {
		synth.disable(key.index);
		System.err.println("disable " + key);
	}
}
