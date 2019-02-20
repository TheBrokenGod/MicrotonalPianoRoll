package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Song {

	public final Audio audio;
	public final String author;
	public final String title;
	public final List<Track> tracks;
	
	public Song(Audio audio, String title, String author) {
		this.audio = audio;
		this.title = title;
		this.author = author;
		this.tracks = new ArrayList<>();
	}

	public void add(Track track) {
		tracks.add(track);
	}
	
	@Override
	public String toString() {
		return (title.length() > 0 ? title + " - " + author + "\n" : "") + "\n\n" +
				tracks.stream().map(Track::toString).collect(Collectors.joining("\n\n"));
	}
}
