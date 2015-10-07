package nl.ghyze.pomodoro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.model.SettingsChangeListener;
import nl.ghyze.pomodoro.view.PomoFrame;
import nl.ghyze.pomodoro.view.systemtray.SystemTrayManager;

public class PomoController implements ActionListener, SettingsChangeListener
{
   private PomoFrame frame;
   private Timer timer;

   private Settings settings;
   private PomodoroStateMachine stateMachine;

   public PomoController()
   {
      settings = new Settings();
      settings.addListener(this);
      settings.load();
      SystemTrayManager.getInstance().setPomoController(this);
      frame = new PomoFrame(this);
      frame.position(settings.getPosition());
      stateMachine = new PomodoroStateMachine(settings);
      stateMachine.updateCurrent();
      timer = new Timer(20, this);
      timer.start();
   }

   @Override
   public void actionPerformed(ActionEvent event)
   {
       stateMachine.handleAction(frame);
      frame.update(stateMachine.getCurrent());
      SystemTrayManager.getInstance().update(stateMachine.getCurrent());
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


   public void stopCurrent(){
       stateMachine.stopCurrent();
   }
   
   public void startPomo(){
       stateMachine.startPomo();
   }

}
