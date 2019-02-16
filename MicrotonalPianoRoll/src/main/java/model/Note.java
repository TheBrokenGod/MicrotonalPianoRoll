package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Note {

	public final int duration;
	public final List<Integer> values;
	
	public Note(int duration) {
		this.duration = duration;
		this.values = new ArrayList<>();
	}
	
	public void add(int value) {
		values.add(value);
	}
	
	@Override
	public String toString() {
		return "\t" + duration + ": " + 
				(values.isEmpty() ? "-" : "[" + values.stream().map(value -> value.toString()).collect(Collectors.joining(" ")) + "]");
	}
}
