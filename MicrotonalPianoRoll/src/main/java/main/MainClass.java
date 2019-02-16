package main;

import java.io.File;

import org.xml.sax.SAXException;

import gui.GUI;
import model.Audio;
import model.SongReader;

public class MainClass {
	
	public static void main(String[] args) throws SAXException {
		System.out.println(new SongReader(new File("lifeeternal.xml")).read());
		new GUI(new Audio(440, 880, 12, -12, 25));
	}
}
