package nl.ghyze.pomodoro.statistics;

import java.util.Date;
import java.util.logging.Logger;

import nl.ghyze.pomodoro.DateTimeUtil;
import nl.ghyze.pomodoro.controller.PomodoroHook;

public class StatisticsHook implements PomodoroHook{
	private static final Logger logger = Logger.getLogger(StatisticsHook.class.getName());

	private int numberCompleted = 0;
	private int numberCancelled = 0;
	
	@Override
	public void completed() {
		numberCompleted ++;
		printStats();
	}

	@Override
	public void canceled() {
		numberCancelled ++;
		printStats();
	}
	
	@Override
	public void started(){
		
	}
	
	private void printStats(){
		logger.info(DateTimeUtil.format(new Date())+" Completed: "+numberCompleted+", Cancelled: "+numberCancelled);
	}

}
