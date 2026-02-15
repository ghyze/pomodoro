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
import nl.ghyze.pomodoro.persistence.SettingsRepository;
import nl.ghyze.pomodoro.tasks.TaskFrame;
import nl.ghyze.pomodoro.model.PomodoroType;

public abstract class AbstractSystemTrayManager
{
   private static final int MAX_IMAGE_INDEX = 99;
   private static final int IMAGE_ARRAY_SIZE = 100;

   protected Image[] pomoImages;
   protected Image[] breakImages;
   protected Image waitImage;
   TrayIcon icon;

   public abstract void setPomoController(PomoController controller);

   public abstract void setTaskFrame(TaskFrame taskFrame);

   public abstract void setSettingsRepository(SettingsRepository settingsRepository);

   protected void initializeImages()
   {
      createPomoMinutesImages();
      createBreakMinutesImages();
      createWaitImage();
   }

   private void createBreakMinutesImages()
   {
      breakImages = new Image[IMAGE_ARRAY_SIZE];
      for (int i = 0; i < IMAGE_ARRAY_SIZE; i++)
      {
         breakImages[i] = createImage(new Color(0, 192, 0), "" + i);
      }
   }

   private void createPomoMinutesImages()
   {
      pomoImages = new Image[IMAGE_ARRAY_SIZE];
      for (int i = 0; i < IMAGE_ARRAY_SIZE; i++)
      {
         pomoImages[i] = createImage(Color.red, "" + i);
      }
   }

   private void createWaitImage()
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
      if (countdown.getType() == PomodoroType.WAIT)
      {
         icon.setImage(waitImage);
      }
      else if (countdown.getType() == PomodoroType.POMO)
      {
         icon.setImage(pomoImages[getSafeImageIndex(countdown.minutesLeft())]);
      }
      else if (countdown.getType() == PomodoroType.BREAK)
      {
         icon.setImage(breakImages[getSafeImageIndex(countdown.minutesLeft())]);
      }
   }

   private int getSafeImageIndex(final int minutesLeft)
   {
      return Math.min(minutesLeft, MAX_IMAGE_INDEX);
   }

   public void message(String message)
   {
      icon.displayMessage("Pomodoro", message, MessageType.INFO);
   }

}
