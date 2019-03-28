package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteLength {
	
	public static final List<String> NAMES_NORMAL = Collections.unmodifiableList(Arrays.asList("1", "2", "4", "8", "16", "32"));
	public static final List<String> NAMES_THIRD = Collections.unmodifiableList(Arrays.asList("2T", "4T", "8T", "16T", "32T"));
	public static final Map<String, Integer> NAME_TO_INV_LEN;
	static {
		Map<String, Integer> nameToInvLen = new HashMap<>();
		for(String name : NAMES_NORMAL) {
			nameToInvLen.put(name, Integer.parseInt(name));
		}
		for(String name : NAMES_THIRD) {
			nameToInvLen.put(name, Math.round(3.f * Integer.parseInt(name.substring(0, name.length() - 1)) / 2.f));
		}
		NAME_TO_INV_LEN = Collections.unmodifiableMap(nameToInvLen);
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
	
	public int inverse() {
		return inverse(name);
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
