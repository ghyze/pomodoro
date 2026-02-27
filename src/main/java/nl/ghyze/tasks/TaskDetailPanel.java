package nl.ghyze.tasks;

import javax.swing.*;
import java.awt.*;

class TaskDetailPanel extends JPanel {

    private final JLabel lbName = new JLabel("Select a task to view details");
    private final JTextArea txNotes = new JTextArea();

    TaskDetailPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        lbName.setFont(lbName.getFont().deriveFont(Font.BOLD, 14f));
        add(lbName, BorderLayout.NORTH);

        txNotes.setLineWrap(true);
        txNotes.setWrapStyleWord(true);
        txNotes.setEditable(false);
        add(new JScrollPane(txNotes), BorderLayout.CENTER);
    }

    void showTask(final Task task) {
        lbName.setText(task.getName());
        txNotes.setText(task.getNotes() != null ? task.getNotes() : "No notes.");
        txNotes.setCaretPosition(0);
    }

    void clear() {
        lbName.setText("Select a task to view details");
        txNotes.setText("");
    }
}
