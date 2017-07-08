package nl.ghyze.pomodoro.tasks;

public class ActualColumn implements TaskColumn {
    @Override
    public Object getValue(Task task) {
        return task.getActual();
    }

    @Override
    public void setValue(Task task, Object value) {
        String valueString = value.toString();
        task.setActual(Integer.valueOf(valueString));
    }
}
