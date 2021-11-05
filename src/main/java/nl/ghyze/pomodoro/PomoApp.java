package nl.ghyze.pomodoro;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.controller.PomodoroHook;
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

		PomoFrame frame = initPomoFrame(controller);

		Settings settings = new Settings();
		settings.load();

		TaskFrame taskFrame = new TaskFrame();

		AbstractSystemTrayManager systemTrayManager = new SystemTrayManagerImpl();
		systemTrayManager.setTaskFrame(taskFrame);

		PomodoroStateMachine stateMachine = initStateMachine(settings, systemTrayManager, new StatisticsHook(), taskFrame.getTaskHook());

		controller.initialize(frame, settings, systemTrayManager, stateMachine);
	}

	private PomoFrame initPomoFrame(PomoController controller) {
		PomoFrame frame = new PomoFrame(controller::stopProgram);
		frame.addButton(PomoButtonFactory.createStopButton(controller::stopCurrent));
		frame.addButton(PomoButtonFactory.createPlayButton(controller::startPomo));
		frame.addButton(PomoButtonFactory.createCloseButton(controller::stopProgram));
		frame.addButton(PomoButtonFactory.createMinimizeButton(frame));
		return frame;
	}

	private PomodoroStateMachine initStateMachine(Settings settings, AbstractSystemTrayManager systemTrayManager, PomodoroHook... hooks) {
		PomodoroStateMachine stateMachine = new PomodoroStateMachine(settings);
		stateMachine.setSystemTrayManager(systemTrayManager);
		for (PomodoroHook hook : hooks){
			stateMachine.addPomodoroHook(hook);
		}
		stateMachine.updateCurrent();
		return stateMachine;
	}

	public static void main(String[] args)
   {
	  new PomoApp();
   }

}
