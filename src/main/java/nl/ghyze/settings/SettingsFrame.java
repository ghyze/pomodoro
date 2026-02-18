package nl.ghyze.settings;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

/**
 * Main settings dialog frame that coordinates all settings panels.
 */
public class SettingsFrame extends JFrame {
    private final Settings settings;
    private final SettingsRepository settingsRepository;

    private final SpringLayout layout = new SpringLayout();

    private final PositionSettingsPanel positionPanel;
    private final DurationSettingsPanel durationPanel;
    private final IdleSettingsPanel idlePanel;

    private final JButton btOk = new JButton("OK");
    private final JButton btCancel = new JButton("Cancel");

    public SettingsFrame(final Settings settings, final SettingsRepository settingsRepository) {
        this.settings = settings;
        this.settingsRepository = settingsRepository;

        // Create panels
        this.positionPanel = new PositionSettingsPanel(settings);
        this.durationPanel = new DurationSettingsPanel(settings);
        this.idlePanel = new IdleSettingsPanel(settings);

        initUI();
    }

    private void initUI() {
        this.setPreferredSize(new Dimension(400, 400));
        this.getContentPane().setLayout(layout);

        initPanels();
        initButtons();

        pack();

        // Center the window on screen
        final Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        final int centerX = (winSize.x / 2) + (winSize.width / 2);
        final int centerY = (winSize.y / 2) + (winSize.height / 2);
        this.setLocation(centerX - (this.getWidth() / 2), centerY - (this.getHeight() / 2));
    }

    private void initPanels() {
        addPanelAtTop(positionPanel);
        addPanelBelow(durationPanel, 125);
        addPanelBelow(idlePanel, 225);
    }

    /**
     * Adds a panel at the top of the content pane, stretched horizontally.
     *
     * @param panel the panel to add
     */
    private void addPanelAtTop(final Component panel) {
        layout.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, panel, 0, SpringLayout.NORTH, getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, panel, 150, SpringLayout.NORTH, getContentPane());
        getContentPane().add(panel);
    }

    /**
     * Adds a panel below another component, stretched horizontally.
     *
     * @param panel the panel to add
     * @param gap the vertical gap in pixels
     */
    private void addPanelBelow(final Component panel, final int gap) {
        layout.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, panel, gap, SpringLayout.NORTH, getContentPane());
        getContentPane().add(panel);
    }

    private void initButtons() {
        // Cancel button (bottom-right)
        layout.putConstraint(SpringLayout.WEST, btCancel, -105, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, btCancel, -5, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, btCancel, -25, SpringLayout.SOUTH, getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, btCancel, -5, SpringLayout.SOUTH, getContentPane());
        this.getContentPane().add(btCancel);

        // OK button (to the left of Cancel)
        layout.putConstraint(SpringLayout.WEST, btOk, -105, SpringLayout.WEST, btCancel);
        layout.putConstraint(SpringLayout.EAST, btOk, -5, SpringLayout.WEST, btCancel);
        layout.putConstraint(SpringLayout.NORTH, btOk, -25, SpringLayout.SOUTH, getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, btOk, -5, SpringLayout.SOUTH, getContentPane());
        this.getContentPane().add(btOk);

        // Button actions
        btCancel.addActionListener(actionEvent -> setVisible(false));

        btOk.addActionListener(actionEvent -> {
            if (validateAllFields()) {
                updateSettings();
                setVisible(false);
            }
        });
    }

    /**
     * Validates all fields in all panels.
     * @return true if all fields are valid, false otherwise
     */
    private boolean validateAllFields() {
        return durationPanel.validateAll() && idlePanel.validateAll();
    }

    /**
     * Collects values from all panels and updates the settings object.
     */
    private void updateSettings() {
        // Position settings
        int screenIndex = positionPanel.getSelectedScreenIndex();
        if (screenIndex >= 0) {
            settings.setScreenIndex(screenIndex);
        }
        settings.setPosition(positionPanel.getSelectedPosition());

        // Duration settings
        int pomoMinutes = durationPanel.getPomoMinutes();
        if (pomoMinutes > 0) {
            settings.setPomoMinutes(pomoMinutes);
        }

        int shortBreakMinutes = durationPanel.getShortBreakMinutes();
        if (shortBreakMinutes > 0) {
            settings.setShortBreakMinutes(shortBreakMinutes);
        }

        int longBreakMinutes = durationPanel.getLongBreakMinutes();
        if (longBreakMinutes > 0) {
            settings.setLongBreakMinutes(longBreakMinutes);
        }

        int pomosBeforeLongBreak = durationPanel.getPomosBeforeLongBreak();
        if (pomosBeforeLongBreak > 0) {
            settings.setPomosBeforeLongBreak(pomosBeforeLongBreak);
        }

        // Idle settings
        settings.setAutoreset(idlePanel.isAutoResetEnabled());

        int idleTime = idlePanel.getIdleTime();
        if (idleTime > 0) {
            settings.setIdleTime(idleTime);
        }

        settingsRepository.save(settings);
    }

    public static void main(String[] args) {
        SettingsFrame frame = new SettingsFrame(new Settings(), new SettingsRepository());
        frame.setVisible(true);
    }
}
