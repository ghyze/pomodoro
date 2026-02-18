package nl.ghyze.tasks;

import lombok.Getter;
import nl.ghyze.statistics.TaskStatisticsHook;

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

public class TaskFrame extends JFrame {

	private final SpringLayout layout = new SpringLayout();

	@Getter
	private final TaskHook taskHook = new TaskHook();
	private TaskStatisticsHook statisticsHook;

	private final TaskRepository taskRepository;
	private final List<Task> tasks;

	private final JPanel tasksPanel = new JPanel();

	private TaskPanel activePanel = null;

	public TaskFrame(final TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
		this.tasks = taskRepository.loadAll();
		this.taskHook.setTaskFrame(this);

		// Register state change listener for all loaded tasks
		tasks.forEach(this::registerTaskStateListener);

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
		for (final Task task : tasks){
			final TaskPanel taskPanel = createTaskPanel(task);
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
		this.repaint();
	}

	private TaskPanel createTaskPanel(final Task task) {
		return new TaskPanel(task, t -> {
			// Log removal before removing
			if (statisticsHook != null) {
				statisticsHook.logRemoved(t);
			}
			tasks.remove(t);
			initTasks();
			tasksPanel.revalidate();
			tasksPanel.repaint();
			this.revalidate();
			this.repaint();
			saveTasks();
		}, this::saveTasks, statisticsHook);
	}

	public void setStatisticsHook(final TaskStatisticsHook statisticsHook) {
		this.statisticsHook = statisticsHook;
		// Register hook with all existing tasks
		tasks.forEach(statisticsHook::registerTask);
	}

	public void saveTasks() {
		try {
			taskRepository.saveAll(tasks);
		} catch (Exception e) {
			System.err.println("Failed to save tasks: " + e.getMessage());
		}
	}

	/**
	 * Registers a property change listener on a task to handle auto-deactivation
	 * when the task is set to DONE.
	 */
	private void registerTaskStateListener(final Task task) {
		task.addPropertyChangeListener(evt -> {
			if ("state".equals(evt.getPropertyName()) && evt.getNewValue() == TaskState.DONE) {
				// If this task is active and was just set to DONE, deactivate it
				if (task.isActive()) {
					task.setActive(false);
					taskHook.setCurrentTask(null);
					initTasks();
					saveTasks();
				}
			}
		});
	}

	private class AddAction extends AbstractAction {

		AddAction(){
			super("Add Task");
		}

		public void actionPerformed(final ActionEvent e){
			TaskDialog.createTask()
					.ifPresent(task -> {
						tasks.add(task);
						// Register state change listener for the new task
						registerTaskStateListener(task);
						if (statisticsHook != null) {
							statisticsHook.registerTask(task);
							statisticsHook.logCreated(task);
						}
					});
			initTasks();
			saveTasks();
		}

	}

	private class TaskPanelMouseAdapter extends MouseAdapter {
		public void mouseClicked(final MouseEvent e){
			if (e.getClickCount() >= 2){
				final TaskPanel source = (TaskPanel) e.getSource();
				final Task newActiveTask = source.getTask();

				// Deactivate currently active task(s) and transition state
				tasks.stream()
						.filter(Task::isActive)
						.forEach(task -> {
							// Log deactivation
							if (statisticsHook != null) {
								statisticsHook.logDeactivated(task);
							}
							// Auto-transition: IN_PROGRESS → PENDING on deactivation (unless DONE)
							if (task.getState() == TaskState.IN_PROGRESS) {
								task.setState(TaskState.PENDING);
							}
							task.setActive(false);
						});

				// Activate the new task
				newActiveTask.setActive(true);
				taskHook.setCurrentTask(newActiveTask);

				// Auto-transition: PENDING → IN_PROGRESS on activation
				if (newActiveTask.getState() == TaskState.PENDING) {
					newActiveTask.setState(TaskState.IN_PROGRESS);
				}

				// Log activation for the newly active task
				if (statisticsHook != null) {
					statisticsHook.logActivated(newActiveTask);
				}

				initTasks();
				saveTasks();
			}
		}
	}
}
