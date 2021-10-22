package nl.ghyze.pomodoro;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.controller.PomodoroStateMachine;
import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.statistics.StatisticsHook;
import nl.ghyze.pomodoro.tasks.TaskFrame;
import nl.ghyze.pomodoro.view.PomoButtonFactory;
import nl.ghyze.pomodoro.view.PomoFrame;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;
import nl.ghyze.pomodoro.view.systemtray.SystemTrayManagerImpl;

public class PomoApp
{
	
	private PomoApp(){
		PomoController controller = new PomoController();
	      
		Settings settings = new Settings();
		settings.addListener(controller);
		settings.load();
		controller.setSettings(settings);

		initPomoFrame(controller, settings);

		TaskFrame taskFrame = new TaskFrame();

		AbstractSystemTrayManager systemTrayManager = new SystemTrayManagerImpl();
		systemTrayManager.setTaskFrame(taskFrame);
		controller.initializeSystemTrayManager(systemTrayManager);

		initStateMachine(controller, settings, taskFrame, systemTrayManager);

		controller.initialize();
	}

	private void initPomoFrame(PomoController controller, Settings settings) {
		PomoFrame frame = new PomoFrame(controller);
		frame.addButton(PomoButtonFactory.createStopButton(controller));
		frame.addButton(PomoButtonFactory.createPlayButton(controller));
		frame.addButton(PomoButtonFactory.createCloseButton(controller));
		frame.addButton(PomoButtonFactory.createMinimizeButton(frame));
		frame.position(settings);
		controller.setPomoFrame(frame);
	}

	private void initStateMachine(PomoController controller, Settings settings, TaskFrame taskFrame, AbstractSystemTrayManager systemTrayManager) {
		PomodoroStateMachine stateMachine = new PomodoroStateMachine(settings);
		stateMachine.setSystemTrayManager(systemTrayManager);
		stateMachine.addPomodoroHook(new StatisticsHook());
		stateMachine.addPomodoroHook(taskFrame.getTaskHook());
		stateMachine.updateCurrent();
		controller.setStateMachine(stateMachine);
	}

	public static void main(String[] args)
   {
	  new PomoApp();
   }

}
