package nl.ghyze.tasks;

import lombok.Getter;
import lombok.Setter;
import nl.ghyze.pomodoro.controller.PomodoroHook;

@Getter
@Setter
public class TaskHook implements PomodoroHook {

	private Task currentTask;
	private TaskService taskService;

	@Override
	public void completed() {
	    if (currentTask != null) {
            currentTask.addCompletedPomo();
            if (taskService != null) {
                taskService.save();
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
