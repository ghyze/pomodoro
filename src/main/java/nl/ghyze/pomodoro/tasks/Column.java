package nl.ghyze.pomodoro.tasks;

public enum Column {

    NAME("Name", new NameColumn()),
    EXPECTED("Estimated", new EstimatedColumn()),
    ACTUAL("Actual", new ActualColumn());

    private String name;
    private TaskColumn taskColumn;

    Column(String name, TaskColumn taskColumn) {
        this.name = name;
        this.taskColumn = taskColumn;
    }

    public static Column getByIndex(int index) {
        Column[] values = values();
        assert index > 0 && index < values.length : "Incorrect ID for Column";
        return values[index];
    }

    public String getName() {
        return name;
    }

    public Object getValue(Task task) {
        return taskColumn.getValue(task);
    }

    public void setValue(Task task, Object value) {
        taskColumn.setValue(task, value);
    }
}
