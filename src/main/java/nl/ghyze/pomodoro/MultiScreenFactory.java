package nl.ghyze.pomodoro;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.view.Screen;

public class MultiScreenFactory {
	
	private GraphicsDevice[] availableGraphicsDevices = null;

	public List<Screen> getAvailableScreenList() {
		GraphicsDevice[] graphicsDevices = getAvailableGraphicsDevices();

		List<Screen> screenList = new ArrayList<Screen>();
		for (int i = 0; i < graphicsDevices.length; i++) {
			GraphicsDevice device = graphicsDevices[i];
			DisplayMode mode = device.getDisplayMode();
			Screen screen = new Screen("Screen " + (i + 1) + " (" + mode.getWidth() + ", " + mode.getHeight() + ")", i);
			screenList.add(screen);
		}
		return screenList;
	}

	public Point getGraphicsDeviceOffset(Settings settings) {
		GraphicsDevice[] graphicsDevices = getAvailableGraphicsDevices();
		if (isGraphicsDeviceOtherThanDefault(settings, graphicsDevices)) {
			Rectangle bounds = getBoundsOfSelectedGraphicsDevice(settings, graphicsDevices);
			return new Point(bounds.x, bounds.y);
		} else {
			return new Point(0, 0);
		}
	}

	public Point getMostBottomRightPoint(Settings settings) {
		GraphicsDevice[] graphicsDevices = getAvailableGraphicsDevices();
		if (isGraphicsDeviceOtherThanDefault(settings, graphicsDevices)) {
			Rectangle bounds = getBoundsOfSelectedGraphicsDevice(settings, graphicsDevices);
			return new Point(bounds.width, bounds.height);
		} else {
			Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			return new Point(winSize.x + winSize.width, winSize.y + winSize.height);
		}
	}

	private GraphicsDevice[] getAvailableGraphicsDevices() {
		
		if (availableGraphicsDevices != null){
			// for testing purposes
			return availableGraphicsDevices;
		}
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] graphicsDevices = env.getScreenDevices();
		return graphicsDevices;
	}

	private Rectangle getBoundsOfSelectedGraphicsDevice(Settings settings, GraphicsDevice[] graphicsDevices) {
		GraphicsDevice device = graphicsDevices[settings.getScreenIndex()];
		Rectangle bounds = device.getDefaultConfiguration().getBounds();
		return bounds;
	}

	private boolean isGraphicsDeviceOtherThanDefault(Settings settings, GraphicsDevice[] graphicsDevices) {
		return settings.getScreenIndex() > 0 && graphicsDevices.length > 1
				&& settings.getScreenIndex() < graphicsDevices.length;
	}
	
	protected void setAvailableGraphicsDevices(GraphicsDevice[] availableGraphicsDevices){
		this.availableGraphicsDevices = availableGraphicsDevices;
	}

}
