package gui;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import model.Measure;
import model.Note;

class Player {

	private final App app;
	private List<Thread> threads;
	private double startTime;
	
	Player(App app) {
		this.app = app;
		threads = null;
		startTime = 0;
	}
	
	void start() {
		if(threads != null) {
			stop();
		}
		threads = Arrays.asList(new Thread(this::audio), new Thread(this::piano), new Thread(this::roll));
		threads.forEach(thread -> thread.setPriority(Thread.MAX_PRIORITY));
		startTime = app.synth.getCurrentTime();
		threads.forEach(thread -> thread.start());
	}

	public boolean isPlaying() {
		return threads != null && threads.get(0).isAlive();
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
			for(Measure measure : app.track.measures) {
				for(Note note : measure.notes) {
					app.synth.play(note);
					app.synth.sleepUntil(time += note.duration());
				}
			}
		}
		catch (InterruptedException e) {
		}
		finally {
			app.synth.stop();
		}
	}
	
	private void piano() {
		double time = this.startTime;
		try {
			// Disable piano input during playback
			SwingUtilities.invokeAndWait(() -> app.setInteractive(false));
			for(Measure measure : app.track.measures) {
				for(Note note : measure.notes) {
					// Press played keys
					SwingUtilities.invokeAndWait(() -> app.piano.play(note));
					app.synth.sleepUntil(time += note.duration());
				}
			}
		}
		catch (InterruptedException | InvocationTargetException e) {
		}
		finally {
			app.piano.stop();
			SwingUtilities.invokeLater(() -> app.setInteractive(true));
		}
	}
	
	private void roll() {
		double time = this.startTime;
		try {
			for(Measure measure : app.track.measures) {
				// Rebuild roll with current measure
				SwingUtilities.invokeAndWait(() -> {
					app.roll.setMeasure(measure);
					app.bar.setMeasure(measure);
				});
				for(Note note : measure.notes) {
					// Paint current note progress
					double start = time;
					double end = time + note.duration();
					while(app.synth.getCurrentTime() < end) {
						SwingUtilities.invokeAndWait(() -> {
							double progress = (app.synth.getCurrentTime() - start) / (end - start);
							app.roll.setProgress(note, progress);
						});
					}
					// Avoid wasting time if last note of measure
					SwingUtilities.invokeLater(() -> app.roll.setProgress(note, 1.0));
					time = end;
				}
			}
		}
		catch (InterruptedException | InvocationTargetException e) {
		}
		finally {
			SwingUtilities.invokeLater(() -> {
				app.roll.setMeasure(app.track.measures.get(0));
				app.bar.setMeasure(app.track.measures.get(0));
			});
		}
	}
}
