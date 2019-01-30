package waves;

public class SineWave extends Wave {

	public SineWave(int hz) {
		super("sine", hz);
	}

	@Override
	protected double sampleAt(double t) {
		return Math.sin(2 * Math.PI * hz * t);
	}
}
