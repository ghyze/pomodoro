package nl.ghyze.pomodoro;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.controller.PomodoroHook;
import nl.ghyze.pomodoro.optiondialog.OptionDialogController;
import nl.ghyze.pomodoro.statemachine.PomodoroStateMachine;
import nl.ghyze.settings.Settings;
import nl.ghyze.settings.SettingsRepository;
import nl.ghyze.tasks.TaskRepository;
import nl.ghyze.statistics.StatisticsHook;
import nl.ghyze.tasks.TaskFrame;
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
		final PomoController controller = new PomoController();

		final PomoFrame frame = initPomoFrame(controller);

		final SettingsRepository settingsRepository = new SettingsRepository();
		final Settings settings = settingsRepository.load();

		final TaskRepository taskRepository = new TaskRepository();
		final TaskFrame taskFrame = new TaskFrame(taskRepository);

		final AbstractSystemTrayManager systemTrayManager = new SystemTrayManagerImpl();
		systemTrayManager.setTaskFrame(taskFrame);
		systemTrayManager.setSettingsRepository(settingsRepository);

		final PomodoroStateMachine stateMachine = initStateMachine(settings, systemTrayManager, new StatisticsHook(), taskFrame.getTaskHook());

		final OptionDialogController dialogController = new OptionDialogController(frame);

		controller.initialize(frame, settings, systemTrayManager, stateMachine, dialogController);
	}

	/**
	 * Initializes the frame.
	 * @param controller the controller to use
	 * @return the frame
	 */
	private PomoFrame initPomoFrame(final PomoController controller) {
		final PomoFrame frame = new PomoFrame(controller::stopProgram);
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
	private PomodoroStateMachine initStateMachine(final Settings settings, final AbstractSystemTrayManager systemTrayManager, final PomodoroHook... hooks) {
		final PomodoroStateMachine stateMachine = new PomodoroStateMachine(settings);
		stateMachine.setSystemTrayManager(systemTrayManager);
		for (final PomodoroHook hook : hooks){
			stateMachine.addPomodoroHook(hook);
		}
		stateMachine.updateCurrent();
		return stateMachine;
	}

	/**
	 * Entry point of the application.
	 * @param args command line arguments, ignored
	 */
	public static void main(final String[] args)
   {
	  new PomoApp();
   }

}
