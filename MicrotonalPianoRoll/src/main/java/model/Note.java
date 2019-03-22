package model;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Note implements Iterable<Integer> {

	private final SortedSet<Integer> values;
	public final NoteLength length;
	
	public Note(String length) {
		this.length = new NoteLength(length);
		values = new TreeSet<>();
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
	
	public Stream<Integer> values() {
		return values.stream();
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
