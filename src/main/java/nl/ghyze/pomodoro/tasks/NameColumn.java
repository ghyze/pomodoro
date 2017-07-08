package nl.ghyze.pomodoro.tasks;

public class NameColumn implements TaskColumn {

    @Override
    public Object getValue(Task task) {
        return task.getName();
    }

    @Override
    public void setValue(Task task, Object value) {
        String valueString = value.toString();
        task.setName(valueString);
    }
}
