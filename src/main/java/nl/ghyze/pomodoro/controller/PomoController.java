package nl.ghyze.pomodoro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
   
   public PomoController(){
      settings = new Settings();
      settings.addListener(this);
      settings.load();
      SystemTrayManager.getInstance().setPomoController(this);
      frame = new PomoFrame(this);
      frame.position(settings.getPosition());
      current = new Pomodoro(0, Type.WAIT);
      timer = new Timer(20, this);
      timer.start();
   }
   
   @Override
   public void actionPerformed(ActionEvent arg0)
   {
      if (current.getType() != Type.WAIT){
         if (current.isDone()) {
            current = getNext(current.getType());
            current.setPomosDone(pomosDone);
         }
      }
      
      frame.update(current);
      SystemTrayManager.getInstance().update(current);
   }
   
   protected Pomodoro getNext(Type type)
   {
      if (type == Type.POMO){
         if (pomosDone < settings.getPomosBeforeLongBreak()){
            String message = "Well done: Short break";
            showMessage(message);
            return new Pomodoro(settings.getShortBreakMinutes(), Type.BREAK);
         } else {
            pomosDone = 0;
            String message = "Well done: Long break";
            showMessage(message);
            return new Pomodoro(settings.getLongBreakMinutes(), Type.BREAK);
         }
      } else if (type == Type.BREAK){
         String message = "Waiting to start next Pomodoro";
         showMessage(message);
         return new Pomodoro(0, Type.WAIT);
      } 
      return null;
   }
   
   public void stopCurrent(){
      current = new Pomodoro(0, Type.WAIT);
   }
   
   public void startPomo(){
      current = new Pomodoro(25, Type.POMO);
      pomosDone++;
      String message = "Starting Pomodoro number "+pomosDone;
      showMessage(message);
   }

   public void stopProgram(){
      SystemTrayManager.getInstance().stop();
      System.exit(0);
   }

   public void showFrame()
   {
      frame.setVisible(true);
   }
   
   public Settings getSettings(){
      return settings;
   }

   @Override
   public void onChange(Settings settings)
   {
      this.settings = settings;
      frame.position(settings.getPosition());
   }
   
   private void showMessage(String message){
      SystemTrayManager.getInstance().message(message);
   }
}
