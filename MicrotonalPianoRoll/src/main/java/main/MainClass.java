package main;

import java.io.File;

import org.xml.sax.SAXException;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;

import model.Song;
import model.SongReader;

public class MainClass {

	private static final Synthesizer SYNTH;
	private static final LineOut OUT;
	static {
		SYNTH = JSyn.createSynthesizer();
		SYNTH.start();
		SYNTH.add(OUT = new LineOut());
		OUT.start();
	}
	
	public static void main(String[] args) throws SAXException, InterruptedException {
		Song song = new SongReader(new File("lifeeternal.xml")).read();
//		GUI gui = new GUI(new Audio(440, 880, 12));
		double time = SYNTH.getCurrentTime();
		Thread g = Player.overdriveGuitar(SYNTH, time, song.audio, song.tracks.get(0), OUT.input);
		Thread b = Player.fingeredBass(SYNTH, time, song.audio, song.tracks.get(1), OUT.input);
		g.start();
		b.start();
//		gui.dispose();
	}
}
