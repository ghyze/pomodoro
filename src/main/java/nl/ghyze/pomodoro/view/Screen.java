package nl.ghyze.pomodoro.view;

import java.awt.DisplayMode;
import java.awt.Point;
import java.awt.Rectangle;

public class Screen {

	private int index;
	private DisplayMode mode;
	private Rectangle availableArea;
	
	public Screen(DisplayMode mode, int index, Rectangle availableArea){
		this.mode = mode;
		this.index = index;
		this.availableArea = availableArea;
	}
	
	public String toString(){
		return "Screen: "+ (index+1)+" ("+mode.getWidth()+", "+mode.getHeight()+")";
	}
	
	public int getIndex(){
		return index;
	}
	
	public Point getGraphicsDeviceOffset() {
		return new Point(availableArea.x, availableArea.y);
	}

	public Point getMostBottomRightPoint() {
		return new Point(availableArea.x+availableArea.width, availableArea.y+availableArea.height);
	}
}
