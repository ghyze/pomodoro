package nl.ghyze.pomodoro.tasks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class TaskPanel extends JPanel{

	/**
	 * <code>serialVersionUID</code> indicates/is used for.
	 */
	private static final long serialVersionUID = 6925083513676349367L;

	private Task task;
	
	private JLabel lbName = new JLabel();
	private JTextField tfName = new JTextField();
	
	private JLabel lbEstimated = new JLabel();
	private JTextField tfEstimated = new JTextField();
	
	private JLabel lbActual = new JLabel();
	
	private LayoutManager layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
	
	public TaskPanel(Task task){
		this.task = task;
		init();
	}
	
	private void init(){

		this.setSize(200, 30);
		this.setLayout(layout);
		
		lbName.setText(task.getName());
		lbName.setOpaque(true);
		lbName.setMinimumSize(new Dimension(200, 25));
		lbName.setPreferredSize(new Dimension(200, 25));
		lbName.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
		this.add(lbName);
		lbName.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (!task.isActive()) {
					lbName.setVisible(false);
					tfName.setVisible(true);
					tfName.requestFocusInWindow();
				}
			}
		});
		
		tfName.setText(task.getName());
		tfName.setOpaque(true);
		tfName.setMinimumSize(new Dimension(200, 25));
		tfName.setPreferredSize(new Dimension(200, 25));
		tfName.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
		tfName.setVisible(false);
		this.add(tfName);
		tfName.addFocusListener(new FocusAdapter(){
			public void focusLost(FocusEvent e){
				showNameLabel();
			}
		});
		tfName.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				System.out.println(e.getKeyCode() +" || " + KeyEvent.VK_ENTER );
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					showNameLabel();
				}
			}
		});
		
		lbEstimated.setText(""+task.getEstimated());
		lbEstimated.setOpaque(true);
		lbEstimated.setMinimumSize(new Dimension(50, 25));
		lbEstimated.setPreferredSize(new Dimension(50, 25));
		lbEstimated.setMaximumSize(new Dimension(50, 25));
		this.add(lbEstimated);
		lbEstimated.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if (!task.isActive()) {
					lbEstimated.setVisible(false);
					tfEstimated.setVisible(true);
					tfEstimated.requestFocusInWindow();
				}	
			}
		});
	
		tfEstimated.setText(""+task.getEstimated());
		tfEstimated.setOpaque(true);
		tfEstimated.setMinimumSize(new Dimension(50, 25));
		tfEstimated.setPreferredSize(new Dimension(50, 25));
		tfEstimated.setMaximumSize(new Dimension(50, 25));
		tfEstimated.setVisible(false);
		this.add(tfEstimated);
		
		tfEstimated.addFocusListener(new FocusAdapter(){
			public void focusLost(FocusEvent e){
				showEstimatedLabel();
			}
		});
		tfEstimated.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				System.out.println(e.getKeyCode() +" || " + KeyEvent.VK_ENTER );
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					showEstimatedLabel();
				}
			}
		});
		
		
		
		lbActual.setText(""+task.getActual());
		lbActual.setOpaque(true);
		lbActual.setMinimumSize(new Dimension(50, 25));
		lbActual.setPreferredSize(new Dimension(50, 25));
		lbActual.setMaximumSize(new Dimension(50, 25));
		this.add(lbActual);
		
		
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.black)); 
		
	}
	
	public void showNameLabel(){
		String name = tfName.getText();
		System.out.println(name);
		task.setName(name);
		lbName.setText(name);
		tfName.setVisible(false);
		lbName.setVisible(true);
	}
	
	public void showEstimatedLabel() {
		try {
			Integer estimated = Integer.valueOf(tfEstimated.getText());
			System.out.println(estimated);
			task.setEstimated(estimated);
			lbEstimated.setText(estimated.toString());
		} catch (Exception ex) {
			task.setEstimated(0);
			ex.printStackTrace();
		}
		tfEstimated.setVisible(false);
		lbEstimated.setVisible(true);
	}
	
	public Task getTask(){
		return task;
	}
}
