package waves;

public class SawtoothWave extends Wave {

	public SawtoothWave(int hz) {
		super("sawtooth", hz);
	}

	@Override
	protected double sampleAt(double t) {
		return 2 * (t * hz - Math.floor(t * hz + 0.5));
	}
}
