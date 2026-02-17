package nl.ghyze.statistics;

import nl.ghyze.tasks.Task;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Hook that logs task lifecycle events to CSV files.
 * Implements PropertyChangeListener to track state and pomodoro count changes.
 * Also provides explicit methods for creation, activation, editing, and removal events.
 */
public class TaskStatisticsHook implements PropertyChangeListener {

    private final StatisticsRepository repository;

    public TaskStatisticsHook(StatisticsRepository repository) {
        this.repository = repository;
    }

    /**
     * Listens for property changes on Task objects.
     * Logs state changes and pomodoro increments.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!(evt.getSource() instanceof Task task)) {
            return;
        }

        String propertyName = evt.getPropertyName();

        if ("state".equals(propertyName)) {
            logStateChanged(task);
        } else if ("actual".equals(propertyName)) {
            logPomoIncremented(task);
        }
    }

    /**
     * Logs a task created event.
     */
    public void logCreated(Task task) {
        TaskEvent event = TaskEvent.created(task);
        repository.logTaskEvent(event);
    }

    /**
     * Logs a task activated event.
     */
    public void logActivated(Task task) {
        TaskEvent event = TaskEvent.activated(task);
        repository.logTaskEvent(event);
    }

    /**
     * Logs a task deactivated event.
     */
    public void logDeactivated(Task task) {
        TaskEvent event = TaskEvent.deactivated(task);
        repository.logTaskEvent(event);
    }

    /**
     * Logs a task state changed event.
     */
    private void logStateChanged(Task task) {
        TaskEvent event = TaskEvent.stateChanged(task);
        repository.logTaskEvent(event);
    }

    /**
     * Logs a task pomodoro incremented event.
     */
    private void logPomoIncremented(Task task) {
        TaskEvent event = TaskEvent.pomoIncremented(task);
        repository.logTaskEvent(event);
    }

    /**
     * Logs a task edited event (name or estimated changed).
     */
    public void logEdited(Task task) {
        TaskEvent event = TaskEvent.edited(task);
        repository.logTaskEvent(event);
    }

    /**
     * Logs a task removed event.
     */
    public void logRemoved(Task task) {
        TaskEvent event = TaskEvent.removed(task);
        repository.logTaskEvent(event);
    }

    /**
     * Registers this hook as a listener on a task.
     */
    public void registerTask(Task task) {
        task.addPropertyChangeListener(this);
    }

    /**
     * Unregisters this hook from a task.
     */
    public void unregisterTask(Task task) {
        task.removePropertyChangeListener(this);
    }
}
