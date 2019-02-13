package main;

import org.xml.sax.SAXException;

import gui.GUI;
import model.Audio;

public class MainClass {
	
	public static void main(String[] args) throws SAXException {
		System.out.println(new Audio(432, 864, 12, -12, 25, "A3"));
		System.out.println();
		System.out.println(new Audio(440, 880, 12, -12, 25, "A3"));
		System.out.println();
		System.out.println(new Audio(256, 512, 12, 0, 13, "C4"));
		new GUI(new Audio(440, 880, 12, -12, 25, "A4"));
	}
}
