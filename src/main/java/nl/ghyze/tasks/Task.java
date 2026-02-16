package nl.ghyze.tasks;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

@EqualsAndHashCode(exclude = "pcs")
@Data
public class Task {

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private final String name;
	private final int estimated;
	private int actual;

	private boolean active = false;

	public Task(final String name, final int estimated){
		this.name = name;
		this.estimated = estimated;
	}

	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	private void changed(){
		pcs.firePropertyChange("actual", null, actual);
	}

	public void addCompletedPomo(){
		final int oldActual = actual;
		actual++;
		pcs.firePropertyChange("actual", oldActual, actual);
	}

	public String toString(){
		return "Task name: " + name + ", estimated: " + estimated + ", actual: " + actual + ", Active: " + active;
	}
	
}
