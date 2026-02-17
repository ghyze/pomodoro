package nl.ghyze.statistics;

import java.time.Instant;
import java.util.logging.Logger;

import nl.ghyze.pomodoro.DateTimeUtil;
import nl.ghyze.pomodoro.controller.PomodoroHook;
import nl.ghyze.pomodoro.model.PomodoroType;
import nl.ghyze.pomodoro.statemachine.PomodoroStateMachine;

/**
 * Hook that logs pomodoro lifecycle events to CSV files and maintains in-memory statistics.
 * Logs to daily-rotated CSV files via StatisticsRepository.
 */
public class StatisticsHook implements PomodoroHook{
	private static final Logger logger = Logger.getLogger(StatisticsHook.class.getName());

	private final StatisticsRepository repository;
	private final PomodoroStateMachine stateMachine;

	// Keep in-memory counters for backward compatibility
	private int numberCompleted = 0;
	private int numberCancelled = 0;

	private PomodoroType previousState = PomodoroType.WAIT;

	public StatisticsHook(StatisticsRepository repository, PomodoroStateMachine stateMachine) {
		this.repository = repository;
		this.stateMachine = stateMachine;
	}

	@Override
	public void started(){
		PomodoroType currentState = stateMachine.getCurrentPomodoroType();
		int pomosDone = stateMachine.getCurrentPomodoro().getPomosDone();

		// Log started event
		PomodoroEvent event = PomodoroEvent.started(previousState, pomosDone + 1);
		repository.logPomodoroEvent(event);

		previousState = currentState;
	}

	@Override
	public void completed() {
		numberCompleted++;
		int pomosDone = stateMachine.getCurrentPomodoro().getPomosDone();

		// Log completed event
		PomodoroEvent event = PomodoroEvent.completed(pomosDone);
		repository.logPomodoroEvent(event);

		previousState = PomodoroType.POMO;
		printStats();
	}

	@Override
	public void canceled() {
		numberCancelled++;
		int pomosDone = stateMachine.getCurrentPomodoro().getPomosDone();

		// Log canceled event
		PomodoroEvent event = PomodoroEvent.canceled(pomosDone);
		repository.logPomodoroEvent(event);

		previousState = PomodoroType.POMO;
		printStats();
	}

	private void printStats(){
		logger.info(DateTimeUtil.format(Instant.now())+" Completed: "+numberCompleted+", Cancelled: "+numberCancelled);
	}

	// Getters for backward compatibility
	public int getNumberCompleted() {
		return numberCompleted;
	}

	public int getNumberCancelled() {
		return numberCancelled;
	}
}
