package main;

import gui.GUI;

public class MainClass {
	
	public static void main(String[] args) throws InterruptedException {
		new GUI(35, 440.1356, 880.456);
		test();
//		double[] song = {
//			55,
//			55,
//			87.307,
//			87.307,
//			55, 
//			55, 
//			48.999,
//			51.913
//		};
//		Synthesizer synth = JSyn.createSynthesizer();
//		synth.start();
//		UnitOscillator osc = new SineOscillator();
//		LineOut lineOut = new LineOut();
//		synth.add(osc);
//		synth.add(lineOut);
//		osc.output.connect(0, lineOut.input, 0);
//		lineOut.start();
//		for(double note : song) {
//			osc.noteOn(note, 1);
//			synth.sleepFor(1);
//			osc.noteOff();
//		}
//		synth.stop();
//		test();
//		System.err.println(ArithmeticUtils.gcd(440, 1000));
//		Track track = new Track(new SineWave(1), SAMPLING_HZ, DURATION_S);
//		List<Double> t = new ArrayList<>();
//		List<Byte> a = new ArrayList<>();
//		byte[] temp = track.generate8bit();
//		for(int i = 0; i < DURATION_S * SAMPLING_HZ; i++) {
//			t.add(track.absT(i));
//			a.add(Byte.valueOf(temp[i]));
//		}
//		XYChart chart = QuickChart.getChart(track.wave.getName(), "T", "A", ".", t, a);
//		new SwingWrapper<XYChart>(chart).displayChart();
	}
	
	private static void test() {
		Notes first = new Notes.Builder(110, 440, 24).startingAt(-33).withTotalNotes(120).build();
		Notes second = new Notes.Builder(220, 440, 12).startingAt(-45).withTotalNotes(120).build();
		double max = 0;
		for(int i = 0; i < 120; i++) {
			max = Math.max(max, Math.abs(first.frequencies.get(i) - second.frequencies.get(i)));
		}
		System.err.println(max);
	}
}
