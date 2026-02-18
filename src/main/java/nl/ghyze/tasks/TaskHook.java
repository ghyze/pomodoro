package nl.ghyze.tasks;

import lombok.Getter;
import lombok.Setter;
import nl.ghyze.pomodoro.controller.PomodoroHook;

@Getter
@Setter
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
}
