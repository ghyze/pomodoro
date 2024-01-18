package nl.ghyze.pomodoro.tasks;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

class AddTaskDialog extends JDialog {

    private enum Result {
        NO_RESULT,
        OK,
        CANCEL
    }

    private static final int ELEMENT_HEIGHT = 30;
    private static final int LABEL_WIDTH = 110;

    private static final AddTaskDialog dialog = new AddTaskDialog();

    private final SpringLayout layout = new SpringLayout();

    private final JLabel lbName = new JLabel("Name");
    private final JLabel lbEstimate = new JLabel("Estimated pomos");

    private final JTextField tfName = new JTextField();
    private final JSpinner spEstimate = new JSpinner(new SpinnerNumberModel(1,0,25,1));

    private final JButton btAdd = new JButton("Add");
    private final JButton btCancel = new JButton("Cancel");

    private Result result;

    private AddTaskDialog(){
        super((Dialog) null, true);
        result = Result.NO_RESULT;
        this.init();
    }

    private void init(){
        this.setSize(300, 200);
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
        dialog.tfName.setText("");
        dialog.spEstimate.setValue(1);
        dialog.setVisible(true);
    }

    static Optional<Task> createTask(){
        showDialog();
        if (dialog.result == Result.OK) {
            String taskName = dialog.tfName.getText();
            Integer taskEstimated = (Integer) dialog.spEstimate.getValue();
            return Optional.of(new Task(taskName, taskEstimated));
        }
        return Optional.empty();
    }
}
