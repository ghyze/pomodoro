package nl.ghyze.pomodoro.type;

import java.awt.Color;

public class TypeWaitHandler extends AbstractPomodoroTypeHandler {

	@Override
	public Color getBackgroundColor() {
		return Color.BLUE;
	}

	@Override
	public float getFontSize() {
		return 16f;
	}

}
