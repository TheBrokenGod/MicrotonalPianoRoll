package main;

import java.util.HashSet;
import java.util.Set;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.MixerMono;
import com.jsyn.unitgen.UnitOscillator;

import model.Track;

public class Synth {

	public final Synthesizer synth;
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
	
	public void play(Set<Integer> chord) {
		for (int i = 0; i < keys.length; i++) {
			keys[i].amplitude.set(chord.contains(i) ? 1 : 0);
		}
	}
	
	public void mute() {
		play(new HashSet<>());
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
