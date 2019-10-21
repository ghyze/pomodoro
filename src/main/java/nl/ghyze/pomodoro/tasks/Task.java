package nl.ghyze.pomodoro.tasks;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Observable;

@EqualsAndHashCode(callSuper = true)
@Data
public class Task extends Observable{

	private final String name;
	private final int estimated;
	private int actual;
	
	private boolean active = false;

	Task(String name, int estimated){
		this.name = name;
		this.estimated = estimated;
	}

	private void changed(){
		setChanged();
		notifyObservers();
	}
	
	void addCompletedPomo(){
		actual++;
		changed();
	}

	public String toString(){
		return "Task name: " + name + ", estimated: " + estimated + ", actual: " + actual + ", Active: " + active;
	}
	
}
