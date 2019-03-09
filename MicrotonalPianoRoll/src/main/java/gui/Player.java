package gui;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import model.Measure;
import model.Note;

class Player {

	private final GUI gui;
	private List<Thread> threads;
	private double startTime;
	
	Player(GUI gui) {
		this.gui = gui;
		threads = null;
		startTime = 0;
	}
	
	void start() {
		threads = Arrays.asList(new Thread(this::audio), new Thread(this::piano), new Thread(this::roll));
		threads.forEach(thread -> thread.setPriority(Thread.MAX_PRIORITY));
		startTime = gui.synth.getCurrentTime();
		threads.forEach(thread -> thread.start());
	}

	public boolean isPlaying() {
		return threads != null;
	}
	
	void stop() {
		threads.forEach(thread -> thread.interrupt());
		threads.forEach(thread -> {
			try {
				thread.join();
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
		threads = null;
	}
	
	private void audio() {
		double time = this.startTime;
		try {
			for(Measure measure : gui.track.measures) {
				for(Note note : measure.notes) {
					gui.synth.play(note.values);
					gui.synth.sleepUntil(time += note.duration());
				}
			}
		}
		catch (InterruptedException e) {
		}
		finally {
			gui.synth.mute();
		}
	}
	
	private void piano() {
		double time = this.startTime;
		try {
			for(Measure measure : gui.track.measures) {
				for(Note note : measure.notes) {
					SwingUtilities.invokeAndWait(() -> gui.piano.play(note.values));
					gui.synth.sleepUntil(time += note.duration());
				}
			}
		}
		catch (InterruptedException | InvocationTargetException e) {
		}
		finally {
			gui.piano.mute();
		}
	}
	
	private void roll() {
		double time = this.startTime;
		try {
			for(Measure measure : gui.track.measures) {
				// Rebuild roll with current measure
				SwingUtilities.invokeAndWait(() -> {
					gui.roll.setMeasure(measure);
				});
				for(Note note : measure.notes) {
					// Paint current note progress
					double start = time;
					double end = time + note.duration();
					while(gui.synth.getCurrentTime() < end) {
						SwingUtilities.invokeAndWait(() -> {
							double progress = (gui.synth.getCurrentTime() - start) / (end - start);
							gui.roll.setProgress(note, progress);
						});
					}
					// Avoid wasting time if last note of measure
					SwingUtilities.invokeLater(() -> gui.roll.setProgress(note, 1.0));
					time = end;
				}
			}
		}
		catch (InterruptedException | InvocationTargetException e) {
		}
		finally {
			SwingUtilities.invokeLater(() -> {
				gui.roll.setMeasure(gui.track.measures.get(0));
			});
		}
	}
}
