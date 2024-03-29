package nl.ghyze.pomodoro.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import nl.ghyze.pomodoro.controller.PomoAction;
import nl.ghyze.pomodoro.model.PomodoroType;

public class PomoButtonFactory {
	
	private static final int controlButtonWidth = 18;
	private static final int controlButtonHeight = 18;

	public static PomoButton createStopButton(final PomoAction action) {
	
		BufferedImage image = new BufferedImage(controlButtonWidth, controlButtonHeight, BufferedImage.TYPE_INT_ARGB);
	
		Graphics gr = image.getGraphics();
		gr.setColor(Color.white);
		gr.drawRect(0, 0, controlButtonWidth - 1, controlButtonHeight - 1);
		gr.fillRect(controlButtonWidth / 4, controlButtonHeight / 4, (controlButtonWidth / 2) + 1,
				(controlButtonHeight / 2) + 1);
	
		PomoButton stopButton = new PomoButton(2, 80, controlButtonWidth, controlButtonHeight);
		stopButton.addVisibleType(PomodoroType.BREAK);
		stopButton.addVisibleType(PomodoroType.POMO);
		stopButton.setImage(image);
		stopButton.setAction(action);
	
		return stopButton;
	}

	public static PomoButton createPlayButton(final PomoAction action) {
	
		BufferedImage image = new BufferedImage(controlButtonWidth, controlButtonHeight, BufferedImage.TYPE_INT_ARGB);
	
		Graphics gr = image.getGraphics();
		gr.setColor(Color.white);
		gr.drawRect(0, 0, controlButtonWidth - 1, controlButtonHeight - 1);
	
		Polygon pol = new Polygon();
		pol.addPoint(controlButtonWidth / 4, controlButtonHeight / 4);
		pol.addPoint(controlButtonWidth / 4, controlButtonHeight - (controlButtonHeight / 4));
		pol.addPoint(controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight / 2);
	
		gr.fillPolygon(pol);
	
		PomoButton playButton = new PomoButton(22, 80, controlButtonWidth, controlButtonHeight);
		playButton.addVisibleType(PomodoroType.WAIT);
		playButton.setImage(image);
		playButton.setAction(action);
	
		return playButton;
	}

	public static PomoButton createCloseButton(final PomoAction action) {
	
		BufferedImage image = new BufferedImage(controlButtonWidth, controlButtonHeight, BufferedImage.TYPE_INT_ARGB);
	
		Graphics gr = image.getGraphics();
		gr.setColor(Color.white);
		gr.drawRect(0, 0, controlButtonWidth - 1, controlButtonHeight - 1);
		gr.drawLine(controlButtonWidth / 4, controlButtonHeight / 4, controlButtonWidth - (controlButtonWidth / 4),
				controlButtonHeight - (controlButtonHeight / 4));
		gr.drawLine(controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight / 4, controlButtonWidth / 4,
				controlButtonHeight - (controlButtonHeight / 4));
	
		PomoButton closeButton = new PomoButton(118, 2, controlButtonWidth, controlButtonHeight);
		closeButton.addVisibleType(PomodoroType.BREAK);
		closeButton.addVisibleType(PomodoroType.POMO);
		closeButton.addVisibleType(PomodoroType.WAIT);
		closeButton.setImage(image);
		closeButton.setAction(action);
		return closeButton;
	}

	public static PomoButton createMinimizeButton(final PomoFrame frame) {
	
		BufferedImage image = new BufferedImage(controlButtonWidth, controlButtonHeight, BufferedImage.TYPE_INT_ARGB);
	
		Graphics gr = image.getGraphics();
		gr.setColor(Color.white);
		gr.drawRect(0, 0, controlButtonWidth - 1, controlButtonHeight - 1);
		gr.drawLine(controlButtonWidth / 4, controlButtonHeight - (controlButtonHeight / 4),
				controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight - (controlButtonHeight / 4));
	
		PomoButton minimizeButton = new PomoButton(98, 2, controlButtonWidth, controlButtonHeight);
		minimizeButton.addVisibleType(PomodoroType.BREAK);
		minimizeButton.addVisibleType(PomodoroType.POMO);
		minimizeButton.addVisibleType(PomodoroType.WAIT);
		minimizeButton.setImage(image);
		minimizeButton.setAction(() -> frame.setVisible(false));
	
		return minimizeButton;
	}

}
