package nl.ghyze.pomodoro.statistics;

import java.util.Date;

import nl.ghyze.pomodoro.DateTimeUtil;
import nl.ghyze.pomodoro.controller.PomodoroHook;

public class StatisticsHook implements PomodoroHook{

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
		System.out.println(DateTimeUtil.format(new Date())+" Completed: "+numberCompleted+", Cancelled: "+numberCancelled);
	}

}
