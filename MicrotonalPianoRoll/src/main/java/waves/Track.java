package waves;
import waves.Wave;

public class Track {

	public final Wave wave;
	private final int samplingHz;
	private final int duration;
	
	public Track(Wave wave, int samplingHz, int duration) {
		this.wave = wave;
		this.samplingHz = samplingHz;
		this.duration = duration;
	}
	
	public double absT(int sample) {
		return (double)sample / samplingHz;
	}
	
	public short[] generate16bit() {
		short[] data = new short[duration * samplingHz];
		for(int sample = 0; sample < data.length; sample++) {
			data[sample] = wave.shortSample(absT(sample));
		}
		return data;
	}
	
	public byte[] generate8bit() {
		byte[] data = new byte[duration * samplingHz];
		for(int sample = 0; sample < data.length; sample++) {
			data[sample] = wave.byteSample(absT(sample));
		}
		return data;
	}
}
