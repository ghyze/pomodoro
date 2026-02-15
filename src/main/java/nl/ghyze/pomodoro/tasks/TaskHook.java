package nl.ghyze.pomodoro.tasks;

import nl.ghyze.pomodoro.controller.PomodoroHook;

public class TaskHook implements PomodoroHook {

	private Task currentTask;
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
	
	void setCurrentTask(Task task){
		assert task != null: "task should not be null";
		currentTask = task;
	}

	public void setTaskFrame(TaskFrame taskFrame) {
		this.taskFrame = taskFrame;
	}
}
