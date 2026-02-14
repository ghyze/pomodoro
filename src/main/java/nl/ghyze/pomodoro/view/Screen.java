package nl.ghyze.pomodoro.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.awt.DisplayMode;
import java.awt.Point;
import java.awt.Rectangle;

@Builder
@AllArgsConstructor
@Getter
public class Screen {

	private int index;
	private DisplayMode mode;
	private Rectangle availableArea;

	public String toString(){
		return "Screen: "+ (index+1)+" ("+mode.getWidth()+", "+mode.getHeight()+")";
	}

	public Point getGraphicsDeviceOffset() {
		return new Point(availableArea.x, availableArea.y);
	}

	public Point getMostBottomRightPoint() {
		return new Point(availableArea.x+availableArea.width, availableArea.y+availableArea.height);
	}
}
