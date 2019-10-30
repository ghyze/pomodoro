package nl.ghyze.pomodoro.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.type.PomodoroType;

public class PomoButtonFactory {
	
	private static int controlButtonWidth = 18;
	private static int controlButtonHeight = 18;

	public static PomoButton createStopButton(final PomoController controller) {
	
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
		stopButton.setAction(controller::stopCurrent);
	
		return stopButton;
	}

	public static PomoButton createPlayButton(final PomoController controller) {
	
		BufferedImage image = new BufferedImage(controlButtonWidth, controlButtonHeight, BufferedImage.TYPE_INT_ARGB);
	
		Graphics gr = image.getGraphics();
		gr.setColor(Color.white);
		gr.drawRect(0, 0, controlButtonWidth - 1, controlButtonHeight - 1);
	
		Polygon pol = new Polygon();
		pol.addPoint(controlButtonWidth / 4, controlButtonHeight / 4);
		pol.addPoint(controlButtonWidth / 4, controlButtonHeight - (controlButtonHeight / 4));
		pol.addPoint(controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight / 2);
	
		gr.fillPolygon(pol);
	
		PomoButton playButton = new PomoButton(22, 80, 18, 18);
		playButton.addVisibleType(PomodoroType.WAIT);
		playButton.setImage(image);
		playButton.setAction(controller::startPomo);
	
		return playButton;
	}

	public static PomoButton createCloseButton(final PomoController controller) {
	
		BufferedImage image = new BufferedImage(controlButtonWidth, controlButtonHeight, BufferedImage.TYPE_INT_ARGB);
	
		Graphics gr = image.getGraphics();
		gr.setColor(Color.white);
		gr.drawRect(0, 0, controlButtonWidth - 1, controlButtonHeight - 1);
		gr.drawLine(controlButtonWidth / 4, controlButtonHeight / 4, controlButtonWidth - (controlButtonWidth / 4),
				controlButtonHeight - (controlButtonHeight / 4));
		gr.drawLine(controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight / 4, controlButtonWidth / 4,
				controlButtonHeight - (controlButtonHeight / 4));
	
		PomoButton closeButton = new PomoButton(118, 2, 18, 18);
		closeButton.addVisibleType(PomodoroType.BREAK);
		closeButton.addVisibleType(PomodoroType.POMO);
		closeButton.addVisibleType(PomodoroType.WAIT);
		closeButton.setImage(image);
		closeButton.setAction(controller::stopProgram);
		return closeButton;
	}

	public static PomoButton createMinimizeButton(final PomoFrame frame) {
	
		BufferedImage image = new BufferedImage(controlButtonWidth, controlButtonHeight, BufferedImage.TYPE_INT_ARGB);
	
		Graphics gr = image.getGraphics();
		gr.setColor(Color.white);
		gr.drawRect(0, 0, controlButtonWidth - 1, controlButtonHeight - 1);
		gr.drawLine(controlButtonWidth / 4, controlButtonHeight - (controlButtonHeight / 4),
				controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight - (controlButtonHeight / 4));
	
		PomoButton minimizeButton = new PomoButton(98, 2, 18, 18);
		minimizeButton.addVisibleType(PomodoroType.BREAK);
		minimizeButton.addVisibleType(PomodoroType.POMO);
		minimizeButton.addVisibleType(PomodoroType.WAIT);
		minimizeButton.setImage(image);
		minimizeButton.setAction(() -> frame.setVisible(false));
	
		return minimizeButton;
	}

}
