package model;

import java.util.ArrayList;
import java.util.List;

import net.sf.saxon.s9api.SaxonApiException;

public class Measure {

	public final List<Note> notes;
	
	public Measure() {
		notes = new ArrayList<>();
	}
	
	public boolean isFull() {
		return freeSpace() == 0;
	}
	
	public void add(Note note) throws SaxonApiException {
		if(freeSpace() < note.logicalLength()) {
			throw new SaxonApiException(note + " does not fit its misure");
		}
		notes.add(note);
	}
	
	public double freeSpace() {
		return 1.0 - notes.stream().mapToDouble(note -> note.logicalLength()).sum();
	}
}
