package gui;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

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
		threads = Arrays.asList(new Thread(this::audio), new Thread(this::piano), new Thread(this::roll));
		threads.forEach(thread -> thread.setPriority(Thread.MAX_PRIORITY));
		startTime = app.synth.getCurrentTime();
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
			// Resume playing at the current measure
			for(int i = app.measure; i < app.track.measuresCount(); i++) {
				for(Note note : app.track.measure(i)) {
					app.synth.play(note);
					app.synth.sleepUntil(time += note.length.absolute());
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
			SwingUtilities.invokeAndWait(() -> app.piano.rebuildKeys(false));
			for(int i = app.measure; i < app.track.measuresCount(); i++) {
				for(Note note : app.track.measure(i)) {
					// Show played keys as pressed
					SwingUtilities.invokeAndWait(() -> app.piano.hold(note));
					app.synth.sleepUntil(time += note.length.absolute());
				}
			}
		}
		catch (InterruptedException | InvocationTargetException e) {
		}
		finally {
			SwingUtilities.invokeLater(() -> {
				app.piano.rebuildKeys(true);
				app.stopIfPlaying();
			});
		}
	}
	
	private void roll() {
		double time = this.startTime;
		try {
			for(int i = app.measure; i < app.track.measuresCount(); i++) {
				Integer measure = i;
				// Rebuild roll with current measure
				SwingUtilities.invokeAndWait(() -> {
					app.setMeasure(measure);
				});
				for(Note note : app.track.measure(i)) {
					// Paint current note progress
					double start = time;
					double end = time + note.length.absolute();
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
			// Playback finished then rewind
			SwingUtilities.invokeAndWait(() -> app.setMeasure(0));
		}
		catch (InterruptedException | InvocationTargetException e) {
			SwingUtilities.invokeLater(() -> app.roll.clearProgress());
		}
	}
}
