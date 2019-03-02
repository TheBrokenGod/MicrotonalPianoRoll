package model;

import java.util.ArrayList;
import java.util.List;

import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.SaxonApiUncheckedException;

public class Track {

	private final double coefficient;
	public final double lowestFreq;
	public final int numKeys;
	public final List<Measure> measures = new ArrayList<Measure>();

	public Track(double lowerFrequency, double higherFrequency, int stepsInBetween, int lowestOffsetFromLower, int keysCount) {
		numKeys = keysCount;
		// Compute the coefficient needed to move in the frequencies table
		coefficient = Math.pow(higherFrequency / lowerFrequency, 1.0 / stepsInBetween);
		// Start at the specified position from lower frequency
		lowestFreq = lowerFrequency * Math.pow(coefficient, lowestOffsetFromLower);
	}
	
	public double calcFrequencyAt(int noteIndex) {
		return lowestFreq * Math.pow(coefficient, noteIndex);
	}

	public void add(Note note) {
		if(measures.isEmpty() || measures.get(measures.size() - 1).isFull()) {
			measures.add(new Measure());
		}
		try {
			measures.get(measures.size() - 1).add(note);
		} 
		catch (SaxonApiException e) {
			throw new SaxonApiUncheckedException(e);
		}
	}
	
//	@Override
//	public String toString() {
//		return notes.stream().map(note -> note.toString()).collect(Collectors.joining("\n"));
//	}
}
