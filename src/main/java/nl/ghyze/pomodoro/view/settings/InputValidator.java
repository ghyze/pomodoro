package nl.ghyze.pomodoro.view.settings;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Utility class for validating integer input fields in settings dialogs.
 * Provides real-time visual feedback and validation on commit.
 */
public class InputValidator {
    private static final int MAX_VALUE = 999;
    private static final int MIN_VALUE = 1;

    private static final Color VALID_BACKGROUND = Color.WHITE;
    private static final Color VALID_FOREGROUND = Color.BLACK;
    private static final Color INVALID_BACKGROUND = new Color(255, 200, 200); // Light pink
    private static final Color INVALID_FOREGROUND = Color.RED;

    /**
     * Validates if the text contains a valid integer within range.
     * @param text The text to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidNumber(final String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        // Check if contains only digits
        if (!text.matches("\\d+")) {
            return false;
        }

        try {
            int value = Integer.parseInt(text);
            return value >= MIN_VALUE && value <= MAX_VALUE;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Updates the visual appearance of a text field based on its validity.
     * @param textField The field to update
     * @param isValid Whether the current value is valid
     */
    public static void updateFieldAppearance(final JTextField textField, final boolean isValid) {
        if (isValid) {
            textField.setBackground(VALID_BACKGROUND);
            textField.setForeground(VALID_FOREGROUND);
        } else {
            textField.setBackground(INVALID_BACKGROUND);
            textField.setForeground(INVALID_FOREGROUND);
        }
    }

    /**
     * Shows an error dialog with a specific message for the field.
     * @param parent The parent component for the dialog
     * @param fieldName The name of the field with the error
     * @param text The invalid text that was entered
     */
    public static void showValidationError(final Component parent, final String fieldName, final String text) {
        String message;

        if (text == null || text.trim().isEmpty()) {
            message = fieldName + " cannot be empty.";
        } else if (!text.matches("\\d+")) {
            message = """
                    %s must contain only digits.
                    Invalid input: "%s\"""".formatted(fieldName, text);
        } else {
            try {
                int value = Integer.parseInt(text);
                if (value < MIN_VALUE) {
                    message = """
                            %s must be at least %d.
                            You entered: %d""".formatted(fieldName, MIN_VALUE, value);
                } else {
                    message = """
                            %s must be less than %d.
                            You entered: %d""".formatted(fieldName, MAX_VALUE + 1, value);
                }
            } catch (NumberFormatException e) {
                message = """
                        %s contains an invalid number.
                        Input: "%s\"""".formatted(fieldName, text);
            }
        }

        JOptionPane.showMessageDialog(
                parent,
                message,
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Attaches validation listeners to a text field for real-time feedback
     * and validation on commit (Enter or focus lost).
     * @param textField The field to attach listeners to
     * @param fieldName The human-readable name of the field for error messages
     * @param parent The parent component for error dialogs
     */
    public static void attachValidationListeners(final JTextField textField, final String fieldName, final Component parent) {
        // Real-time validation as user types
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent e) {
                validateField();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                validateField();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                validateField();
            }

            private void validateField() {
                final boolean isValid = isValidNumber(textField.getText());
                updateFieldAppearance(textField, isValid);
            }
        });

        // Validation on focus lost
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent e) {
                final String text = textField.getText();
                if (!isValidNumber(text)) {
                    showValidationError(parent, fieldName, text);
                }
            }
        });

        // Validation on Enter key
        textField.addActionListener(e -> {
            final String text = textField.getText();
            if (!isValidNumber(text)) {
                showValidationError(parent, fieldName, text);
            }
        });
    }
}
