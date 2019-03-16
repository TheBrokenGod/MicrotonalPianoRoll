package model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class Note implements Iterable<Integer> {

	private final Set<Integer> values;
	public final NoteLength length;
	
	public Note(String length) {
		this.length = new NoteLength(length);
		values = new HashSet<>();
	}
	
	public void add(int value) {
		values.add(value);
	}
	
	public boolean contains(int value) {
		return values.contains(value);
	}
	
	public void remove(int value) {
		values.remove(value);
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return values.iterator();
	}
	
	@Override
	public String toString() {
		return length.toString() + ": " + 
				(values.isEmpty() ? "-" : "[" + values.stream().map(value -> value.toString()).collect(Collectors.joining(" ")) + "]");
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}
}
