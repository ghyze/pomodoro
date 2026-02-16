package nl.ghyze.settings;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * Panel for configuring idle time and auto-reset settings.
 */
public class IdleSettingsPanel extends JPanel {
    private static final int LABEL_WIDTH = 200;

    private final SpringLayout layout = new SpringLayout();

    private final JCheckBox cbAutoReset = new JCheckBox("Autoreset after idle time");
    private final JTextField tfIdleTime = new JTextField();

    public IdleSettingsPanel(final Settings settings) {
        setLayout(layout);
        setPreferredSize(new java.awt.Dimension(400, 60)); // Ensure panel is visible
        initComponents(settings);
    }

    private void initComponents(final Settings settings) {
        // Auto-reset checkbox
        layout.putConstraint(SpringLayout.WEST, cbAutoReset, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, cbAutoReset, LABEL_WIDTH, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, cbAutoReset, 5, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, cbAutoReset, 25, SpringLayout.NORTH, this);
        add(cbAutoReset);
        cbAutoReset.addActionListener(event -> tfIdleTime.setEnabled(cbAutoReset.isSelected()));
        cbAutoReset.setSelected(settings.isAutoreset());

        // Idle time field
        layout.putConstraint(SpringLayout.WEST, tfIdleTime, 5 + LABEL_WIDTH, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, tfIdleTime, -5, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, tfIdleTime, 5, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, tfIdleTime, 25, SpringLayout.NORTH, this);
        add(tfIdleTime);
        tfIdleTime.setText("" + settings.getIdleTime());
        tfIdleTime.setEnabled(cbAutoReset.isSelected());
        InputValidator.attachValidationListeners(tfIdleTime, "Idle time", this);
    }

    /**
     * Checks if auto-reset is enabled.
     * @return true if auto-reset checkbox is selected
     */
    public boolean isAutoResetEnabled() {
        return cbAutoReset.isSelected();
    }

    /**
     * Gets the idle time in minutes.
     * @return The idle time duration, or -1 if invalid
     */
    public int getIdleTime() {
        try {
            return Integer.parseInt(tfIdleTime.getText());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Validates all fields in this panel.
     * Only validates the idle time field if auto-reset is enabled.
     * @return true if all fields are valid, false otherwise
     */
    public boolean validateAll() {
        // Only validate idle time if auto-reset is enabled
        if (!tfIdleTime.isEnabled()) {
            return true;
        }

        final String text = tfIdleTime.getText();
        if (!InputValidator.isValidNumber(text)) {
            InputValidator.showValidationError(this, "Idle time", text);
            tfIdleTime.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Gets the bottom-most component for layout reference.
     * @return The idle time text field
     */
    public JTextField getBottomComponent() {
        return tfIdleTime;
    }
}
