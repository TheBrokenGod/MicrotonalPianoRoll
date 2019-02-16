package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Song {

	public final int bpm;
	public final String author;
	public final String title;
	public final List<Track> tracks;
	
	public Song(Audio audio, int bpm, String title, String author) {
		this.bpm = bpm;
		this.title = title;
		this.author = author;
		this.tracks = new ArrayList<>();
	}

	public void add(Track track) {
		tracks.add(track);
	}
	
	@Override
	public String toString() {
		return (title.length() > 0 ? title + " - " + author + "\n" : "") +
				"BPM: " + bpm + "\n\n" +
				tracks.stream().map(Track::toString).collect(Collectors.joining("\n\n"));
	}
}
