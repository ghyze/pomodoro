package nl.ghyze.pomodoro.type;

import java.awt.Color;

public enum PomodoroType {
	POMO(new TypePomoHandler()), 
	BREAK(new TypeBreakHandler()), 
	WAIT((new TypeWaitHandler()));

	private final AbstractPomodoroTypeHandler handler;

	PomodoroType(AbstractPomodoroTypeHandler handler) {
		this.handler = handler;
	}

	public Color getBackgroundColor() {
		return handler.getBackgroundColor();
	}

	public float getFontSize() {
		return handler.getFontSize();
	}
	
	public boolean isPomo(){
		return this == POMO;
	}
	
	public boolean isBreak(){
		return this == BREAK;
	}
	
	public boolean isWait(){
		return this == WAIT;
	}
}