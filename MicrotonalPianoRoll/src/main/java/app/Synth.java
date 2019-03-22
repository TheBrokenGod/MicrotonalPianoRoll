package app;

import java.util.HashMap;
import java.util.Map;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.ImpulseOscillator;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.MixerMono;
import com.jsyn.unitgen.PulseOscillator;
import com.jsyn.unitgen.RedNoise;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.SawtoothOscillatorBL;
import com.jsyn.unitgen.SawtoothOscillatorDPW;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.UnitOscillator;

import model.Note;
import model.Track;

class Synth {

	private static final Map<String, Class<? extends UnitOscillator>> oscillators;
	static {
		oscillators = new HashMap<>();
		oscillators.put(ImpulseOscillator.class.getSimpleName(), ImpulseOscillator.class);
		oscillators.put(PulseOscillator.class.getSimpleName(), PulseOscillator.class);
		oscillators.put(RedNoise.class.getSimpleName(), RedNoise.class);
		oscillators.put(SawtoothOscillator.class.getSimpleName(), SawtoothOscillator.class);
		oscillators.put(SawtoothOscillatorBL.class.getSimpleName(), SawtoothOscillatorBL.class);
		oscillators.put(SawtoothOscillatorDPW.class.getSimpleName(), SawtoothOscillatorDPW.class);
		oscillators.put(SineOscillator.class.getSimpleName(), SineOscillator.class);
		oscillators.put(SquareOscillator.class.getSimpleName(), SquareOscillator.class);
		oscillators.put(TriangleOscillator.class.getSimpleName(), TriangleOscillator.class);
	}
	
	private final Synthesizer synth;
	private final UnitOscillator[] keys;
	private final MixerMono mixer;
	private final LineOut out;
	
	public Synth(Track track, String unitOsc) {
		(synth = JSyn.createSynthesizer()).start();
		keys = new UnitOscillator[track.numKeys];
		synth.add(mixer = new MixerMono(track.numKeys));
		for (int i = 0; i < keys.length; i++) {
			try {
				synth.add(keys[i] = oscillators.get(unitOsc).getDeclaredConstructor().newInstance());
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
	
	public Synth(Track track) {
		this(track, Synth.defaultOscillator());
	}

	public void play(int key) {
		keys[key].amplitude.set(1);
	}

	public void stop(int key) {
		keys[key].amplitude.set(0);
	}

	public void play(Note note) {
		stop();
		note.forEach(key -> play(key));
	}

	public void stop() {
		for (int i = 0; i < keys.length; i++) {
			stop(i);
		}
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
	
	public String oscillator() {
		return oscillators.keySet().stream().filter(name -> name.equals(keys[0].getClass().getSimpleName())).findAny().get();
	}
	
	public static String[] availableOscillators() {
		return oscillators.keySet().stream().sorted().toArray(size -> new String[size]);
	}
	
	public static String defaultOscillator() {
		return SawtoothOscillator.class.getSimpleName();
	}
}
