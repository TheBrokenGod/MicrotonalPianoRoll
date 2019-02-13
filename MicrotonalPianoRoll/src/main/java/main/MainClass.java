package main;

import gui.GUI;
import model.Audio;

public class MainClass {
	
	public static void main(String[] args) throws InterruptedException {
		new GUI(new Audio(220, 880, 24, "C2"));
	}
}
