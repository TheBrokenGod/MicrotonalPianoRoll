package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Track implements Iterable<Measure> {

	private final double coefficient;
	private final double lowestFrequency;
	private final List<Measure> measures = new ArrayList<Measure>();
	public final int numKeys;

	public Track(double lowerFrequency, double higherFrequency, int stepsInBetween, int lowestOffsetFromLower, int keysCount) {
		numKeys = keysCount;
		// Compute the coefficient needed to move in the frequencies table
		coefficient = Math.pow(higherFrequency / lowerFrequency, 1.0 / stepsInBetween);
		// Start at the specified position from lower frequency
		lowestFrequency = lowerFrequency * Math.pow(coefficient, lowestOffsetFromLower);
	}
	
	public double calcFrequencyAt(int noteIndex) {
		return lowestFrequency * Math.pow(coefficient, noteIndex);
	}
	
	public Measure measure(int measure) {
		return measures.get(measure);
	}
	
	public Measure firstMeasure() {
		return measures.get(0);
	}
	
	public Measure lastMeasure() {
		return measures.get(measures.size() - 1);
	}

	void add(int bpm, Note note) {
		if(measures.isEmpty() || lastMeasure().isFull()) {
			measures.add(new Measure(bpm));
		}
		lastMeasure().add(note);
	}

	public void add(Measure measure) {
		measures.add(measure);
	}

	public int measuresCount() {
		return measures.size();
	}
	
	@Override
	public Iterator<Measure> iterator() {
		return measures.iterator();
	}
}
