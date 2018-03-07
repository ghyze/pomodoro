package nl.ghyze.pomodoro.statistics;

import java.util.Date;

import nl.ghyze.pomodoro.DateUtil;
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
	
	public void printStats(){
		System.out.println(DateUtil.format(new Date())+" Completed: "+numberCompleted+", Cancelled: "+numberCancelled);
	}

}
