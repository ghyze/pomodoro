package nl.ghyze.pomodoro;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.view.Screen;

public class MultiScreenFactory {

    private GraphicsDevice[] availableGraphicsDevices = null;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();

    public List<Screen> getAvailableScreenList() {
        GraphicsDevice[] graphicsDevices = getAvailableGraphicsDevices();

        List<Screen> screenList = new ArrayList<Screen>();
        for (int i = 0; i < graphicsDevices.length; i++) {
            GraphicsDevice device = graphicsDevices[i];
            DisplayMode mode = device.getDisplayMode();
            Screen screen = Screen.builder()
                    .mode(mode)
                    .index(i)
                    .availableArea(getAvailableArea(i))
                    .build();
            screenList.add(screen);
        }
        return screenList;
    }

    public Screen getSelectedScreen(Settings settings) {
        List<Screen> screenList = getAvailableScreenList();
        int screenIndex = settings.getScreenIndex();
        int actualIndex = screenIndex < screenList.size() ? screenIndex : 0;
        return screenList.get(actualIndex);
    }

    private Rectangle getAvailableArea(int index) {
        GraphicsDevice[] graphicsDevices = getAvailableGraphicsDevices();
        GraphicsDevice device = graphicsDevices[index];
        // get total window size for device
        Rectangle bounds = device.getDefaultConfiguration().getBounds();

        // adjust for taskbar at the bottom
        Insets insets = toolkit.getScreenInsets(device.getDefaultConfiguration());
        bounds.height -= insets.bottom;

        return bounds;
    }

    private GraphicsDevice[] getAvailableGraphicsDevices() {

        if (availableGraphicsDevices != null) {
            // for testing purposes
            return availableGraphicsDevices;
        }

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] graphicsDevices = env.getScreenDevices();
        return graphicsDevices;
    }

    protected void setAvailableGraphicsDevices(GraphicsDevice[] availableGraphicsDevices) {
        this.availableGraphicsDevices = availableGraphicsDevices;
    }

    protected void setToolkit(Toolkit toolkit){
        this.toolkit = toolkit;
    }

}
