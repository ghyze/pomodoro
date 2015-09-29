package nl.ghyze.pomodoro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.model.SettingsChangeListener;
import nl.ghyze.pomodoro.model.Pomodoro.Type;
import nl.ghyze.pomodoro.view.PomoFrame;
import nl.ghyze.pomodoro.view.systemtray.SystemTrayManager;

public class PomoController implements ActionListener, SettingsChangeListener
{
   private PomoFrame frame;
   private Pomodoro current;
   private Timer timer;
   private int pomosDone = 0;

   private Settings settings;

   public PomoController()
   {
      settings = new Settings();
      settings.addListener(this);
      settings.load();
      SystemTrayManager.getInstance().setPomoController(this);
      frame = new PomoFrame(this);
      frame.position(settings.getPosition());
      current = new Pomodoro(0, Type.WAIT);
      updateCurrent();
      timer = new Timer(20, this);
      timer.start();
   }

   @Override
   public void actionPerformed(ActionEvent arg0)
   {
      if (current.getType() != Type.WAIT)
      {
         if (current.isDone())
         {
            if (current.getType() == Type.POMO)
            {
               Object[] options = { "Save", "Discard" };
               int choice = JOptionPane.showOptionDialog(frame, "Pomodoro finished. What would you like to do with this one?", "Pomodoro finished", JOptionPane.OK_CANCEL_OPTION,
                     JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
               System.out.println("Finished choice: "+choice);
               if (choice == 0){
                  pomosDone++;
               }
               Pomodoro next = getNext(Type.POMO);
               current = next;
               updateCurrent();

            }
            else if (current.getType() == Type.BREAK)
            {
               Object[] options = { "Ok", "Cancel" };
               int choice = JOptionPane.showOptionDialog(frame, "Ready to start next one??", "Break finished", JOptionPane.OK_CANCEL_OPTION,
                     JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
               System.out.println("New choice: "+choice);
               if (choice == 0){
                  startPomo();
               } else {
                  startWait();
               }
            }

         }
      }

      frame.update(current);
      SystemTrayManager.getInstance().update(current);
   }

   protected Pomodoro getNext(Type type)
   {
      if (type == Type.POMO)
      {
         if (pomosDone < settings.getPomosBeforeLongBreak())
         {
            String message = "Well done: Short break";
            return new Pomodoro(settings.getShortBreakMinutes(), Type.BREAK);
         }
         else
         {
            pomosDone = 0;
            String message = "Well done: Long break";
            return new Pomodoro(settings.getLongBreakMinutes(), Type.BREAK);
         }
      }
      else if (type == Type.BREAK)
      {
         String message = "Waiting to start next Pomodoro";
         showMessage(message);
         return new Pomodoro(0, Type.WAIT);
      }
      return null;
   }

   public void stopCurrent()
   {
      startWait();
   }
   
   public void startWait(){
      current = new Pomodoro(0, Type.WAIT);
      updateCurrent();
   }

   public void startPomo()
   {
      current = new Pomodoro(25, Type.POMO);
      updateCurrent();
      String message = "Starting Pomodoro number " + (pomosDone +1);
      showMessage(message);
   }

   public void stopProgram()
   {
      SystemTrayManager.getInstance().stop();
      System.exit(0);
   }

   public void showFrame()
   {
      frame.setVisible(true);
   }

   public Settings getSettings()
   {
      return settings;
   }

   @Override
   public void onChange(Settings settings)
   {
      this.settings = settings;
      frame.position(settings.getPosition());
   }

   private void showMessage(String message)
   {
      SystemTrayManager.getInstance().message(message);
   }

   private void updateCurrent()
   {
      current.setPomosDone(pomosDone);
      current.setMaxPomosDone(settings.getPomosBeforeLongBreak());
   }
}
