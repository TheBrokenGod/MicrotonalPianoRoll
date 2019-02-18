package model;

public class Audio {

	private final double lowerFrequency;
	private final double coefficient;
	
	/**
	 * 
	 * @param lowerFrequency
	 * @param higherFrequency
	 * @param stepsInBetween
	 */
	public Audio(double lowerFrequency, double higherFrequency, int stepsInBetween) {
		this.lowerFrequency = lowerFrequency;
		// Compute the coefficient needed to move in the frequencies table
		coefficient = Math.pow(higherFrequency / lowerFrequency, 1.0 / stepsInBetween);
	}
	
	public double get(int baseOffsetFromLower, int noteIndex) {
		return lowerFrequency * Math.pow(coefficient, baseOffsetFromLower + noteIndex);
	}
	
//	@Override
//	public String toString() {
//		return table.entrySet().stream()
//			.map(entry -> entry.getKey() + "\t" + String.format("%.2f Hz", entry.getValue()))
//			.collect(Collectors.joining("\n"));
//	}
}
