package nl.ghyze.tasks;

import lombok.Setter;
import nl.ghyze.pomodoro.controller.PomodoroHook;

public class TaskHook implements PomodoroHook {

	private Task currentTask;
	@Setter
	private TaskFrame taskFrame;

	@Override
	public void completed() {
	    if (currentTask != null) {
            currentTask.addCompletedPomo();
            if (taskFrame != null) {
                taskFrame.saveTasks();
            }
        }
	}

	@Override
	public void canceled() {
		// nothing to do
	}
	
	@Override
	public void started(){
		// nothing to do
	}
	
	public void setCurrentTask(final Task task){
		assert task != null: "task should not be null";
		currentTask = task;
	}
}
