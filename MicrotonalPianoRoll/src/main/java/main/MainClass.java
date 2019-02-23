package main;

import java.io.File;

import org.xml.sax.SAXException;

import gui.GUI;
import model.SongReader;
import model.Track;

public class MainClass {
	
	public static void main(String[] args) throws SAXException, InterruptedException {
		Track track = new SongReader(new File("lifeeternal.xml")).read();
		GUI gui = new GUI(track);
		gui.play();
		Thread.sleep(1000);
		gui.stop();
		gui.play();
	}
}
