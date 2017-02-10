package nl.ghyze.pomodoro.experimental;

import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.view.PomoFrame;

public class MultiMonitor
{

   public static void main(String[] args)
   {
      GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice[] graphicsDevices = env.getScreenDevices();

      for (GraphicsDevice device : graphicsDevices)
      {
         System.out.println(device);
         DisplayMode mode = device.getDisplayMode();
         System.out.println("\tWidth x heigth: " + mode.getWidth() + " x " + mode.getHeight());
         
         GraphicsConfiguration config = device.getDefaultConfiguration();
         System.out.println("\tWidth x heigth: " + config.getBounds().getWidth() + " x " + config.getBounds().getHeight());
         System.out.println("\tx, y: " + config.getBounds().getX() + " , " + config.getBounds().getY());
         
      }
      
      GraphicsConfiguration config = graphicsDevices[1].getDefaultConfiguration();
      PomoController controller = new PomoController();
      PomoFrame frame = new PomoFrame(controller);
      frame.setLocation((int)config.getBounds().getX(), (int)config.getBounds().getY());
      
   }

}
