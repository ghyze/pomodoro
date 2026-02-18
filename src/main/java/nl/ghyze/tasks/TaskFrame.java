package nl.ghyze.tasks;

import lombok.Getter;
import nl.ghyze.statistics.TaskStatisticsHook;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;

public class TaskFrame extends JFrame {

	private static final Logger logger = Logger.getLogger(TaskFrame.class.getName());

	private final SpringLayout layout = new SpringLayout();

	@Getter
	private final TaskHook taskHook = new TaskHook();
	private TaskStatisticsHook statisticsHook;

	private final TaskRepository taskRepository;
	private final List<Task> tasks;

	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
	private final JPanel todoTasksPanel = new JPanel();
	private final JPanel doneTasksPanel = new JPanel();

	private TaskPanel activePanel = null;

	public TaskFrame(final TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
		this.tasks = taskRepository.loadAll();
		this.taskHook.setTaskFrame(this);

		// Register state change listener for all loaded tasks
		tasks.forEach(this::registerTaskStateListener);

		this.setSize(800, 600);
		this.getContentPane().setLayout(layout);

		// Initialize task panels with BoxLayout
		todoTasksPanel.setLayout(new BoxLayout(todoTasksPanel, BoxLayout.PAGE_AXIS));
		doneTasksPanel.setLayout(new BoxLayout(doneTasksPanel, BoxLayout.PAGE_AXIS));

		// Add tabs to tabbed pane with scroll panes
		tabbedPane.addTab("Todo", new JScrollPane(todoTasksPanel));
		tabbedPane.addTab("Done", new JScrollPane(doneTasksPanel));
		tabbedPane.setSelectedIndex(0); // Default to Todo tab

		initTasks();

		// Add tabbed pane to frame (replaces single scrollPane)
		layout.putConstraint(SpringLayout.WEST, tabbedPane, 5, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, tabbedPane, -5, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, tabbedPane, 35, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, tabbedPane, -35, SpringLayout.SOUTH, getContentPane());
		this.add(tabbedPane);


		JButton btAddTask = new JButton(new AddAction());
		layout.putConstraint(SpringLayout.WEST, btAddTask, -120, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, btAddTask, -5, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, btAddTask, -30, SpringLayout.SOUTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, btAddTask, -5, SpringLayout.SOUTH, getContentPane());
		this.add(btAddTask);
		
	}
	
	private void initTasks(){
		// Clear both task panels
		todoTasksPanel.removeAll();
		doneTasksPanel.removeAll();

		// Remove active panel if it exists
		if (activePanel != null){
			activePanel.setVisible(false);
			this.remove(activePanel);
		}

		// Populate panels based on task state
		for (final Task task : tasks){
			final TaskPanel taskPanel = createTaskPanel(task);
			taskPanel.addMouseListener(new TaskPanelMouseAdapter());

			if (task.isActive()){
				// Active task stays at top (outside tabs)
				activePanel = taskPanel;
				layout.putConstraint(SpringLayout.WEST, activePanel, 5, SpringLayout.WEST, getContentPane());
				layout.putConstraint(SpringLayout.EAST, activePanel, -5, SpringLayout.EAST, getContentPane());
				layout.putConstraint(SpringLayout.NORTH, activePanel, 5, SpringLayout.NORTH, getContentPane());
				layout.putConstraint(SpringLayout.SOUTH, activePanel, 30, SpringLayout.NORTH, getContentPane());
				this.add(activePanel);
			} else {
				// Route to appropriate tab based on state
				if (task.getState() == TaskState.DONE) {
					doneTasksPanel.add(taskPanel);
				} else {
					// PENDING or IN_PROGRESS
					todoTasksPanel.add(taskPanel);
				}
			}
		}

		// Refresh all panels
		todoTasksPanel.revalidate();
		todoTasksPanel.repaint();
		doneTasksPanel.revalidate();
		doneTasksPanel.repaint();
		this.revalidate();
		this.repaint();
	}

	private TaskPanel createTaskPanel(final Task task) {
		return new TaskPanel(task, t -> {
			// Show confirmation dialog
			final int result = JOptionPane.showConfirmDialog(
					this,
					"Are you sure you want to remove task '" + t.getName() + "'?",
					"Confirm Removal",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);

			// Only remove if user confirmed
			if (result == JOptionPane.YES_OPTION) {
				// Log removal before removing
				if (statisticsHook != null) {
					statisticsHook.logRemoved(t);
				}
				tasks.remove(t);
				initTasks();
				// initTasks() already revalidates and repaints all panels
				saveTasks();
			}
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
		} catch (final Exception e) {
			logger.warning("Failed to save tasks: " + e.getMessage());
		}
	}

	/**
	 * Registers a property change listener on a task to handle:
	 * - Auto-deactivation when task is set to DONE
	 * - Automatic tab switching based on task state
	 */
	private void registerTaskStateListener(final Task task) {
		task.addPropertyChangeListener(evt -> {
			if ("state".equals(evt.getPropertyName())) {
				final TaskState newState = (TaskState) evt.getNewValue();

				// Auto-deactivate if set to DONE
				if (newState == TaskState.DONE && task.isActive()) {
					task.setActive(false);
					taskHook.setCurrentTask(null);
				}

				// Switch to appropriate tab based on new state
				if (newState == TaskState.DONE) {
					tabbedPane.setSelectedIndex(1); // Done tab
				} else {
					// PENDING or IN_PROGRESS
					tabbedPane.setSelectedIndex(0); // Todo tab
				}

				// Refresh UI and save
				initTasks();
				saveTasks();
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
