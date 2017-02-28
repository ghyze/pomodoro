package nl.ghyze.pomodoro;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.controller.PomodoroStateMachine;
import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.statistics.StatisticsHook;
import nl.ghyze.pomodoro.view.PomoFrame;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;
import nl.ghyze.pomodoro.view.systemtray.SystemTrayManagerImpl;

public class PomoApp
{

   public static void main(String[] args)
   {
      PomoController controller = new PomoController();
      AbstractSystemTrayManager systemTrayManager = new SystemTrayManagerImpl();
      controller.initializeSystemTrayManager(systemTrayManager);
      
      Settings settings = new Settings();
      settings.addListener(controller);
      settings.load();
      controller.setSettings(settings);
      
      PomoFrame frame = new PomoFrame(controller);
      frame.position(settings);
      controller.setPomoFrame(frame);
      
      PomodoroStateMachine stateMachine = new PomodoroStateMachine(settings);
      stateMachine.setSystemTrayManager(systemTrayManager);
      stateMachine.addPomodoroHook(new StatisticsHook());
      stateMachine.updateCurrent();
      controller.setStateMachine(stateMachine);
      
      controller.initialize();
   }

}
