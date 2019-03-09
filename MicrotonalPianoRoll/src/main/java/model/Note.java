package model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Note {

	private final int len;
	public final int bpm;
	public final Set<Integer> values;
	
	public Note(String len, int bpm) {
		this.bpm = bpm;
		if(!len.endsWith("T")) {
			this.len = Integer.parseInt(len); 
		}
		else {
			this.len = 3 * Integer.parseInt(len.substring(0, len.length() - 1)) / 2;
		}
		values = new HashSet<>();
	}
	
	public void add(int value) {
		values.add(value);
	}
	
	public double logicalLength() {
		return 1.0 / len;
	}
	
	public double duration() {
		return 240.0 / bpm / len;
	}
	
	@Override
	public String toString() {
		return len + ": " + 
				(values.isEmpty() ? "-" : "[" + values.stream().map(value -> value.toString()).collect(Collectors.joining(" ")) + "]");
	}
}
