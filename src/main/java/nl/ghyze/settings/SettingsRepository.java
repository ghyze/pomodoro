package nl.ghyze.settings;

import nl.ghyze.pomodoro.persistence.PersistenceManager;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 * Repository for loading and saving Settings using .properties file format.
 * Automatically migrates from Java Preferences on first run.
 */
public class SettingsRepository {

    // Property keys matching Settings.KEY_* constants
    private static final String KEY_POSITION = "position";
    private static final String KEY_POMO_MINUTES = "pomoMinutes";
    private static final String KEY_SHORT_BREAK_MINUTES = "shortBreakNMinutes";
    private static final String KEY_LONG_BREAK_MINUTES = "longBreakMinutes";
    private static final String KEY_POMOS_BEFORE_LONG_BREAK = "pomosBeforeLongBreak";
    private static final String KEY_AUTORESET = "autoReset";
    private static final String KEY_IDLE_TIME = "idleTime";
    private static final String KEY_SCREEN_INDEX = "screenIndex";

    /**
     * Loads settings from ~/.pomodoro/settings.properties.
     * If the file doesn't exist, migrates from Java Preferences.
     * On error, returns default settings.
     *
     * @return loaded Settings object
     */
    public Settings load() {
        Path settingsFile = PersistenceManager.getSettingsFile();

        try {
            if (Files.exists(settingsFile)) {
                return loadFromPropertiesFile(settingsFile);
            } else {
                // First run - migrate from Preferences
                Settings settings = migrateFromPreferences();
                // Save migrated settings to new location
                save(settings);
                return settings;
            }
        } catch (Exception e) {
            System.err.println("Failed to load settings: " + e.getMessage());
            return createDefaultSettings();
        }
    }

    /**
     * Saves settings to ~/.pomodoro/settings.properties.
     *
     * @param settings the settings to save
     */
    public void save(Settings settings) {
        try {
            PersistenceManager.ensureDirectoryExists();
            Path settingsFile = PersistenceManager.getSettingsFile();
            Properties props = toProperties(settings);

            try (OutputStream out = Files.newOutputStream(settingsFile)) {
                props.store(out, "Pomodoro Application Settings");
            }
        } catch (Exception e) {
            System.err.println("Failed to save settings: " + e.getMessage());
            e.printStackTrace();
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null,
                            "Failed to save settings: " + e.getMessage(),
                            "Save Error",
                            JOptionPane.WARNING_MESSAGE));
        }
    }

    /**
     * Migrates settings from Java Preferences API to Settings object.
     *
     * @return Settings object with values from Preferences or defaults
     */
    private Settings migrateFromPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(Settings.class);

        int pomoMinutes = prefs.getInt(KEY_POMO_MINUTES, 25);
        int shortBreakMinutes = prefs.getInt(KEY_SHORT_BREAK_MINUTES, 5);
        int longBreakMinutes = prefs.getInt(KEY_LONG_BREAK_MINUTES, 15);
        int pomosBeforeLongBreak = prefs.getInt(KEY_POMOS_BEFORE_LONG_BREAK, 3);
        int screenIndex = prefs.getInt(KEY_SCREEN_INDEX, 0);
        Settings.Position position = Settings.Position.values()[prefs.getInt(KEY_POSITION, 3)];
        boolean autoreset = prefs.getBoolean(KEY_AUTORESET, false);
        int idleTime = prefs.getInt(KEY_IDLE_TIME, 60);

        return Settings.builder()
                .pomoMinutes(pomoMinutes)
                .shortBreakMinutes(shortBreakMinutes)
                .longBreakMinutes(longBreakMinutes)
                .pomosBeforeLongBreak(pomosBeforeLongBreak)
                .screenIndex(screenIndex)
                .position(position)
                .autoreset(autoreset)
                .idleTime(idleTime)
                .build();
    }

    /**
     * Loads settings from a .properties file.
     *
     * @param file the properties file
     * @return Settings object
     * @throws IOException if reading fails
     */
    private Settings loadFromPropertiesFile(Path file) throws IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(file)) {
            props.load(in);
        }
        return loadFromProperties(props);
    }

    /**
     * Converts Properties to Settings object.
     *
     * @param props the properties
     * @return Settings object
     */
    private Settings loadFromProperties(Properties props) {
        int pomoMinutes = Integer.parseInt(props.getProperty(KEY_POMO_MINUTES, "25"));
        int shortBreakMinutes = Integer.parseInt(props.getProperty(KEY_SHORT_BREAK_MINUTES, "5"));
        int longBreakMinutes = Integer.parseInt(props.getProperty(KEY_LONG_BREAK_MINUTES, "15"));
        int pomosBeforeLongBreak = Integer.parseInt(props.getProperty(KEY_POMOS_BEFORE_LONG_BREAK, "3"));
        int screenIndex = Integer.parseInt(props.getProperty(KEY_SCREEN_INDEX, "0"));
        Settings.Position position = Settings.Position.valueOf(
                props.getProperty(KEY_POSITION, Settings.Position.BOTTOM_RIGHT.name()));
        boolean autoreset = Boolean.parseBoolean(props.getProperty(KEY_AUTORESET, "false"));
        int idleTime = Integer.parseInt(props.getProperty(KEY_IDLE_TIME, "60"));

        return Settings.builder()
                .pomoMinutes(pomoMinutes)
                .shortBreakMinutes(shortBreakMinutes)
                .longBreakMinutes(longBreakMinutes)
                .pomosBeforeLongBreak(pomosBeforeLongBreak)
                .screenIndex(screenIndex)
                .position(position)
                .autoreset(autoreset)
                .idleTime(idleTime)
                .build();
    }

    /**
     * Converts Settings object to Properties.
     *
     * @param settings the settings object
     * @return Properties
     */
    private Properties toProperties(Settings settings) {
        Properties props = new Properties();
        props.setProperty(KEY_POMO_MINUTES, String.valueOf(settings.getPomoMinutes()));
        props.setProperty(KEY_SHORT_BREAK_MINUTES, String.valueOf(settings.getShortBreakMinutes()));
        props.setProperty(KEY_LONG_BREAK_MINUTES, String.valueOf(settings.getLongBreakMinutes()));
        props.setProperty(KEY_POMOS_BEFORE_LONG_BREAK, String.valueOf(settings.getPomosBeforeLongBreak()));
        props.setProperty(KEY_SCREEN_INDEX, String.valueOf(settings.getScreenIndex()));
        props.setProperty(KEY_POSITION, settings.getPosition().name());
        props.setProperty(KEY_AUTORESET, String.valueOf(settings.isAutoreset()));
        props.setProperty(KEY_IDLE_TIME, String.valueOf(settings.getIdleTime()));
        return props;
    }

    /**
     * Creates Settings object with default values.
     *
     * @return Settings with defaults
     */
    private Settings createDefaultSettings() {
        return Settings.builder()
                .pomoMinutes(25)
                .shortBreakMinutes(5)
                .longBreakMinutes(15)
                .pomosBeforeLongBreak(3)
                .screenIndex(0)
                .position(Settings.Position.BOTTOM_RIGHT)
                .autoreset(false)
                .idleTime(60)
                .build();
    }
}
