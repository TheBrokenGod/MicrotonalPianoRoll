package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Track implements Iterable<Measure> {
	
	public final double lowerFrequency;
	public final double higherFrequency;
	public final int stepsInBetween;
	public final int lowestOffset;
	public final int numKeys;
	private final double coefficient;
	private final List<Measure> measures = new ArrayList<Measure>();

	public Track(double lowerFrequency, double higherFrequency, int stepsInBetween, int lowestOffset, int numKeys) {
		this.lowerFrequency = lowerFrequency;
		this.higherFrequency = higherFrequency;
		this.stepsInBetween = stepsInBetween;
		this.lowestOffset = lowestOffset;
		this.numKeys = numKeys;
		// Compute the coefficient needed to move in the frequencies table
		coefficient = Math.pow(higherFrequency / lowerFrequency, 1.0 / stepsInBetween);
	}
	
	public double calcFrequencyAt(int noteIndex) {
		// The index is shifted by the offset specified during construction
		return lowerFrequency * Math.pow(coefficient, lowestOffset + noteIndex);
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

	public void insert(int index, Measure measure) {
		measures.add(index, measure);
	}
	
	public void remove(int index) {
		measures.remove(index);
	}

	public int measuresCount() {
		return measures.size();
	}
	
	@Override
	public Iterator<Measure> iterator() {
		return measures.iterator();
	}
	
	public void copyTo(Track track) {
		track.measures.clear();
		track.measures.addAll(measures);
	}
}
