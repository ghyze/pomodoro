package nl.ghyze.pomodoro.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import nl.ghyze.pomodoro.controller.PomoAction;
import nl.ghyze.pomodoro.model.PomodoroType;

public class PomoButtonFactory {

	private static final int controlButtonWidth = 18;
	private static final int controlButtonHeight = 18;

	/**
	 * Creates a BufferedImage with white border, ready for custom drawing.
	 *
	 * @param drawContent lambda that draws the button-specific content
	 * @return BufferedImage with border and custom content
	 */
	private static BufferedImage createButtonImage(final Consumer<Graphics> drawContent) {
		final BufferedImage image = new BufferedImage(controlButtonWidth, controlButtonHeight, BufferedImage.TYPE_INT_ARGB);
		final Graphics gr = image.getGraphics();
		gr.setColor(Color.white);
		gr.drawRect(0, 0, controlButtonWidth - 1, controlButtonHeight - 1);
		drawContent.accept(gr);
		return image;
	}

	/**
	 * Creates and configures a PomoButton with common settings.
	 *
	 * @param x x-position
	 * @param y y-position
	 * @param image button image
	 * @param action button action
	 * @param visibleTypes types when button is visible
	 * @return configured PomoButton
	 */
	private static PomoButton createButton(final int x, final int y, final BufferedImage image,
			final PomoAction action, final PomodoroType... visibleTypes) {
		final PomoButton button = new PomoButton(x, y, controlButtonWidth, controlButtonHeight);
		for (final PomodoroType type : visibleTypes) {
			button.addVisibleType(type);
		}
		button.setImage(image);
		button.setAction(action);
		return button;
	}

	public static PomoButton createStopButton(final PomoAction action) {
		final BufferedImage image = createButtonImage(gr -> {
			gr.fillRect(controlButtonWidth / 4, controlButtonHeight / 4,
					(controlButtonWidth / 2) + 1, (controlButtonHeight / 2) + 1);
		});
		return createButton(2, 80, image, action, PomodoroType.BREAK, PomodoroType.POMO);
	}

	public static PomoButton createPlayButton(final PomoAction action) {
		final BufferedImage image = createButtonImage(gr -> {
			final Polygon pol = new Polygon();
			pol.addPoint(controlButtonWidth / 4, controlButtonHeight / 4);
			pol.addPoint(controlButtonWidth / 4, controlButtonHeight - (controlButtonHeight / 4));
			pol.addPoint(controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight / 2);
			gr.fillPolygon(pol);
		});
		return createButton(22, 80, image, action, PomodoroType.WAIT);
	}

	public static PomoButton createCloseButton(final PomoAction action) {
		final BufferedImage image = createButtonImage(gr -> {
			gr.drawLine(controlButtonWidth / 4, controlButtonHeight / 4,
					controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight - (controlButtonHeight / 4));
			gr.drawLine(controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight / 4,
					controlButtonWidth / 4, controlButtonHeight - (controlButtonHeight / 4));
		});
		return createButton(118, 2, image, action, PomodoroType.BREAK, PomodoroType.POMO, PomodoroType.WAIT);
	}

	public static PomoButton createMinimizeButton(final PomoFrame frame) {
		final BufferedImage image = createButtonImage(gr -> {
			gr.drawLine(controlButtonWidth / 4, controlButtonHeight - (controlButtonHeight / 4),
					controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight - (controlButtonHeight / 4));
		});
		return createButton(98, 2, image, () -> frame.setVisible(false),
				PomodoroType.BREAK, PomodoroType.POMO, PomodoroType.WAIT);
	}

	public static PomoButton createTasksButton(final PomoAction action) {
		final BufferedImage image = createButtonImage(gr -> {
			final int lineHeight = controlButtonHeight / 4;
			for (int i = 1; i <= 3; i++) {
				gr.drawLine(3, lineHeight * i, 3, lineHeight * i);
				gr.drawLine(6, lineHeight * i, controlButtonWidth - 3, lineHeight * i);
			}
		});
		return createButton(118, 80, image, action, PomodoroType.BREAK, PomodoroType.POMO, PomodoroType.WAIT);
	}

}
