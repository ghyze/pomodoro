package nl.ghyze.pomodoro.tasks;

import nl.ghyze.pomodoro.controller.PomodoroHook;

public class TaskHook implements PomodoroHook {

	private Task currentTask = new Task();
	
	@Override
	public void completed() {
		currentTask.addCompletedPomo();
	}

	@Override
	public void canceled() {
		// nothing to do
	}
	
	@Override
	public void started(){
		
	}
	
	public void setCurrentTask(Task task){
		assert task != null: "task should not be null";
		currentTask = task;
	}
	
	public Task getCurrentTask(){
		return currentTask;
	}

}
