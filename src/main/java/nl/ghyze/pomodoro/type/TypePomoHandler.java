package nl.ghyze.pomodoro.type;

import java.awt.Color;

public class TypePomoHandler extends AbstractPomodoroTypeHandler {

	@Override
	public Color getBackgroundColor() {
		return Color.RED;
	}

	@Override
	public float getFontSize() {
		return 30f;
	}

}
