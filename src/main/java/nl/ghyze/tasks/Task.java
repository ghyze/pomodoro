package nl.ghyze.tasks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.UUID;

@EqualsAndHashCode(exclude = "pcs")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Task {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    @Builder.Default
    private final UUID id = UUID.randomUUID();

    @Setter
    private String name;

    @Setter
    private int estimated;
    private int actual;

    @Builder.Default
    private boolean active = false;

    @Builder.Default
    private TaskState state = TaskState.PENDING;

    @Setter
    private String notes;

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
