package nl.ghyze.pomodoro.view;

import lombok.Builder;

import java.awt.DisplayMode;
import java.awt.Point;
import java.awt.Rectangle;

@Builder
public record Screen(int index, DisplayMode mode, Rectangle availableArea) {

	@Override
	public String toString() {
		return "Screen: " + (index + 1) + " (" + mode.getWidth() + ", " + mode.getHeight() + ")";
	}

	public Point getGraphicsDeviceOffset() {
		return new Point(availableArea.x, availableArea.y);
	}

	public Point getMostBottomRightPoint() {
		return new Point(availableArea.x + availableArea.width, availableArea.y + availableArea.height);
	}
}
