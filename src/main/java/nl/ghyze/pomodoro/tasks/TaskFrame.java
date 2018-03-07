package nl.ghyze.pomodoro.tasks;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

public class TaskFrame extends JFrame {

	private SpringLayout layout = new SpringLayout();
	private JScrollPane scrollPane;
	
	private TaskHook taskHook = new TaskHook();
	
	private List<Task> tasks = new ArrayList<>();
	
	private JButton btAddTask = new JButton(new AddAction());
	
	private JPanel tasksPanel = new JPanel();
	
	private TaskPanel activePanel = null;

	public TaskFrame() {
		this.setSize(800, 600);
		this.getContentPane().setLayout(layout);
		
		tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.PAGE_AXIS	));
		initTasks();
		
		scrollPane = new JScrollPane(tasksPanel);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 5, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, scrollPane, -5, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 35, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, scrollPane, -35, SpringLayout.SOUTH, getContentPane());
		this.add(scrollPane);
		

		layout.putConstraint(SpringLayout.WEST, btAddTask, -120, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, btAddTask, -5, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, btAddTask, -30, SpringLayout.SOUTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, btAddTask, -5, SpringLayout.SOUTH, getContentPane());
		this.add(btAddTask);
		
	}
	
	private void initTasks(){
		System.out.println("Number of tasks: "+tasks.size());
		tasksPanel.removeAll();
		if (activePanel != null){
			activePanel.setVisible(false);
			this.remove(activePanel);
		}
		tasksPanel.invalidate();
		for (Task task : tasks){
			TaskPanel taskPanel = createTaskPanel(task);
			taskPanel.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					if (e.getClickCount() >= 2){
						System.out.println("Doubleclick detected");
						for (Task task : tasks){
							task.setActive(false);
						}
						TaskPanel source = (TaskPanel) e.getSource();
						source.getTask().setActive(true);
						initTasks();
					}
				}
			});

			if (task.isActive()){
				System.out.println("Active panel: "+task.getName());
				activePanel = taskPanel;
				layout.putConstraint(SpringLayout.WEST, activePanel, 5, SpringLayout.WEST, getContentPane());
				layout.putConstraint(SpringLayout.EAST, activePanel, -5, SpringLayout.EAST, getContentPane());
				layout.putConstraint(SpringLayout.NORTH, activePanel, 5, SpringLayout.NORTH, getContentPane());
				layout.putConstraint(SpringLayout.SOUTH, activePanel, 30, SpringLayout.NORTH, getContentPane());
				this.add(activePanel);
			} else {
				System.out.println("NonActive panel: "+task.getName());
				tasksPanel.add(taskPanel);
			}
		}
		this.revalidate();
	}
	
	private TaskPanel createTaskPanel(Task task) {
		TaskPanel panel = new TaskPanel(task);
		return panel;
	}
	
	public TaskHook getTaskHook(){
		return taskHook;
	}

	public static void main(String[] args) {
		TaskFrame taskFrame = new TaskFrame();
		taskFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		taskFrame.setVisible(true);
	}
	
	class AddAction extends AbstractAction{
		
		public AddAction(){
			super("Add Task");
		}
		
		public void actionPerformed(ActionEvent e){
			tasks.add(new Task());
			initTasks();
		}
	}
}
