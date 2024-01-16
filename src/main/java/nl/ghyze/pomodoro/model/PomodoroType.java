package nl.ghyze.pomodoro.model;

import nl.ghyze.pomodoro.optiondialog.OptionDialogModel;

import java.awt.Color;

public enum PomodoroType {
	POMO(Color.RED, 30f, "Pomodoro finished", "Pomodoro finished. What would you like to do with this one?", new Object[] {OptionDialogModel.Choice.SAVE, OptionDialogModel.Choice.DISCARD, OptionDialogModel.Choice.CONTINUE_ACTION }),
	BREAK(new Color(0, 192, 0), 30f, "Break finished", "Ready to start next one?", new Object[] { OptionDialogModel.Choice.OK, OptionDialogModel.Choice.CANCEL }),
	WAIT(Color.BLUE, 16f, "", "", new Object[0]);

	private final Color backgroundColor;
	private final float fontSize;

	private final String dialogTitle;
	private final String dialogMessage;
	private final Object[] dialogChoices;

	PomodoroType(Color backgroundColor, float fontSize, String dialogTitle, String dialogMessage, Object[] dialogChoices){
		this.backgroundColor = backgroundColor;
		this.fontSize = fontSize;
		this.dialogTitle = dialogTitle;
		this.dialogMessage = dialogMessage;
		this.dialogChoices = dialogChoices;
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

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public float getFontSize() {
		return fontSize;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

	public String getDialogMessage() {
		return dialogMessage;
	}

	public Object[] getDialogChoices() {
		return dialogChoices;
	}
}
