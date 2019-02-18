package main;

import java.io.File;

import org.xml.sax.SAXException;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillatorDPW;
import com.jsyn.unitgen.UnitOscillator;

import gui.GUI;
import model.Audio;
import model.Song;
import model.SongReader;
import model.Track;

public class MainClass {
	
	static Synthesizer syn = JSyn.createSynthesizer();
	static UnitOscillator gen = new SawtoothOscillatorDPW();
	static LineOut out = new LineOut();
	static {
		syn.add(gen);
		syn.add(out);
		gen.output.connect(out.input);
		syn.start();
	}
	
	public static void playAudio(Audio audio, Track track) throws InterruptedException {
		double start = syn.getCurrentTime();
		for (int i = 0; i < track.notes.size(); i++)
		{
			gen.frequency.set(audio.get(track.offset, track.notes.get(i).values.get(0)));
			out.start();
			syn.sleepUntil(start += track.notes.get(i).duration());
		}
		syn.stop();
	}
	
	public static void main(String[] args) throws SAXException, InterruptedException {
		Song song = new SongReader(new File("lifeeternal.xml")).read();
		GUI gui = new GUI(new Audio(440, 880, 12));
		playAudio(song.audio, song.tracks.get(0));
		gui.dispose();
	}
}
