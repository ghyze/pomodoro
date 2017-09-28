package nl.ghyze.pomodoro.type;

import java.awt.Color;

public enum PomodoroType {
	POMO(new TypePomoHandler()), BREAK(new TypeBreakHandler()), WAIT((new TypeWaitHandler()));

	private AbstractPomodoroTypeHandler handler;

	private PomodoroType(AbstractPomodoroTypeHandler handler) {
		this.handler = handler;
	}

	public Color getBackgroundColor() {
		return handler.getBackgroundColor();
	}

	public float getFontSize() {
		return handler.getFontSize();
	}
}