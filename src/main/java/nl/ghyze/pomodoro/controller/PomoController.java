package nl.ghyze.pomodoro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.Timer;

import lombok.NoArgsConstructor;
import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.model.SettingsChangeListener;
import nl.ghyze.pomodoro.optiondialog.OptionDialogController;
import nl.ghyze.pomodoro.optiondialog.OptionDialogModel;
import nl.ghyze.pomodoro.optiondialog.OptionDialogModelFactory;
import nl.ghyze.pomodoro.optiondialog.ResetOptionDialogCallback;
import nl.ghyze.pomodoro.optiondialog.StateMachineOptionDialogCallback;
import nl.ghyze.pomodoro.view.PomoFrame;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;

@NoArgsConstructor
public class PomoController implements ActionListener, SettingsChangeListener
{
   private PomoFrame frame;

   private Settings settings;
   private PomodoroStateMachine stateMachine;
   private AbstractSystemTrayManager systemTrayManager;

   public void initializeSystemTrayManager(AbstractSystemTrayManager systemTrayManager)
   {
      this.systemTrayManager = systemTrayManager;
      this.systemTrayManager.setPomoController(this);
   }

   public void initialize()
   {
      Timer timer = new Timer(20, this);
      timer.start();
   }

   @Override
   public void actionPerformed(ActionEvent event)
   {
      if (stateMachine.shouldChangeState())
      {
         OptionDialogModel model = OptionDialogModelFactory.createChangeStateModel(PomodoroStateMachine.getCurrentType());
         StateMachineOptionDialogCallback callback = new StateMachineOptionDialogCallback(stateMachine);
         OptionDialogController.showDialog(frame, model, callback);
      }
      checkMinutesSinceLastAction();
      frame.update(PomodoroStateMachine.getCurrent());
      systemTrayManager.update(PomodoroStateMachine.getCurrent());
   }

   private void checkMinutesSinceLastAction()
   {
      int minutesSinceLastAction = getMinutesSinceLastAction();

      if (minutesSinceLastAction >= settings.getIdleTime())
      {
         stateMachine.reset();
      }
   }

   private int getMinutesSinceLastAction()
   {
      Date now = new Date();
      long millis = (now.getTime() - stateMachine.getLastAction().getTime());
      return (int) (millis / (60 * 1000));
   }

   public void stopProgram()
   {
      systemTrayManager.stop();
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
      frame.position(settings);
   }

   public void stopCurrent()
   {
      stateMachine.stopCurrent();
   }

   public void startPomo()
   {
      stateMachine.startPomo();
   }

   public void reset()
   {
      OptionDialogModel resetModel = OptionDialogModelFactory.createResetModel();
      OptionDialogController.showDialog(frame, resetModel, new ResetOptionDialogCallback(stateMachine));
   }
   
   public void setSettings(Settings settings){
	   this.settings = settings;
   }
   
   public void setPomoFrame(PomoFrame frame){
	   this.frame = frame;
   }
   
   public void setStateMachine(PomodoroStateMachine stateMachine){
	   this.stateMachine = stateMachine;
   }
}
