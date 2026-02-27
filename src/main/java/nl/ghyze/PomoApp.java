package nl.ghyze;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.optiondialog.OptionDialogController;
import nl.ghyze.pomodoro.statemachine.PomodoroStateMachine;
import nl.ghyze.settings.Settings;
import nl.ghyze.settings.SettingsRepository;
import nl.ghyze.tasks.TaskHook;
import nl.ghyze.tasks.TaskRepository;
import nl.ghyze.tasks.TaskService;
import nl.ghyze.statistics.StatisticsHook;
import nl.ghyze.statistics.StatisticsRepository;
import nl.ghyze.statistics.TaskStatisticsHook;
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

        final SettingsRepository settingsRepository = new SettingsRepository();
        final Settings settings = settingsRepository.load();

        final TaskRepository taskRepository = new TaskRepository();
        final TaskHook taskHook = new TaskHook();
        final TaskService taskService = new TaskService(taskRepository, taskHook);
        final TaskFrame taskFrame = new TaskFrame(taskService);

        final PomoFrame frame = initPomoFrame(controller, taskFrame);

		final AbstractSystemTrayManager systemTrayManager = new SystemTrayManagerImpl();
		systemTrayManager.setTaskFrame(taskFrame);
		systemTrayManager.setSettingsRepository(settingsRepository);

		// Create statistics repository for event logging
		final StatisticsRepository statisticsRepository = new StatisticsRepository();

		// Create state machine first so we can inject it into StatisticsHook
		final PomodoroStateMachine stateMachine = new PomodoroStateMachine(settings);
		stateMachine.setSystemTrayManager(systemTrayManager);

		// Create hooks with statistics repository
		final StatisticsHook statisticsHook = new StatisticsHook(statisticsRepository, stateMachine);
		final TaskStatisticsHook taskStatisticsHook = new TaskStatisticsHook(statisticsRepository);

		// Register task statistics hook with task service
		taskService.setStatisticsHook(taskStatisticsHook);

		// Register hooks with state machine
		stateMachine.addPomodoroHook(statisticsHook);
		stateMachine.addPomodoroHook(taskHook);
		stateMachine.updateCurrent();

		final OptionDialogController dialogController = new OptionDialogController(frame);

		controller.initialize(frame, settings, systemTrayManager, stateMachine, dialogController);
	}

	/**
	 * Initializes the frame.
	 * @param controller the controller to use
	 * @return the frame
	 */
	private PomoFrame initPomoFrame(final PomoController controller, final TaskFrame taskFrame) {
		final PomoFrame frame = new PomoFrame(controller::stopProgram);
		frame.addButton(PomoButtonFactory.createStopButton(controller::stopCurrent));
		frame.addButton(PomoButtonFactory.createPlayButton(controller::startPomo));
		frame.addButton(PomoButtonFactory.createCloseButton(controller::stopProgram));
		frame.addButton(PomoButtonFactory.createMinimizeButton(frame));
        frame.addButton(PomoButtonFactory.createTasksButton(() -> taskFrame.setVisible(true)));
		return frame;
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
