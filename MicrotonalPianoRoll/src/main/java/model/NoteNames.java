package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteNames {
	
	private static final List<String> BASE = Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");
	private static final List<String> NAMES = new ArrayList<>();	
	static {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 12; j++) {
				NAMES.add(BASE.get(j) + i);
			}
		}
	}
	
	public static String[] all() {
		return NAMES.toArray(new String[NAMES.size()]);
	}
	
	public static String[] range(String firstName, int namesCount) {
		int startIndex = NAMES.indexOf(firstName);
		return NAMES.subList(startIndex, startIndex + namesCount).toArray(new String[namesCount]);
	}
	
	public static int compare(String note1, String note2) {
		int octave1 = Integer.parseInt(note1.substring(note1.length() - 1, note1.length()));
		int octave2 = Integer.parseInt(note2.substring(note2.length() - 1, note2.length()));
		if(octave1 != octave2) {
			return octave1 - octave2;
		}
		return BASE.indexOf(note1.substring(0, note1.length() - 1)) - BASE.indexOf(note2.substring(0, note2.length() - 1));
	}
}
