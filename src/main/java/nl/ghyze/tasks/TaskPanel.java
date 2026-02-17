package nl.ghyze.tasks;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Consumer;

import javax.swing.*;

public class TaskPanel extends JPanel implements PropertyChangeListener {

	/**
	 * <code>serialVersionUID</code> indicates/is used for.
	 */
	private static final long serialVersionUID = 6925083513676349367L;

	private final Task task;
	
	private final JLabel lbName = new JLabel();

	private final JLabel lbEstimated = new JLabel();

	private final JLabel lbActual = new JLabel();
	
	private final JComboBox<TaskState> cbState = new JComboBox<>(TaskState.values());
	private final JButton btnEdit = new JButton("...");
	private final JButton btnRemove = new JButton("X");
	private final Consumer<Task> removeTaskCallback;
	private final Runnable saveCallback;

	private final LayoutManager layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
	
	TaskPanel(final Task task, final Consumer<Task> removeTaskCallback, final Runnable saveCallback){
		this.task = task;
		this.removeTaskCallback = removeTaskCallback;
		this.saveCallback = saveCallback;
		task.addPropertyChangeListener(this);
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
		lbEstimated.setMinimumSize(new Dimension(30, 25));
		lbEstimated.setPreferredSize(new Dimension(30, 25));
		lbEstimated.setMaximumSize(new Dimension(30, 25));
		this.add(lbEstimated);

		lbActual.setText(""+task.getActual());
		lbActual.setOpaque(true);
		lbActual.setMinimumSize(new Dimension(30, 25));
		lbActual.setPreferredSize(new Dimension(30, 25));
		lbActual.setMaximumSize(new Dimension(30, 25));
		this.add(lbActual);

		// Dropdown for TaskState
		cbState.setSelectedItem(task.getState());
		cbState.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
				final var label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof TaskState) {
					label.setText(((TaskState) value).getName());
				}
				return label;
			}
		});
		cbState.addActionListener(e -> {
			task.setState((TaskState) cbState.getSelectedItem());
		});
        cbState.setMinimumSize(new Dimension(100, 25));
        cbState.setPreferredSize(new Dimension(100, 25));
        cbState.setMaximumSize(new Dimension(100, 25));
		this.add(cbState);

		btnEdit.setMinimumSize(new Dimension(30, 25));
		btnEdit.setPreferredSize(new Dimension(30, 25));
		btnEdit.setMaximumSize(new Dimension(30, 25));
		btnEdit.addActionListener(e -> {
			if (TaskDialog.editTask(task)) {
				lbName.setText(task.getName());
				lbEstimated.setText(""+task.getEstimated());
				cbState.setSelectedItem(task.getState());
				saveCallback.run();
			}
		});
		this.add(btnEdit);

		btnRemove.setMargin(new Insets(0, 2, 0, 2));
		btnRemove.setMinimumSize(new Dimension(30, 25));
		btnRemove.setPreferredSize(new Dimension(30, 25));
		btnRemove.setMaximumSize(new Dimension(30, 25));
		btnRemove.addActionListener(e -> removeTaskCallback.accept(task));
		this.add(btnRemove);

		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	Task getTask(){
		return task;
	}

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if ("actual".equals(evt.getPropertyName())) {
            lbActual.setText(""+task.getActual());
        }
        if ("state".equals(evt.getPropertyName())) {
            cbState.setSelectedItem(task.getState());
        }
    }
}
