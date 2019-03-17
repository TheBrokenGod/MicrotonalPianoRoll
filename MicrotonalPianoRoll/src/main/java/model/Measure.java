package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Measure implements Iterable<Note> {

	private int bpm;
	private final List<Note> notes;
	
	public Measure(int bpm) {
		this.bpm = bpm;
		notes = new ArrayList<>();
	}
	
	public Measure(int bpm, String fillRes) {
		this(bpm);
		fill(fillRes);
	}
	
	public boolean isFull() {
		return freeSpace() == 0;
	}
	
	public Measure add(Note note) {
		if(freeSpace() < note.length.logical()) {
			throw new IllegalArgumentException(note + " does not fit the misure");
		}
		notes.add(note);
		note.length.measure = this;
		return this;
	}

	public Measure addAll(List<Note> notes) {
		notes.forEach(note -> add(note));
		return this;
	}
	
	public Measure fill(String length) {
		while(!isFull()) {
			add(new Note(length));
		}
		return this;
	}
	
	public Note note(int note) {
		return notes.get(note);
	}
	
	public int notesCount() {
		return notes.size();
	}
	
	public double freeSpace() {
		return 1.0 - notes.stream().mapToDouble(note -> note.length.logical()).sum();
	}

	public Note remove(int note) {
		return notes.remove(note);
	}

	public Note lastNote() {
		return notes.get(notes.size() - 1);
	}

	@Override
	public Iterator<Note> iterator() {
		return notes.iterator();
	}
	
	public int getBPM() {
		return bpm;
	}
	
	public void setBPM(int bpm) {
		this.bpm = bpm;
	}
}
