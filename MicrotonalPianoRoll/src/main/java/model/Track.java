package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Track {

	public final int offset;
	public final String name;
	public final List<Note> notes;
	
	public Track(String name, int offset) {
		this.name = name;
		this.offset = offset;
		this.notes = new ArrayList<>();
	}

	public void add(Note note) {
		notes.add(note);
	}
	
	@Override
	public String toString() {
		return name + "\n" + notes.stream().map(note -> note.toString()).collect(Collectors.joining("\n"));
	}
}
