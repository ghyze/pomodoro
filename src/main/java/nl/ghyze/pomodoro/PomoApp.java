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

/**
 * This class is the entry point of the application.
 * It creates the controller and the frame.
 */
public class PomoApp
{

	/**
	 * Constructor.
	 */
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

	/**
	 * Initializes the frame.
	 * @param controller the controller to use
	 * @return the frame
	 */
	private PomoFrame initPomoFrame(PomoController controller) {
		PomoFrame frame = new PomoFrame(controller::stopProgram);
		frame.addButton(PomoButtonFactory.createStopButton(controller::stopCurrent));
		frame.addButton(PomoButtonFactory.createPlayButton(controller::startPomo));
		frame.addButton(PomoButtonFactory.createCloseButton(controller::stopProgram));
		frame.addButton(PomoButtonFactory.createMinimizeButton(frame));
		return frame;
	}

	/**
	 * Initializes the state machine.
	 * @param settings the settings to use
	 * @param systemTrayManager the system tray manager to use
	 * @param hooks the hooks to use
	 * @return the state machine
	 */
	private PomodoroStateMachine initStateMachine(Settings settings, AbstractSystemTrayManager systemTrayManager, PomodoroHook... hooks) {
		PomodoroStateMachine stateMachine = new PomodoroStateMachine(settings);
		stateMachine.setSystemTrayManager(systemTrayManager);
		for (PomodoroHook hook : hooks){
			stateMachine.addPomodoroHook(hook);
		}
		stateMachine.updateCurrent();
		return stateMachine;
	}

	/**
	 * Entry point of the application.
	 * @param args command line arguments, ignored
	 */
	public static void main(String[] args)
   {
	  new PomoApp();
   }

}
