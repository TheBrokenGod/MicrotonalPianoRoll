package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Note {

	public final int bpm;
	public final int invLen;
	public final List<Integer> values;
	
	public Note(int bpm, String noteLen) {
		this.bpm = bpm;
		if(!noteLen.endsWith("T")) {
			invLen = Integer.parseInt(noteLen); 
		}
		else {
			invLen = 3 * Integer.parseInt(noteLen.substring(0, noteLen.length() - 1)) / 2;
		}
		values = new ArrayList<>();
	}
	
	public void add(int value) {
		values.add(value);
	}
	
	public double duration() {
		return 240.0 / bpm / invLen;
	}
	
	@Override
	public String toString() {
		return "\t" + invLen + ": " + 
				(values.isEmpty() ? "-" : "[" + values.stream().map(value -> value.toString()).collect(Collectors.joining(" ")) + "]");
	}
}
