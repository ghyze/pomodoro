package nl.ghyze.pomodoro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.Timer;

import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.model.SettingsChangeListener;
import nl.ghyze.pomodoro.optiondialog.OptionDialogController;
import nl.ghyze.pomodoro.optiondialog.OptionDialogModel;
import nl.ghyze.pomodoro.optiondialog.OptionDialogModelFactory;
import nl.ghyze.pomodoro.optiondialog.ResetOptionDialogCallback;
import nl.ghyze.pomodoro.optiondialog.StateMachineOptionDialogCallback;
import nl.ghyze.pomodoro.view.PomoFrame;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;

public class PomoController implements ActionListener, SettingsChangeListener
{
   private PomoFrame frame;
   private Timer timer;

   private Settings settings;
   private PomodoroStateMachine stateMachine;
   private AbstractSystemTrayManager systemTrayManager;

   private int minutesSinceLastAction = 0;

   public PomoController()
   {

   }

   public void initializeSystemTrayManager(AbstractSystemTrayManager systemTrayManager)
   {
      this.systemTrayManager = systemTrayManager;
      this.systemTrayManager.setPomoController(this);
   }

   public void initialize()
   {
      settings = new Settings();
      settings.addListener(this);
      settings.load();
      frame = new PomoFrame(this);
      frame.position(settings.getPosition());
      stateMachine = new PomodoroStateMachine(settings);
      stateMachine.setSystemTrayManager(this.systemTrayManager);
      stateMachine.updateCurrent();
      timer = new Timer(20, this);
      timer.start();
   }

   @Override
   public void actionPerformed(ActionEvent event)
   {
      if (stateMachine.shouldChangeState())
      {
         OptionDialogModel model = OptionDialogModelFactory.createChangeStateModel(stateMachine.getCurrentType());
         StateMachineOptionDialogCallback callback = new StateMachineOptionDialogCallback(stateMachine);
         OptionDialogController.showDialog(frame, model, callback);
      }
      checkMinutesSinceLastAction();
      frame.update(stateMachine.getCurrent());
      systemTrayManager.update(stateMachine.getCurrent());
   }

   private void checkMinutesSinceLastAction()
   {
      minutesSinceLastAction = getMinutesSinceLastAction();

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
      frame.position(settings.getPosition());
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
}
