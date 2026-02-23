package nl.ghyze.tasks;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

class TaskDialog extends JDialog {

    private enum Result {
        NO_RESULT,
        OK,
        CANCEL
    }

    private static final int ELEMENT_HEIGHT = 30;
    private static final int LABEL_WIDTH = 110;

    private static final TaskDialog dialog = new TaskDialog();

    private final SpringLayout layout = new SpringLayout();

    private final JLabel lbName = new JLabel("Name");
    private final JLabel lbEstimate = new JLabel("Estimated pomos");
    private final JLabel lbState = new JLabel("State");

    private final JTextField tfName = new JTextField();
    private final JSpinner spEstimate = new JSpinner(new SpinnerNumberModel(1,0,25,1));
    private final JComboBox<TaskState> cbState = new JComboBox<>(TaskState.values());

    private final JButton btAdd = new JButton("OK");
    private final JButton btCancel = new JButton("Cancel");

    private Result result;
//    private boolean editMode = false;
//    private Task editTask = null;

    private TaskDialog(){
        super((Dialog) null, true);
        result = Result.NO_RESULT;
        this.init();
    }

    private void init(){
        this.setSize(300, 240);
        this.getContentPane().setLayout(layout);

        layout.putConstraint(SpringLayout.WEST, lbName, 5, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, lbName, LABEL_WIDTH, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, lbName, 5, SpringLayout.NORTH, getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, lbName, ELEMENT_HEIGHT, SpringLayout.NORTH, getContentPane());
        this.getContentPane().add(lbName);

        layout.putConstraint(SpringLayout.WEST, tfName, 10 + LABEL_WIDTH, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, tfName, -5, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, tfName, 5, SpringLayout.NORTH, getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, tfName, ELEMENT_HEIGHT, SpringLayout.NORTH, getContentPane());
        this.getContentPane().add(tfName);

        layout.putConstraint(SpringLayout.WEST, lbEstimate, 5, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, lbEstimate, LABEL_WIDTH, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, lbEstimate, 5, SpringLayout.SOUTH, lbName);
        layout.putConstraint(SpringLayout.SOUTH, lbEstimate, ELEMENT_HEIGHT, SpringLayout.SOUTH, lbName);
        this.getContentPane().add(lbEstimate);

        layout.putConstraint(SpringLayout.WEST, spEstimate, 10 + LABEL_WIDTH, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, spEstimate, -5, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, spEstimate, 5, SpringLayout.SOUTH, tfName);
        layout.putConstraint(SpringLayout.SOUTH, spEstimate, ELEMENT_HEIGHT, SpringLayout.SOUTH, tfName);
        this.getContentPane().add(spEstimate);

        layout.putConstraint(SpringLayout.WEST, lbState, 5, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, lbState, LABEL_WIDTH, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, lbState, 5, SpringLayout.SOUTH, lbEstimate);
        layout.putConstraint(SpringLayout.SOUTH, lbState, ELEMENT_HEIGHT, SpringLayout.SOUTH, lbEstimate);
        this.getContentPane().add(lbState);

        layout.putConstraint(SpringLayout.WEST, cbState, 10 + LABEL_WIDTH, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, cbState, -5, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, cbState, 5, SpringLayout.SOUTH, spEstimate);
        layout.putConstraint(SpringLayout.SOUTH, cbState, ELEMENT_HEIGHT, SpringLayout.SOUTH, spEstimate);
        this.getContentPane().add(cbState);

        cbState.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
                final var label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TaskState taskState) {
                    label.setText(taskState.getName());
                }
                return label;
            }
        });

        layout.putConstraint(SpringLayout.WEST, btCancel, -LABEL_WIDTH, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, btCancel, -5, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, btCancel, -ELEMENT_HEIGHT, SpringLayout.SOUTH, getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, btCancel, -5, SpringLayout.SOUTH, getContentPane());
        this.getContentPane().add(btCancel);

        layout.putConstraint(SpringLayout.WEST, btAdd, -LABEL_WIDTH, SpringLayout.WEST, btCancel);
        layout.putConstraint(SpringLayout.EAST, btAdd, -5, SpringLayout.WEST, btCancel);
        layout.putConstraint(SpringLayout.NORTH, btAdd, -ELEMENT_HEIGHT, SpringLayout.SOUTH, getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, btAdd, -5, SpringLayout.SOUTH, getContentPane());
        this.getContentPane().add(btAdd);

        btAdd.addActionListener(e -> {
            result = Result.OK;
            dialog.setVisible(false);
        });

        btCancel.addActionListener(e -> {
            result = Result.CANCEL;
            dialog.setVisible(false);
        });
    }

    private static void showDialog(){
//        dialog.editMode = false;
//        dialog.editTask = null;
        dialog.tfName.setText("");
        dialog.spEstimate.setValue(1);
        dialog.cbState.setSelectedItem(TaskState.PENDING);
        dialog.setVisible(true);
    }

    private static void showEditDialog(final Task task){
//        dialog.editMode = true;
//        dialog.editTask = task;
        dialog.tfName.setText(task.getName());
        dialog.spEstimate.setValue(task.getEstimated());
        dialog.cbState.setSelectedItem(task.getState());
        dialog.setVisible(true);
    }

    static Optional<Task> createTask(){
        showDialog();
        if (dialog.result == Result.OK) {
            final String taskName = dialog.tfName.getText();
            final Integer taskEstimated = (Integer) dialog.spEstimate.getValue();
            final TaskState taskState = (TaskState) dialog.cbState.getSelectedItem();
            return Optional.of(Task.builder()
                            .name(taskName)
                            .estimated(taskEstimated)
                            .state(taskState)
                    .build());
        }
        return Optional.empty();
    }

    static boolean editTask(final Task task){
        showEditDialog(task);
        if (dialog.result == Result.OK) {
            task.setName(dialog.tfName.getText());
            task.setEstimated((Integer) dialog.spEstimate.getValue());
            task.setState((TaskState) dialog.cbState.getSelectedItem());
            return true;
        }
        return false;
    }
}

