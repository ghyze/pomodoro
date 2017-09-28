package nl.ghyze.pomodoro.type;

import java.awt.Color;

public class TypeBreakHandler extends AbstractPomodoroTypeHandler {

	private Color backgroundColor = new Color(0, 192, 0);
	
	@Override
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public float getFontSize() {
		return 30f;
	}

}
