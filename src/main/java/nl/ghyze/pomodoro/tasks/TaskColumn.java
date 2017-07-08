package nl.ghyze.pomodoro.tasks;

public interface TaskColumn {
    Object getValue(Task task);

    void setValue(Task task, Object value);
}
