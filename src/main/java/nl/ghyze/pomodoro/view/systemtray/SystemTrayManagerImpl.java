package nl.ghyze.pomodoro.view.systemtray;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.view.TaskFrame;
import nl.ghyze.pomodoro.view.menu.MenuController;

public class SystemTrayManagerImpl extends AbstractSystemTrayManager
{

   private MenuController menuController;

   public SystemTrayManagerImpl()
   {
      initializeImages();
      menuController = new MenuController();
      PopupMenu menu = menuController.createPopupMenu();
      initTrayIcon(menu);
   }
   
   public void setPomoController(PomoController controller)
   {
      menuController.setPomoController(controller);
   }
   
   public void setTaskFrame(TaskFrame taskFrame){
	   menuController.setTaskFrame(taskFrame);
   }

   protected Dimension getTrayIconSize()
   {
      SystemTray tray = SystemTray.getSystemTray();
      Dimension iconsize = tray.getTrayIconSize();
      return iconsize;
   }

   protected void initTrayIcon(PopupMenu menu)
   {
      SystemTray tray = SystemTray.getSystemTray();
      icon = new TrayIcon(waitImage, "Pomo", menu);
      try
      {
         tray.add(icon);
      }
      catch (AWTException e)
      {
         e.printStackTrace();
      }
   }

}
