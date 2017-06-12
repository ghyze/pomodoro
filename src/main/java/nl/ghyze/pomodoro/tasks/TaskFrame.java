package nl.ghyze.pomodoro.tasks;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TaskFrame extends JFrame {

	private JTable taskTable;
	private SpringLayout layout = new SpringLayout();
	private JScrollPane scrollPane;
	private TaskTableModel taskTableModel = new TaskTableModel();
	
	private TaskHook taskHook = new TaskHook();

	public TaskFrame() {
		this.setSize(800, 600);
		this.getContentPane().setLayout(layout);
		taskTable = new JTable(taskTableModel);
		scrollPane = new JScrollPane(taskTable);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 5, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, scrollPane, -5, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, scrollPane, -25, SpringLayout.SOUTH, getContentPane());
		this.add(scrollPane);
		
		taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		taskTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				Task task = taskTableModel.getTask(taskTable.getSelectedRow());
				if (task != null){
					taskHook.setCurrentTask(task);
				}
			}
			
		});
	}
	
	public TaskHook getTaskHook(){
		return taskHook;
	}

	public static void main(String[] args) {
		TaskFrame taskFrame = new TaskFrame();
		taskFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		taskFrame.setVisible(true);
	}
}
