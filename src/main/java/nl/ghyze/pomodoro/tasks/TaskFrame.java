package nl.ghyze.pomodoro.tasks;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import nl.ghyze.pomodoro.persistence.TaskRepository;

public class TaskFrame extends JFrame {

	private SpringLayout layout = new SpringLayout();

	private TaskHook taskHook = new TaskHook();

	private TaskRepository taskRepository;
	private List<Task> tasks;

	private JPanel tasksPanel = new JPanel();

	private TaskPanel activePanel = null;

	public TaskFrame(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
		this.tasks = taskRepository.loadAll();
		this.taskHook.setTaskFrame(this);

		this.setSize(800, 600);
		this.getContentPane().setLayout(layout);
		
		tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.PAGE_AXIS	));
		initTasks();

		JScrollPane scrollPane = new JScrollPane(tasksPanel);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 5, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, scrollPane, -5, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 35, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, scrollPane, -35, SpringLayout.SOUTH, getContentPane());
		this.add(scrollPane);


		JButton btAddTask = new JButton(new AddAction());
		layout.putConstraint(SpringLayout.WEST, btAddTask, -120, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, btAddTask, -5, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, btAddTask, -30, SpringLayout.SOUTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, btAddTask, -5, SpringLayout.SOUTH, getContentPane());
		this.add(btAddTask);
		
	}
	
	private void initTasks(){
		tasksPanel.removeAll();
		if (activePanel != null){
			activePanel.setVisible(false);
			this.remove(activePanel);
		}
		tasksPanel.invalidate();
		for (Task task : tasks){
			TaskPanel taskPanel = createTaskPanel(task);
			taskPanel.addMouseListener(new TaskPanelMouseAdapter());

			if (task.isActive()){
				activePanel = taskPanel;
				layout.putConstraint(SpringLayout.WEST, activePanel, 5, SpringLayout.WEST, getContentPane());
				layout.putConstraint(SpringLayout.EAST, activePanel, -5, SpringLayout.EAST, getContentPane());
				layout.putConstraint(SpringLayout.NORTH, activePanel, 5, SpringLayout.NORTH, getContentPane());
				layout.putConstraint(SpringLayout.SOUTH, activePanel, 30, SpringLayout.NORTH, getContentPane());
				this.add(activePanel);
			} else {
				tasksPanel.add(taskPanel);
			}
		}
		this.revalidate();
	}
	
	private TaskPanel createTaskPanel(Task task) {
		return new TaskPanel(task);
	}
	
	public TaskHook getTaskHook() {
		return taskHook;
	}

	public void saveTasks() {
		try {
			taskRepository.saveAll(tasks);
		} catch (Exception e) {
			System.err.println("Failed to save tasks: " + e.getMessage());
		}
	}

	private class AddAction extends AbstractAction {

		AddAction(){
			super("Add Task");
		}

		public void actionPerformed(ActionEvent e){
			AddTaskDialog.createTask()
					.ifPresent(tasks::add);
			initTasks();
			saveTasks();
		}

	}

	private class TaskPanelMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e){
			if (e.getClickCount() >= 2){
				tasks.forEach(task -> task.setActive(false));
				TaskPanel source = (TaskPanel) e.getSource();
				source.getTask().setActive(true);
				taskHook.setCurrentTask(source.getTask());
				initTasks();
				saveTasks();
			}
		}
	}
}
