package nl.ghyze.pomodoro.controller;

import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.Pomodoro.Type;
import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.view.systemtray.SystemTrayManager;

public class PomodoroStateMachine {

	private Pomodoro current;
	private Settings settings;
	private int pomosDone = 0;
	private SystemTrayManager systemTrayManager;

	public PomodoroStateMachine(Settings settings) {
		this.settings = settings;
		current = new Pomodoro(0, Type.WAIT);
	}

	public void setSystemTrayManager(SystemTrayManager systemTrayManager) {
		this.systemTrayManager = systemTrayManager;
	}

	public Pomodoro getCurrent() {
		return current;
	}

	public Pomodoro.Type getCurrentType() {
		return current.getType();
	}

	public boolean shouldChangeState() {
		return (current.getType() != Type.WAIT && current.isDone());
	}

	public void handleAction(int choice) {
		if (current.getType() == Type.POMO) {
			if (choice == 0) {
				pomosDone++;
			}
			Pomodoro next = getNext(Type.POMO);
			current = next;
			updateCurrent();

		} else if (current.getType() == Type.BREAK) {
			if (choice == 0) {
				startPomo();
			} else {
				startWait();
			}
		}

	}

	private Pomodoro getNext(Type type) {
		if (type == Type.POMO) {
			if (pomosDone < settings.getPomosBeforeLongBreak()) {
				String message = "Well done: Short break";
				return new Pomodoro(settings.getShortBreakMinutes(), Type.BREAK);
			} else {
				pomosDone = 0;
				String message = "Well done: Long break";
				return new Pomodoro(settings.getLongBreakMinutes(), Type.BREAK);
			}
		} else if (type == Type.BREAK) {
			String message = "Waiting to start next Pomodoro";
			showMessage(message);
			return new Pomodoro(0, Type.WAIT);
		}
		return null;
	}

	public void stopCurrent() {
		startWait();
	}

	public void startWait() {
		current = new Pomodoro(0, Type.WAIT);
		updateCurrent();
	}

	public void startPomo() {
		current = new Pomodoro(settings.getPomoMinutes(), Type.POMO);
		updateCurrent();
		String message = "Starting Pomodoro number " + (pomosDone + 1);
		showMessage(message);
	}

	private void showMessage(String message) {
		if (systemTrayManager != null) {
			systemTrayManager.message(message);
		}
	}

	public void updateCurrent() {
		current.setPomosDone(pomosDone);
		current.setMaxPomosDone(settings.getPomosBeforeLongBreak());
	}
}
