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
//      current = new Countdown(5, Type.BREAK);
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
//            System.out.println("Autoswitch to next");
         }
      }
      
      frame.update(current);
      SystemTrayManager.getInstance().update(current);
   }
   
   private Pomodoro getNext(Type type)
   {
      if (type == Type.POMO){
         if (pomosDone < settings.getPomosBeforeLongBreak()){
            return new Pomodoro(settings.getShortBreakMinutes(), Type.BREAK);
         } else {
            pomosDone = 0;
            return new Pomodoro(settings.getLongBreakMinutes(), Type.BREAK);
         }
      } else if (type == Type.BREAK){
         return new Pomodoro(0, Type.WAIT);
      } else if (type == Type.WAIT) {
         pomosDone++;
         return new Pomodoro(settings.getPomoMinutes(), Type.POMO);
      }
      return null;
   }
   
   public void stopCurrent(){
      current = new Pomodoro(0, Type.WAIT);
   }
   
   public void startPomo(){
      current = new Pomodoro(25, Type.POMO);
      System.out.println("Pomo started");
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
}
