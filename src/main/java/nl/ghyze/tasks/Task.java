package nl.ghyze.tasks;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.UUID;

@EqualsAndHashCode(exclude = "pcs")
@Data
public class Task {

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	@Getter
	private final UUID id;

	private String name;
	private int estimated;
	private int actual;

	private boolean active = false;

	private TaskState state = TaskState.PENDING;

	public Task(final String name, final int estimated){
		this.id = UUID.randomUUID();
		this.name = name;
		this.estimated = estimated;
	}

	public Task(final String name, final int estimated, final TaskState state){
		this.id = UUID.randomUUID();
		this.name = name;
		this.estimated = estimated;
		this.state = state;
	}

	// Constructor with explicit UUID for deserialization/migration
	public Task(final UUID id, final String name, final int estimated, final TaskState state){
		this.id = id;
		this.name = name;
		this.estimated = estimated;
		this.state = state;
	}

	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	public void addCompletedPomo(){
		final int oldActual = actual;
		actual++;
		pcs.firePropertyChange("actual", oldActual, actual);
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setEstimated(final int estimated) {
		this.estimated = estimated;
	}

	public void setState(final TaskState state) {
        if (this.state != state) {
            this.state = state;
            pcs.firePropertyChange("state", null, state);
        }
	}

	public String toString(){
		return "Task name: " + name + ", estimated: " + estimated + ", actual: " + actual + ", Active: " + active;
	}
	
}
