package nl.ghyze.pomodoro.tasks;

import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

public class TaskTableModel extends AbstractTableModel implements Observer {

	private final String[] columnNames;
	private TaskManager taskManager;
	
	public TaskTableModel() {
		taskManager = new TaskManager();
		taskManager.addObserver(this);
		columnNames = new String[] { "Name", "Expected", "Actual" };
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
		if (isIndexAvailable(rowIndex, columnIndex)) {
			Task task = taskManager.getTask(rowIndex);
			switch (columnIndex) {
			case 0:
				return task.getName();
			case 1:
				return task.getEstimated();
			case 2:
				return task.getActual();
			default:
			}
		}
		return null;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (isIndexAvailable(rowIndex, columnIndex)) {
			Task task = taskManager.getTask(rowIndex);
			String valueString = value.toString();
			switch (columnIndex) {
			case 0:
				task.setName(valueString);
				break;
			case 1:
				task.setEstimated(Integer.valueOf(valueString).intValue());
				break;
			case 2:
				task.setActual(Integer.valueOf(valueString).intValue());
				break;
			default:
			}
		}
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
	
	public Task getTask(int index){
		return taskManager.getTask(index);
	}

}
