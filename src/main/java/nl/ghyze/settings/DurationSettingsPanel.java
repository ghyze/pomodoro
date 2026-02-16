package nl.ghyze.settings;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * Panel for configuring duration settings (pomodoro time, break times, etc.).
 */
public class DurationSettingsPanel extends JPanel {
    private static final int LABEL_WIDTH = 200;

    private final SpringLayout layout = new SpringLayout();

    private final JLabel lbPomoTime = new JLabel("Time of pomodoros");
    private final JLabel lbShortBreakTime = new JLabel("Time of short break");
    private final JLabel lbLongBreakTime = new JLabel("Time of long break");
    private final JLabel lbNrOfPomos = new JLabel("Pomodoros between long breaks");

    private final JTextField tfPomoTime = new JTextField();
    private final JTextField tfShortBreakTime = new JTextField();
    private final JTextField tfLongBreakTime = new JTextField();
    private final JTextField tfNrOfPomos = new JTextField();

    public DurationSettingsPanel(final Settings settings) {
        setLayout(layout);
        setPreferredSize(new java.awt.Dimension(400, 100)); // Ensure panel is visible
        initComponents(settings);
    }

    private void initComponents(final Settings settings) {
        // Pomodoro time label
        layout.putConstraint(SpringLayout.WEST, lbPomoTime, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, lbPomoTime, LABEL_WIDTH, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, lbPomoTime, 5, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, lbPomoTime, 25, SpringLayout.NORTH, this);
        add(lbPomoTime);

        // Short break time label
        layout.putConstraint(SpringLayout.WEST, lbShortBreakTime, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, lbShortBreakTime, LABEL_WIDTH, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, lbShortBreakTime, 5, SpringLayout.SOUTH, lbPomoTime);
        layout.putConstraint(SpringLayout.SOUTH, lbShortBreakTime, 25, SpringLayout.SOUTH, lbPomoTime);
        add(lbShortBreakTime);

        // Long break time label
        layout.putConstraint(SpringLayout.WEST, lbLongBreakTime, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, lbLongBreakTime, LABEL_WIDTH, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, lbLongBreakTime, 5, SpringLayout.SOUTH, lbShortBreakTime);
        layout.putConstraint(SpringLayout.SOUTH, lbLongBreakTime, 25, SpringLayout.SOUTH, lbShortBreakTime);
        add(lbLongBreakTime);

        // Number of pomos label
        layout.putConstraint(SpringLayout.WEST, lbNrOfPomos, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, lbNrOfPomos, LABEL_WIDTH, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, lbNrOfPomos, 5, SpringLayout.SOUTH, lbLongBreakTime);
        layout.putConstraint(SpringLayout.SOUTH, lbNrOfPomos, 25, SpringLayout.SOUTH, lbLongBreakTime);
        add(lbNrOfPomos);

        // Pomodoro time field
        layout.putConstraint(SpringLayout.WEST, tfPomoTime, 5 + LABEL_WIDTH, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, tfPomoTime, -5, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, tfPomoTime, 5, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, tfPomoTime, 25, SpringLayout.NORTH, this);
        add(tfPomoTime);
        tfPomoTime.setText("" + settings.getPomoMinutes());
        InputValidator.attachValidationListeners(tfPomoTime, "Pomodoro time", this);

        // Short break time field
        layout.putConstraint(SpringLayout.WEST, tfShortBreakTime, 5 + LABEL_WIDTH, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, tfShortBreakTime, -5, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, tfShortBreakTime, 5, SpringLayout.SOUTH, lbPomoTime);
        layout.putConstraint(SpringLayout.SOUTH, tfShortBreakTime, 25, SpringLayout.SOUTH, lbPomoTime);
        add(tfShortBreakTime);
        tfShortBreakTime.setText("" + settings.getShortBreakMinutes());
        InputValidator.attachValidationListeners(tfShortBreakTime, "Short break time", this);

        // Long break time field
        layout.putConstraint(SpringLayout.WEST, tfLongBreakTime, 5 + LABEL_WIDTH, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, tfLongBreakTime, -5, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, tfLongBreakTime, 5, SpringLayout.SOUTH, lbShortBreakTime);
        layout.putConstraint(SpringLayout.SOUTH, tfLongBreakTime, 25, SpringLayout.SOUTH, lbShortBreakTime);
        add(tfLongBreakTime);
        tfLongBreakTime.setText("" + settings.getLongBreakMinutes());
        InputValidator.attachValidationListeners(tfLongBreakTime, "Long break time", this);

        // Number of pomos field
        layout.putConstraint(SpringLayout.WEST, tfNrOfPomos, 5 + LABEL_WIDTH, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, tfNrOfPomos, -5, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, tfNrOfPomos, 5, SpringLayout.SOUTH, lbLongBreakTime);
        layout.putConstraint(SpringLayout.SOUTH, tfNrOfPomos, 25, SpringLayout.SOUTH, lbLongBreakTime);
        add(tfNrOfPomos);
        tfNrOfPomos.setText("" + settings.getPomosBeforeLongBreak());
        InputValidator.attachValidationListeners(tfNrOfPomos, "Pomodoros before long break", this);
    }

    /**
     * Gets the pomodoro time in minutes.
     * @return The pomodoro duration, or -1 if invalid
     */
    public int getPomoMinutes() {
        return parseNumber(tfPomoTime.getText());
    }

    /**
     * Gets the short break time in minutes.
     * @return The short break duration, or -1 if invalid
     */
    public int getShortBreakMinutes() {
        return parseNumber(tfShortBreakTime.getText());
    }

    /**
     * Gets the long break time in minutes.
     * @return The long break duration, or -1 if invalid
     */
    public int getLongBreakMinutes() {
        return parseNumber(tfLongBreakTime.getText());
    }

    /**
     * Gets the number of pomodoros before a long break.
     * @return The number of pomodoros, or -1 if invalid
     */
    public int getPomosBeforeLongBreak() {
        return parseNumber(tfNrOfPomos.getText());
    }

    /**
     * Validates all fields in this panel.
     * @return true if all fields are valid, false otherwise
     */
    public boolean validateAll() {
        final JTextField[] fields = {tfPomoTime, tfShortBreakTime, tfLongBreakTime, tfNrOfPomos};
        final String[] fieldNames = {
                "Pomodoro time",
                "Short break time",
                "Long break time",
                "Pomodoros before long break"
        };

        for (int i = 0; i < fields.length; i++) {
            final String text = fields[i].getText();
            if (!InputValidator.isValidNumber(text)) {
                InputValidator.showValidationError(this, fieldNames[i], text);
                fields[i].requestFocus();
                return false;
            }
        }

        return true;
    }

    /**
     * Gets the bottom-most component for layout reference.
     * @return The bottom text field
     */
    public JTextField getBottomComponent() {
        return tfNrOfPomos;
    }

    private int parseNumber(final String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
