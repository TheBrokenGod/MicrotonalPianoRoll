package main;

import java.io.File;

import org.xml.sax.SAXException;

import gui.App;
import model.TrackReader;
import model.Track;

public class MainClass {
	
	public static void main(String[] args) throws SAXException, InterruptedException {
		Track track = new TrackReader(new File("lifeeternal.xml")).read();
		new App(track);
	}
}
