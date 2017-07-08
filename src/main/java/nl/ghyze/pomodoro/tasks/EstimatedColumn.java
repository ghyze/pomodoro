package nl.ghyze.pomodoro.tasks;

public class EstimatedColumn implements TaskColumn {
    @Override
    public Object getValue(Task task) {
        return task.getEstimated();
    }

    @Override
    public void setValue(Task task, Object value) {
        String valueString = value.toString();
        task.setEstimated(Integer.valueOf(valueString));
    }
}
