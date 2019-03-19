package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteLength {
	
	public static final List<String> NAMES_NORMAL = Collections.unmodifiableList(Arrays.asList("1", "2", "4", "8", "16", "32"));
	public static final List<String> NAMES_THIRD = Collections.unmodifiableList(Arrays.asList("2T", "4T", "8T", "16T", "32T"));
	private static final List<String> NAMES = new ArrayList<String>();
	private static final Map<String, Integer> NAME_TO_INV_LEN = new HashMap<>();
	static {
		NAMES.addAll(NAMES_NORMAL);
		NAMES.addAll(NAMES_THIRD);
		for(String name : NAMES) {
			if(!name.endsWith("T")) {
				NAME_TO_INV_LEN.put(name, Integer.parseInt(name));
			}
			else {
				NAME_TO_INV_LEN.put(name, Math.round(3.f * Integer.parseInt(name.substring(0, name.length() - 1)) / 2.f));
			}
		};
	}
	
	private final String name;
	Measure measure;
	
	public NoteLength(String name) {
		this.name = name;
	}
	
	public double logical() {
		return 1.0 / NAME_TO_INV_LEN.get(name);
	}
	
	public double absolute() {
		return 240.0 / measure.getBPM() / NAME_TO_INV_LEN.get(name);
	}
	
	public static int inverse(String name) {
		return NAME_TO_INV_LEN.get(name);
	}
	
	public String name() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
