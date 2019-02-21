package main;

import java.util.Arrays;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.MixerMono;
import com.jsyn.unitgen.UnitOscillator;

import model.Audio;

public class KeySynth {

	private final UnitOscillator[] keys;
	private final MixerMono mixer;
	public final UnitOutputPort output;
	
	public KeySynth(Audio audio, Class<? extends UnitOscillator> unitOsc) {
		this.keys = new UnitOscillator[audio.numKeys];
		this.mixer = new MixerMono(audio.numKeys);
		for (int i = 0; i < keys.length; i++) {
			try {
				keys[i] = unitOsc.getDeclaredConstructor().newInstance();
			}
			catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
			keys[i].frequency.set(audio.calcFrequencyAt(i));
			keys[i].output.connect(0, mixer.input, i);
			keys[i].amplitude.set(0);
		}
		this.output = mixer.output;
	}
	
	public void enable(int key) {
		keys[key].amplitude.set(1);
	}
	
	public void disable(int key) {
		keys[key].amplitude.set(0);		
	}
	
	public void addTo(Synthesizer synth) {
		Arrays.stream(keys).forEach(key -> synth.add(key));
		synth.add(mixer);
	}
	
	public void removeFrom(Synthesizer synth) {
		synth.remove(mixer);
		Arrays.stream(keys).forEach(key -> synth.remove(key));
	}
}
