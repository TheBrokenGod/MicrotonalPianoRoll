package main;

import org.xml.sax.SAXException;

import gui.GUI;
import model.Audio;
import model.Track;

public class MainClass {
	
	public static void main(String[] args) throws SAXException, InterruptedException {
//		Track track = new SongReader(new File("lifeeternal.xml")).read();
		GUI gui = new GUI(new Track(new Audio(1, 1, 1, 0, 10), null));
//		gui.play();
	}
}
