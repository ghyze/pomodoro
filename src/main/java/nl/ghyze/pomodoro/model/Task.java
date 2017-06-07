package nl.ghyze.pomodoro.model;

import java.util.Observable;

public class Task extends Observable{

	private String name = "";
	private int estimated ;
	private int actual;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		changed();
	}
	public int getEstimated() {
		return estimated;
	}
	public void setEstimated(int estimated) {
		this.estimated = estimated;
		changed();
	}
	public int getActual() {
		return actual;
	}
	public void setActual(int actual) {
		this.actual = actual;
		changed();
	}
	
	private void changed(){
		setChanged();
		notifyObservers();
	}
	
	public boolean isEmpty(){
		return (name == null || name.length() == 0) && estimated == 0 && actual == 0;
	}
	
	public void addCompletedPomo(){
		System.out.println("Completed pomo added to "+name);
		actual++;
	}
	
	
}
