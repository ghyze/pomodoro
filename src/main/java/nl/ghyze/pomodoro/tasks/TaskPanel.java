package nl.ghyze.pomodoro.tasks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

public class TaskPanel extends JPanel implements Observer {

	/**
	 * <code>serialVersionUID</code> indicates/is used for.
	 */
	private static final long serialVersionUID = 6925083513676349367L;

	private Task task;
	
	private JLabel lbName = new JLabel();

	private JLabel lbEstimated = new JLabel();

	private JLabel lbActual = new JLabel();
	
	private LayoutManager layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
	
	TaskPanel(Task task){
		this.task = task;
		task.addObserver(this);
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

		lbEstimated.setText(""+task.getEstimated());
		lbEstimated.setOpaque(true);
		lbEstimated.setMinimumSize(new Dimension(50, 25));
		lbEstimated.setPreferredSize(new Dimension(50, 25));
		lbEstimated.setMaximumSize(new Dimension(50, 25));
		this.add(lbEstimated);

		lbActual.setText(""+task.getActual());
		lbActual.setOpaque(true);
		lbActual.setMinimumSize(new Dimension(50, 25));
		lbActual.setPreferredSize(new Dimension(50, 25));
		lbActual.setMaximumSize(new Dimension(50, 25));
		this.add(lbActual);
		
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	Task getTask(){
		return task;
	}

    @Override
    public void update(Observable o, Object arg) {
        lbActual.setText(""+task.getActual());
    }
}
