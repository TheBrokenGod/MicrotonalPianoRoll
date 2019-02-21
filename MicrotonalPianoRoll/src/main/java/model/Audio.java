package model;

public class Audio {

	private final double coefficient;
	private final double lowestFreq;
	public final int numKeys;
		
	public Audio(double lowerFrequency, double higherFrequency, int stepsInBetween, int lowestOffsetFromLower, int keysCount) {
		numKeys = keysCount;
		// Compute the coefficient needed to move in the frequencies table
		coefficient = Math.pow(higherFrequency / lowerFrequency, 1.0 / stepsInBetween);
		// Start at the specified position from lower frequency
		lowestFreq = lowerFrequency * Math.pow(coefficient, lowestOffsetFromLower);
	}
	
	public double calcFrequencyAt(int noteIndex) {
		return lowestFreq * Math.pow(coefficient, noteIndex);
	}
	
//	@Override
//	public String toString() {
//		return table.entrySet().stream()
//			.map(entry -> entry.getKey() + "\t" + String.format("%.2f Hz", entry.getValue()))
//			.collect(Collectors.joining("\n"));
//	}
}
