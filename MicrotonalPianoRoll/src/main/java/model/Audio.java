package model;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Audio {

	private final SortedMap<Integer, Double> table = new TreeMap<>();
	
	/**
	 * 
	 * @param lowerFrequency
	 * @param higherFrequency
	 * @param stepsInBetween
	 * @param lowestStepsFromLower
	 * @param keysCount
	 * @param lowestKeyNote
	 */
	public Audio(double lowerFrequency, double higherFrequency, int stepsInBetween, int lowestStepsFromLower, int keysCount) {
		// Compute the base coefficient from known informations
		double coefficient = Math.pow(higherFrequency / lowerFrequency, 1.0 / stepsInBetween);
		// Move back or forward to the actual requested minimum
		lowerFrequency = lowerFrequency * Math.pow(coefficient, lowestStepsFromLower);
		// Generate the table of frequencies
		for(int i = 0; i < keysCount; i++) {
			table.put(i, Math.pow(coefficient, i) * lowerFrequency);
		}
	}
	
	/**
	 * 
	 * @param lowestFrequency
	 * @param highestFrequency
	 * @param stepsInBetween
	 * @param lowestKeyNote
	 */
	public Audio(double lowestFrequency, double highestFrequency, int stepsInBetween) {
		this(lowestFrequency, highestFrequency, stepsInBetween, 0, stepsInBetween + 1);
	}
	
	public int numEntries() {
		return table.size();
	}
	
	public double lowestFrequency() {
		return table.get(table.firstKey());
	}
	
	public double highestFrequency() {
		return table.get(table.lastKey());
	}
	
	@Override
	public String toString() {
		return table.entrySet().stream()
			.map(entry -> entry.getKey() + "\t" + String.format("%.2f Hz", entry.getValue()))
			.collect(Collectors.joining("\n"));
	}
}
