package nl.ghyze.pomodoro.tasks;

import javax.swing.table.AbstractTableModel;
import java.util.Observable;
import java.util.Observer;

import static nl.ghyze.pomodoro.tasks.Column.*;

public class TaskTableModel extends AbstractTableModel implements Observer {

    private final String[] columnNames;
    private TaskManager taskManager;

    TaskTableModel() {
        taskManager = new TaskManager();
        taskManager.addObserver(this);
        columnNames = new String[]{NAME.getName(), EXPECTED.getName(), ACTUAL.getName()};
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public int getRowCount() {
        return taskManager.getNumberOfTasks();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    public boolean isCellEditable(int row, int col) {
        return col != 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        assert isIndexAvailable(rowIndex, columnIndex) : "Invalid index for setting value";
        Task task = taskManager.getTask(rowIndex);
        Column column = Column.getByIndex(columnIndex);
        return column.getValue(task);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        assert isIndexAvailable(rowIndex, columnIndex) : "Invalid index for setting value";
        Task task = taskManager.getTask(rowIndex);
        Column column = Column.getByIndex(columnIndex);
        column.setValue(task, value);
    }

    private boolean isIndexAvailable(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            return false;
        }
        return columnIndex >= 0 && columnIndex < getColumnCount();
    }


    @Override
    public void update(Observable o, Object arg) {
        taskManager.update();
    }

    Task getTask(int index) {
        return taskManager.getTask(index);
    }

}
