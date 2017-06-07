package nl.ghyze.pomodoro.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class TaskManager {
	
	private List<Task> taskList = new ArrayList<>();
	
	private List<Observer> observerList = new ArrayList<>();
	
	public TaskManager(){
		addEmptyTask();
	}

	private void addEmptyTask() {
		Task task = new Task();
		for (Observer observer : observerList){
			task.addObserver(observer);
		}
		taskList.add(task);
	}

	public int getNumberOfTasks() {
		return taskList.size();
	}

	public Task getTask(int i) {
		return taskList.get(i);
	}
	
	private boolean hasEmptyTask() {
		for (Task task : taskList) {
			if (task.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public void addObserver(Observer observer){
		observerList.add(observer);
		for (Task task : taskList){
			task.addObserver(observer);
		}
	}
	
	public void update(){
		if (!hasEmptyTask()) {
			addEmptyTask();
		}
	}
	
	
}
