package nl.ghyze.tasks;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;

public class TaskFrame extends JFrame {

	private final SpringLayout layout = new SpringLayout();
	private final TaskService taskService;

	private final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.BOTTOM);
	private final JPanel todoTasksPanel = new JPanel();
	private final JPanel doneTasksPanel = new JPanel();

	private final TaskDetailPanel detailPanel = new TaskDetailPanel();
	private UUID selectedTaskId = null;

	private TaskPanel activePanel = null;

	public TaskFrame(final TaskService taskService) {
		this.taskService = taskService;
		taskService.setOnChanged(this::initTasks);

		this.setSize(800, 600);
		this.getContentPane().setLayout(layout);

		todoTasksPanel.setLayout(new BoxLayout(todoTasksPanel, BoxLayout.PAGE_AXIS));
		doneTasksPanel.setLayout(new BoxLayout(doneTasksPanel, BoxLayout.PAGE_AXIS));

		tabbedPane.addTab("Todo", new JScrollPane(todoTasksPanel));
		tabbedPane.addTab("Done", new JScrollPane(doneTasksPanel));
		tabbedPane.setSelectedIndex(0);

		initTasks();

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, detailPanel);
		splitPane.setResizeWeight(0.7);
		layout.putConstraint(SpringLayout.WEST, splitPane, 5, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, splitPane, -5, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, splitPane, 35, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, splitPane, -35, SpringLayout.SOUTH, getContentPane());
		this.add(splitPane);

		JButton btAddTask = new JButton(new AddAction());
		layout.putConstraint(SpringLayout.WEST, btAddTask, -120, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, btAddTask, -5, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, btAddTask, -30, SpringLayout.SOUTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, btAddTask, -5, SpringLayout.SOUTH, getContentPane());
		this.add(btAddTask);

		JButton btRemoveDone = new JButton(new RemoveAllDoneAction());
		layout.putConstraint(SpringLayout.WEST, btRemoveDone, 5, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, btRemoveDone, 155, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, btRemoveDone, -30, SpringLayout.SOUTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, btRemoveDone, -5, SpringLayout.SOUTH, getContentPane());
		btRemoveDone.setVisible(false);
		this.add(btRemoveDone);

		tabbedPane.addChangeListener(e -> btRemoveDone.setVisible(tabbedPane.getSelectedIndex() == 1));
	}

	private void initTasks() {
		todoTasksPanel.removeAll();
		doneTasksPanel.removeAll();

		if (activePanel != null) {
			activePanel.setVisible(false);
			this.remove(activePanel);
		}

		for (final Task task : taskService.getTasks()) {
			final TaskPanel taskPanel = createTaskPanel(task);
			taskPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					final Task clicked = ((TaskPanel) e.getSource()).getTask();
					selectedTaskId = clicked.getId();
					detailPanel.showTask(clicked);
					if (e.getClickCount() >= 2) {
						taskService.activateTask(clicked);
					}
				}
			});

			if (task.isActive()) {
				activePanel = taskPanel;
				layout.putConstraint(SpringLayout.WEST, activePanel, 5, SpringLayout.WEST, getContentPane());
				layout.putConstraint(SpringLayout.EAST, activePanel, -5, SpringLayout.EAST, getContentPane());
				layout.putConstraint(SpringLayout.NORTH, activePanel, 5, SpringLayout.NORTH, getContentPane());
				layout.putConstraint(SpringLayout.SOUTH, activePanel, 30, SpringLayout.NORTH, getContentPane());
				this.add(activePanel);
			} else {
				if (task.getState() == TaskState.DONE) {
					doneTasksPanel.add(taskPanel);
				} else {
					todoTasksPanel.add(taskPanel);
				}
			}
		}

		if (selectedTaskId != null) {
			taskService.getTasks().stream()
					.filter(t -> selectedTaskId.equals(t.getId()))
					.findFirst()
					.ifPresentOrElse(detailPanel::showTask, detailPanel::clear);
		}

		todoTasksPanel.revalidate();
		todoTasksPanel.repaint();
		doneTasksPanel.revalidate();
		doneTasksPanel.repaint();
		this.revalidate();
		this.repaint();
	}

	private TaskPanel createTaskPanel(final Task task) {
		final Consumer<Task> removeCallback = t -> {
			final int result = JOptionPane.showConfirmDialog(
					this,
					"Are you sure you want to remove task '" + t.getName() + "'?",
					"Confirm Removal",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (result == JOptionPane.YES_OPTION) {
				taskService.removeTask(t);
			}
		};
		return new TaskPanel(task, removeCallback, taskService::editTask);
	}

	private class AddAction extends AbstractAction {

		AddAction() {
			super("Add Task");
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			TaskDialog.createTask().ifPresent(taskService::addTask);
		}
	}

	private class RemoveAllDoneAction extends AbstractAction {

		RemoveAllDoneAction() {
			super("Remove All Done");
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			final int result = JOptionPane.showConfirmDialog(
					TaskFrame.this,
					"Remove all done tasks?",
					"Confirm Removal",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (result == JOptionPane.YES_OPTION) {
				taskService.removeAllDoneTasks();
			}
		}
	}
}
