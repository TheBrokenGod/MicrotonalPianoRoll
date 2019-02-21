package main;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.unitgen.MixerMono;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.UnitOscillator;

import model.Audio;
import model.Note;
import model.Track;

public class Player extends Thread {

	private final Synthesizer synth;
	private final Audio audio;
	private final Track track;
	private final UnitOscillator[] units;
	private final MixerMono mixer;
	private final UnitInputPort output;
	private double time;
	
	private Player(Synthesizer synth, double time, Audio audio, Track track, UnitInputPort output, Class<? extends UnitOscillator> unitOsc, int numOsc) {
		this.synth = synth;
		this.audio = audio;
		this.track = track;
		this.output = output;
		units = new UnitOscillator[numOsc];
		mixer = new MixerMono(numOsc);
		for (int i = 0; i < units.length; i++) {
			try {
				synth.add(units[i] = unitOsc.getDeclaredConstructor().newInstance());
			}
			catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
			units[i].setEnabled(false);
			units[i].output.connect(0, mixer.input, i);
		}
		synth.add(mixer);
		mixer.output.connect(output);
		this.time = time;
	}
	
	public static Player overdriveGuitar(Synthesizer synth, double startTime, Audio audio, Track track, UnitInputPort output) {
		return new Player(synth, startTime, audio, track, output, SawtoothOscillator.class, 6);
	}
	
	public static Player fingeredBass(Synthesizer synth, double startTime, Audio audio, Track track, UnitInputPort output) {
		return new Player(synth, startTime, audio, track, output, SineOscillator.class, 1);
	}
	
	@Override
	public void run() {
		try {
			for(Note note : track.notes) {
				for (int i = 0; i < units.length; i++) {
					if(i < note.values.size()) {
						units[i].setEnabled(true);
						units[i].frequency.set(audio.calcFrequencyAt(note.values.get(i)));
					}
					else {
						units[i].setEnabled(false);					
					}
				}
				synth.sleepUntil(time += note.duration());
			}
		} 
		catch (InterruptedException e) {
		}
		finally {
			mixer.output.disconnect(output);
			synth.remove(mixer);
			for (int i = 0; i < units.length; i++) {
				synth.remove(units[i]);
			}
		}
	}
}
