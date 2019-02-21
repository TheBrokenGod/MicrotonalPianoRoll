package main;

import java.io.File;

import org.xml.sax.SAXException;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;

import gui.GUI;
import model.SongReader;
import model.Track;

public class MainClass {
	
	public static void main(String[] args) throws SAXException, InterruptedException {
		Track track = new SongReader(new File("lifeeternal.xml")).read();
//		synth.output.disconnect(OUT.input);
//		synth.removeFrom(SYNTH);
		GUI gui = new GUI(track.audio);
//		Thread g = Player.overdriveGuitar(SYNTH, time, song.audio, song.tracks.get(0), OUT.input);
//		Thread b = Player.fingeredBass(SYNTH, time, song.audio, song.tracks.get(1), OUT.input);
//		g.start();
//		b.start();
//		gui.dispose();
	}
}
