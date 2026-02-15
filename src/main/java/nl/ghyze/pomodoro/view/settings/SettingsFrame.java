package nl.ghyze.pomodoro.view.settings;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.persistence.SettingsRepository;

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
        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int midVerticalPoint = (winSize.x / 2) + (winSize.width / 2);
        int midHorizontalPoint = (winSize.y / 2) + (winSize.height / 2);
        this.setLocation(midVerticalPoint - (this.getWidth() / 2), midHorizontalPoint - (this.getHeight() / 2));
    }

    private void initPanels() {
        // Position panel at top
        layout.putConstraint(SpringLayout.WEST, positionPanel, 0, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, positionPanel, 0, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, positionPanel, 0, SpringLayout.NORTH, getContentPane());
        add(positionPanel);

        // Duration panel below position panel
        layout.putConstraint(SpringLayout.WEST, durationPanel, 0, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, durationPanel, 0, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, durationPanel, 5, SpringLayout.SOUTH, positionPanel.getBottomComponent());
        add(durationPanel);

        // Idle panel below duration panel
        layout.putConstraint(SpringLayout.WEST, idlePanel, 0, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, idlePanel, 0, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, idlePanel, 5, SpringLayout.SOUTH, durationPanel.getBottomComponent());
        add(idlePanel);
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
}
