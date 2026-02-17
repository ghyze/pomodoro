package nl.ghyze.statistics;

import nl.ghyze.tasks.Task;
import nl.ghyze.tasks.TaskState;

import java.time.Instant;
import java.util.UUID;

/**
 * Immutable record representing a task lifecycle event.
 * Used for logging task changes to CSV files.
 */
public record TaskEvent(
        Instant timestamp,
        UUID taskId,
        String eventType,      // created, activated, deactivated, state_changed, pomo_incremented, edited, removed
        String taskName,
        TaskState taskState,
        int pomoActual,
        int pomoEstimated
) {
    /**
     * Creates a task event from a Task object.
     */
    private static TaskEvent fromTask(String eventType, Task task) {
        return new TaskEvent(
                Instant.now(),
                task.getId(),
                eventType,
                task.getName(),
                task.getState(),
                task.getActual(),
                task.getEstimated()
        );
    }

    /**
     * Creates a task created event.
     */
    public static TaskEvent created(Task task) {
        return fromTask("created", task);
    }

    /**
     * Creates a task activated event.
     */
    public static TaskEvent activated(Task task) {
        return fromTask("activated", task);
    }

    /**
     * Creates a task deactivated event.
     */
    public static TaskEvent deactivated(Task task) {
        return fromTask("deactivated", task);
    }

    /**
     * Creates a task state changed event.
     */
    public static TaskEvent stateChanged(Task task) {
        return fromTask("state_changed", task);
    }

    /**
     * Creates a task pomodoro incremented event.
     */
    public static TaskEvent pomoIncremented(Task task) {
        return fromTask("pomo_incremented", task);
    }

    /**
     * Creates a task edited event.
     */
    public static TaskEvent edited(Task task) {
        return fromTask("edited", task);
    }

    /**
     * Creates a task removed event.
     */
    public static TaskEvent removed(Task task) {
        return fromTask("removed", task);
    }
}
