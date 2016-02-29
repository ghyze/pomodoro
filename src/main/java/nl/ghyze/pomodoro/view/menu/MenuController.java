package nl.ghyze.pomodoro.view.menu;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.view.SettingsFrame;

public class MenuController
{

   private PomoController controller;

   public MenuController()
   {
   }

   public void setPomoController(PomoController controller)
   {
      this.controller = controller;
   }

   public PopupMenu createPopupMenu()
   {
      PopupMenu menu = new PopupMenu();
      MenuItem show = createShowMenuItem();
      menu.add(show);

      MenuItem settings = createSettingsMenuItem();
      menu.add(settings);

      MenuItem exit = createExitMenuItem();
      menu.add(exit);

      MenuItem reset = createResetMenuItem();
      menu.add(reset);
      return menu;
   }

   protected MenuItem createShowMenuItem()
   {
      MenuItem show = new MenuItem("Show Frame");
      show.addActionListener(new ActionListener()
         {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
               if (controller != null)
               {
                  controller.showFrame();
               }
            }

         });
      return show;
   }

   protected MenuItem createSettingsMenuItem()
   {
      MenuItem settings = new MenuItem("Settings");
      settings.addActionListener(new ActionListener()
         {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
               if (controller != null)
               {
                  SettingsFrame settingsFrame = new SettingsFrame(controller.getSettings());
                  settingsFrame.setVisible(true);
               }
            }

         });
      return settings;
   }

   protected MenuItem createExitMenuItem()
   {
      MenuItem exit = new MenuItem("Exit");
      exit.addActionListener(new ActionListener()
         {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
               if (controller != null)
               {
                  controller.stopProgram();
               }
            }

         });
      return exit;
   }

   protected MenuItem createResetMenuItem()
   {
      MenuItem reset = new MenuItem("Reset");
      reset.addActionListener(new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent event)
            {
               if (controller != null)
               {
                  controller.reset();
               }
            }
         });
      return reset;
   }
}
