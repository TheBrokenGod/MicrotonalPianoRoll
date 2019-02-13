package model;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Audio {

	private final SortedMap<String, Double> table = new TreeMap<>();
	
	/**
	 * 
	 * @param lowerFrequency
	 * @param higherFrequency
	 * @param stepsInBetween
	 * @param lowestStepsFromLower
	 * @param keysCount
	 * @param lowestKeyNote
	 */
	public Audio(double lowerFrequency, double higherFrequency, int stepsInBetween, int lowestStepsFromLower, int keysCount, String lowestKeyNote) {
		// Compute the base coefficient from known informations
		double coefficient = Math.pow(higherFrequency / lowerFrequency, 1.0 / stepsInBetween);
		// Move back or forward to the actual requested minimum
		lowerFrequency = lowerFrequency * Math.pow(coefficient, lowestStepsFromLower);
		// Generate the table of frequencies
		String key = lowestKeyNote;
		for(int i = 0; i < keysCount; i++) {
			table.put(key, Math.pow(coefficient, i) * lowerFrequency);
			key = key + "+";
		}
	}
	
	/**
	 * 
	 * @param lowestFrequency
	 * @param highestFrequency
	 * @param stepsInBetween
	 * @param lowestKeyNote
	 */
	public Audio(double lowestFrequency, double highestFrequency, int stepsInBetween, String lowestKeyNote) {
		this(lowestFrequency, highestFrequency, stepsInBetween, 0, stepsInBetween + 1, lowestKeyNote);
	}
	
	public int keysCount() {
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
		return String.join("\n", 
			table.entrySet().stream()
			.map(entry -> entry.getKey() + "\t" + String.format("%.3f Hz", entry.getValue()))
			.collect(Collectors.toList())
		);
	}
}
