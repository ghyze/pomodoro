package nl.ghyze.settings;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpringLayout;

import nl.ghyze.pomodoro.MultiScreenFactory;
import nl.ghyze.settings.Settings.Position;
import nl.ghyze.pomodoro.view.Screen;

/**
 * Panel for configuring window position settings (screen and corner).
 */
public class PositionSettingsPanel extends JPanel {
    private static final int LABEL_WIDTH = 200;

    private final SpringLayout layout = new SpringLayout();
    private final MultiScreenFactory multiScreenFactory = new MultiScreenFactory();

    private final JLabel lbPosition = new JLabel("Position");
    private final JComboBox<Screen> jcbScreen = new JComboBox<>();
    private final ButtonGroup bgPosition = new ButtonGroup();
    private final JRadioButton rbTopLeft = new JRadioButton("Top left");
    private final JRadioButton rbTopRight = new JRadioButton("Top right");
    private final JRadioButton rbBottomLeft = new JRadioButton("Bottom left");
    private final JRadioButton rbBottomRight = new JRadioButton("Bottom right");

    public PositionSettingsPanel(final Settings settings) {
        setLayout(layout);
        setPreferredSize(new java.awt.Dimension(400, 150)); // Ensure panel is visible
        initComponents(settings);
    }

    private void initComponents(final Settings settings) {
        // Populate screen dropdown
        multiScreenFactory.getAvailableScreenList()
                .forEach(jcbScreen::addItem);

        // Group radio buttons
        bgPosition.add(rbTopLeft);
        bgPosition.add(rbTopRight);
        bgPosition.add(rbBottomLeft);
        bgPosition.add(rbBottomRight);

        // Position label
        layout.putConstraint(SpringLayout.WEST, lbPosition, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, lbPosition, LABEL_WIDTH, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, lbPosition, 5, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, lbPosition, 25, SpringLayout.NORTH, this);
        add(lbPosition);

        // Screen dropdown
        layout.putConstraint(SpringLayout.WEST, jcbScreen, 5, SpringLayout.EAST, lbPosition);
        layout.putConstraint(SpringLayout.EAST, jcbScreen, -5, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, jcbScreen, 5, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, jcbScreen, 25, SpringLayout.NORTH, this);
        add(jcbScreen);
        jcbScreen.setEnabled(jcbScreen.getItemCount() > 1);
        if (settings.getScreenIndex() < jcbScreen.getItemCount()) {
            jcbScreen.setSelectedIndex(settings.getScreenIndex());
        }

        // Top Left radio button
        layout.putConstraint(SpringLayout.WEST, rbTopLeft, 5, SpringLayout.EAST, lbPosition);
        layout.putConstraint(SpringLayout.EAST, rbTopLeft, LABEL_WIDTH, SpringLayout.EAST, lbPosition);
        layout.putConstraint(SpringLayout.NORTH, rbTopLeft, 5, SpringLayout.SOUTH, jcbScreen);
        layout.putConstraint(SpringLayout.SOUTH, rbTopLeft, 25, SpringLayout.SOUTH, jcbScreen);
        add(rbTopLeft);
        rbTopLeft.setSelected(settings.getPosition() == Position.TOP_LEFT);

        // Top Right radio button
        layout.putConstraint(SpringLayout.WEST, rbTopRight, 5, SpringLayout.EAST, lbPosition);
        layout.putConstraint(SpringLayout.EAST, rbTopRight, LABEL_WIDTH, SpringLayout.EAST, lbPosition);
        layout.putConstraint(SpringLayout.NORTH, rbTopRight, 5, SpringLayout.SOUTH, rbTopLeft);
        layout.putConstraint(SpringLayout.SOUTH, rbTopRight, 25, SpringLayout.SOUTH, rbTopLeft);
        add(rbTopRight);
        rbTopRight.setSelected(settings.getPosition() == Position.TOP_RIGHT);

        // Bottom Left radio button
        layout.putConstraint(SpringLayout.WEST, rbBottomLeft, 5, SpringLayout.EAST, lbPosition);
        layout.putConstraint(SpringLayout.EAST, rbBottomLeft, LABEL_WIDTH, SpringLayout.EAST, lbPosition);
        layout.putConstraint(SpringLayout.NORTH, rbBottomLeft, 5, SpringLayout.SOUTH, rbTopRight);
        layout.putConstraint(SpringLayout.SOUTH, rbBottomLeft, 25, SpringLayout.SOUTH, rbTopRight);
        add(rbBottomLeft);
        rbBottomLeft.setSelected(settings.getPosition() == Position.BOTTOM_LEFT);

        // Bottom Right radio button
        layout.putConstraint(SpringLayout.WEST, rbBottomRight, 5, SpringLayout.EAST, lbPosition);
        layout.putConstraint(SpringLayout.EAST, rbBottomRight, LABEL_WIDTH, SpringLayout.EAST, lbPosition);
        layout.putConstraint(SpringLayout.NORTH, rbBottomRight, 5, SpringLayout.SOUTH, rbBottomLeft);
        layout.putConstraint(SpringLayout.SOUTH, rbBottomRight, 25, SpringLayout.SOUTH, rbBottomLeft);
        add(rbBottomRight);
        rbBottomRight.setSelected(settings.getPosition() == Position.BOTTOM_RIGHT);
    }

    /**
     * Gets the selected screen index.
     * @return The index of the selected screen, or -1 if none selected
     */
    public int getSelectedScreenIndex() {
        Screen screen = (Screen) jcbScreen.getSelectedItem();
        return screen != null ? screen.index() : -1;
    }

    /**
     * Gets the selected position.
     * @return The selected corner position
     */
    public Position getSelectedPosition() {
        if (rbTopLeft.isSelected()) {
            return Position.TOP_LEFT;
        } else if (rbTopRight.isSelected()) {
            return Position.TOP_RIGHT;
        } else if (rbBottomLeft.isSelected()) {
            return Position.BOTTOM_LEFT;
        }
        return Position.BOTTOM_RIGHT;
    }

    /**
     * Gets the bottom-most component for layout reference.
     * @return The bottom radio button
     */
    public JRadioButton getBottomComponent() {
        return rbBottomRight;
    }
}
