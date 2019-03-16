package main;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.MixerMono;
import com.jsyn.unitgen.UnitOscillator;

import model.Note;
import model.Track;

public class Synth {

	private final Synthesizer synth;
	private final UnitOscillator[] keys;
	private final MixerMono mixer;
	private final LineOut out;
	
	public Synth(Track track, Class<? extends UnitOscillator> unitOsc) {
		(synth = JSyn.createSynthesizer()).start();
		keys = new UnitOscillator[track.numKeys];
		synth.add(mixer = new MixerMono(track.numKeys));
		for (int i = 0; i < keys.length; i++) {
			try {
				synth.add(keys[i] = unitOsc.getDeclaredConstructor().newInstance());
			}
			catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
			keys[i].frequency.set(track.calcFrequencyAt(i));
			keys[i].amplitude.set(0);
			keys[i].output.connect(0, mixer.input, i);
		}
		synth.add(out = new LineOut());
		mixer.output.connect(0, out.input, 0);
		mixer.output.connect(0, out.input, 1);
		out.start();
	}
	
	public void play(Note note) {
		stop();
		note.forEach(key -> play(key));
	}

	public void play(int key) {
		keys[key].amplitude.set(1);
	}
	
	public void stop() {
		for (int i = 0; i < keys.length; i++) {
			stop(i);
		}
	}
	
	public void stop(int key) {
		keys[key].amplitude.set(0);
	}
	
	public double getCurrentTime() {
		return synth.getCurrentTime();
	}
	
	public void sleepUntil(double time) throws InterruptedException {
		synth.sleepUntil(time);
	}
	
	public void dispose() {
		out.stop();
		synth.stop();
	}
}
