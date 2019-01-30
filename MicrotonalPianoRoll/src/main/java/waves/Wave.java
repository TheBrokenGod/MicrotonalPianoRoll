package waves;

abstract public class Wave {

	private final String name;
	protected final int hz;
	
	protected Wave(String name, int hz) {
		this.name = name;
		this.hz = hz;
	}
	
	abstract protected double sampleAt(double t);
	
	public final byte byteSample(double t, double volume) {
		double raw = volume * sampleAt(t);
		return (byte)((raw >= 0 ? raw * 127 : -raw * -128) + 128);
	}
	
	public final short shortSample(double t, double volume) {
		double raw = volume * sampleAt(t);
		return (short)(raw >= 0 ? raw * Short.MAX_VALUE : -raw * Short.MIN_VALUE);
	}
	
	public final byte byteSample(double t) {
		return byteSample(t, 1.0);
	}
	
	public final short shortSample(double t) {
		return shortSample(t, 1.0);
	}
	
	public final String getName() {
		return name;
	}
}
