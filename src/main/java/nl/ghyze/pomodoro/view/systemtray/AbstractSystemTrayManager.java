package nl.ghyze.pomodoro.view.systemtray;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.view.TaskFrame;

public abstract class AbstractSystemTrayManager
{

   protected Image[] pomoImages;
   protected Image[] breakImages;
   protected Image waitImage;
   protected TrayIcon icon;

   public abstract void setPomoController(PomoController controller);
   
   public abstract void setTaskFrame(TaskFrame taskFrame);

   protected void initializeImages()
   {
      createPomoMinutesImages();
      createBreakMinutesImages();
      createWaitImage();
   }

   protected void createBreakMinutesImages()
   {
      breakImages = new Image[100];
      for (int i = 0; i < 100; i++)
      {
         breakImages[i] = createImage(new Color(0, 192, 0), "" + i);
      }
   }

   protected void createPomoMinutesImages()
   {
      pomoImages = new Image[100];
      for (int i = 0; i < 100; i++)
      {
         pomoImages[i] = createImage(Color.red, "" + i);
      }
   }

   protected void createWaitImage()
   {
      waitImage = createImage(Color.BLUE, "...");
   }

   private Image createImage(Color color, String number)
   {
      Dimension iconsize = getTrayIconSize();
      Image image = new BufferedImage(iconsize.width, iconsize.height, BufferedImage.TYPE_INT_RGB);
      Graphics gr = image.getGraphics();
      gr.setColor(color);
      gr.fillRect(0, 0, iconsize.width, iconsize.height);
      gr.setColor(Color.white);

      FontMetrics fm = gr.getFontMetrics();
      Rectangle2D bounds = fm.getStringBounds(number, gr);

      gr.drawString(number, (int) (iconsize.width - bounds.getWidth()) / 2, (int) (iconsize.height + bounds.getHeight()) / 2);
      return image;
   }

   protected abstract Dimension getTrayIconSize();

   public void stop()
   {
      SystemTray tray = SystemTray.getSystemTray();
      tray.remove(icon);
   }

   public void update(Pomodoro countdown)
   {
      if (countdown.getType() == Pomodoro.Type.WAIT)
      {
         icon.setImage(waitImage);
      }
      else if (countdown.getType() == Pomodoro.Type.POMO)
      {
         icon.setImage(pomoImages[countdown.minutesLeft()]);
      }
      else if (countdown.getType() == Pomodoro.Type.BREAK)
      {
         icon.setImage(breakImages[countdown.minutesLeft()]);
      }
   }

   public void message(String message)
   {
      icon.displayMessage("Pomodoro", message, MessageType.INFO);
   }

}
