package nl.ghyze.pomodoro;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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
			Screen screen = new Screen(mode, i, getAvailableArea(i));
			screenList.add(screen);
		}
		return screenList;
	}

	public Screen getSelectedScreen(Settings settings) {
		List<Screen> screenList = getAvailableScreenList();
		int screenIndex = settings.getScreenIndex();
		int actualIndex = screenIndex <= screenList.size()?screenIndex:1;
		return screenList.get(actualIndex);
	}
	
	private Rectangle getAvailableArea(int index){
		GraphicsDevice[] graphicsDevices = getAvailableGraphicsDevices();
		if (isGraphicsDeviceOtherThanDefault(index, graphicsDevices)) {
			return getBoundsOfSelectedGraphicsDevice(index, graphicsDevices);
		} else {
			return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
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

	private Rectangle getBoundsOfSelectedGraphicsDevice(int index, GraphicsDevice[] graphicsDevices) {
		GraphicsDevice device = graphicsDevices[index];
		Rectangle bounds = device.getDefaultConfiguration().getBounds();
		return bounds;
	}

	private boolean isGraphicsDeviceOtherThanDefault(int screenIndex, GraphicsDevice[] graphicsDevices) {
		return screenIndex > 0 && graphicsDevices.length > 1
				&& screenIndex < graphicsDevices.length;
	}
	
	protected void setAvailableGraphicsDevices(GraphicsDevice[] availableGraphicsDevices){
		this.availableGraphicsDevices = availableGraphicsDevices;
	}

}
