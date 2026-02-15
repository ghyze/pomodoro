package nl.ghyze.pomodoro.view;

import nl.ghyze.pomodoro.MultiScreenFactory;
import nl.ghyze.pomodoro.model.Settings;

import java.awt.*;

public class FrameLocation {
    private final Dimension frameSize;
    private final Settings.Position position;
    private final Point mostBottomRightPoint;
    private final Point graphicsDeviceOffset;

    public FrameLocation(Settings settings, Dimension frameSize){
        this.frameSize = frameSize;
        position = settings.getPosition();
        MultiScreenFactory multiScreenFactory = new MultiScreenFactory();
        Screen screen = multiScreenFactory.getSelectedScreen(settings);
        mostBottomRightPoint = screen.getMostBottomRightPoint();
        graphicsDeviceOffset = screen.getGraphicsDeviceOffset();
    }

    public Point getLocation(){
        int x = getXPosition();
        int y = getYPosition();
        return new Point(x, y);
    }

    private int getXPosition(){
        return switch (position.horizontal) {
            case LEFT -> graphicsDeviceOffset.x;
            case RIGHT -> mostBottomRightPoint.x - (int) frameSize.getWidth();
        };
    }

    private int getYPosition(){
        return switch (position.vertical) {
            case TOP -> graphicsDeviceOffset.y;
            case BOTTOM -> mostBottomRightPoint.y - (int) frameSize.getHeight();
        };
    }
}
