package main;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Notes {
	
	public static class Builder {
		
		private Double coefficient;
		private Double lowestFrequency;
		private Integer numNotes;
		
		/**
		 * Creates a NotesBuilder from two well-known frequencies and the relative distance between them.
		 * 
		 * These frequencies don't have to be the actual lowest and highest that will be generated,
		 * but simply two notes which frequencies, as well as relative distance is known.
		 * 
		 * == EXAMPLES ==
		 * • new NotesBuilder(220, 440, 12) constructs a builder for the standard [A3-A4] notes interval 
		 * • new NotesBuilder(220, 880, 24) behaves in the same way but producing a larger [A3-A5] interval
		 * • new NotesBuilder(216, 432, 12) will build an interval similar to the first but for 432Hz music
		 * 
		 * @param lowFrequency		The low frequency
		 * @param highFrequency		The high frequency
		 * @param stepsInBetween	The steps (often semitones) between them
		 */
		public Builder(double lowFrequency, double highFrequency, int stepsInBetween) {
			if(lowFrequency <= 0 || highFrequency <= lowFrequency || stepsInBetween <= 0) {
				throw new IllegalArgumentException();
			}
			coefficient = Math.pow(highFrequency / lowFrequency, 1.0 / stepsInBetween);
			this.lowestFrequency = lowFrequency;
			this.numNotes = stepsInBetween + 1;
		}
		
		/**
		 * Sets how many steps far from the low frequency passed to the constructor the actual lowest note will be.
		 * 
		 * For example, 'new NotesBuilder(220, 440, 12)' and 'new NotesBuilder(440, 880, 12).startingAt(-12)' 
		 * will both produce the same [A3-A4] standard notes interval.
		 * 
		 * If not called, it defaults to 0.
		 * 
		 * @param stepsFromLow	Steps back or forward from the low frequency passed to the constructor. 
		 * @return				The builder itself
		 */
		public Builder startingAt(int stepsFromLow) {
			lowestFrequency *= Math.pow(coefficient, stepsFromLow);
			return this;
		}
		
		/**
		 * Sets how many notes will be generated.
		 * 
		 * For example 'new NotesBuilder(220, 440, 12)' and 'new NotesBuilder(220, 880, 24).withTotalNotes(13)' will both produce 
		 * the same [A3-A4] standard interval.
		 * 
		 * If not called, it defaults to 'stepsInBetween + 1' of what was passed to the constructor.
		 * 
		 * @param numNotes	The number of notes to be generated
		 * @return			The builder itself
		 */
		public Builder withTotalNotes(int numNotes) {
			if(numNotes < 1) {
				throw new IllegalArgumentException();
			}
			this.numNotes = numNotes;
			return this;
		}
		
		/**
		 * Builds the notes calculating their frequencies.
		 * 
		 * @return The notes
		 */
		public Notes build() {
			double[] freqs = new double[numNotes];
			for(int i = 0; i < numNotes; i++) {
				freqs[i] = Math.pow(coefficient, i) * lowestFrequency;
			}
			return new Notes(freqs);
		}	
	}
	
	public final List<Double> frequencies;
	
	private Notes(double[] frequencies) {
		this.frequencies = Collections.unmodifiableList(Arrays.stream(frequencies).mapToObj(freq -> Double.valueOf(freq)).collect(Collectors.toList()));
	}
	
	@Override
	public String toString() {
		return String.join(",", frequencies.stream().map(freq -> freq.toString()).collect(Collectors.toList()));
	}
}
