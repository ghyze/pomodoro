package nl.ghyze.pomodoro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import lombok.NoArgsConstructor;
import nl.ghyze.pomodoro.DateTimeUtil;
import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.model.SettingsChangeListener;
import nl.ghyze.pomodoro.optiondialog.OptionDialogController;
import nl.ghyze.pomodoro.optiondialog.OptionDialogModel;
import nl.ghyze.pomodoro.optiondialog.OptionDialogModelFactory;
import nl.ghyze.pomodoro.optiondialog.ResetOptionDialogCallback;
import nl.ghyze.pomodoro.optiondialog.StateMachineOptionDialogCallback;
import nl.ghyze.pomodoro.view.PomoFrame;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;

import static nl.ghyze.pomodoro.Stopwatch.MILLISECONDS_PER_SECOND;

@NoArgsConstructor
public class PomoController implements ActionListener, SettingsChangeListener
{
   private PomoFrame frame;

   private Settings settings;
   private PomodoroStateMachine stateMachine;
   private AbstractSystemTrayManager systemTrayManager;


   public void initialize(PomoFrame frame, Settings settings, AbstractSystemTrayManager systemTrayManager, PomodoroStateMachine stateMachine)
   {
      this.frame = frame;
      this.settings = settings;
      this.systemTrayManager = systemTrayManager;
      this.stateMachine = stateMachine;

      this.frame.position(settings);
      this.systemTrayManager.setPomoController(this);
      settings.addListener(this);
      OptionDialogController.init(frame);
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
         OptionDialogController.showDialog(model, callback);
      }
      checkMinutesSinceLastAction();
      frame.update(PomodoroStateMachine.getCurrent());
      systemTrayManager.update(PomodoroStateMachine.getCurrent());
   }

   private void checkMinutesSinceLastAction()
   {
       if (stateMachine.getLastAction().isTimedOut(settings.getIdleTime()*MILLISECONDS_PER_SECOND) &&
       PomodoroStateMachine.getCurrentType().isWait())
      {
         stateMachine.reset();
      }
   }

   private int getMinutesSinceLastAction()
   {
      return stateMachine.getLastAction().timePassedMinutes();
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
      OptionDialogController.showDialog(resetModel, new ResetOptionDialogCallback(stateMachine));
   }
}
