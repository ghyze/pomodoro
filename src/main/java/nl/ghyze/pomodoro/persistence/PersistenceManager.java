package nl.ghyze.pomodoro.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Manages the data directory and file paths for application persistence.
 * All application data is stored in ~/.pomodoro/ directory.
 */
public class PersistenceManager {

    private static final String DATA_DIR_NAME = ".pomodoro";
    private static final String SETTINGS_FILE_NAME = "settings.properties";
    private static final String TASKS_FILE_NAME = "tasks.json";

    // For testing purposes - allows overriding the data directory
    private static Path testDataDirectory = null;

    /**
     * Gets the data directory path (~/.pomodoro).
     * Returns test directory if set via setDataDirectoryForTesting().
     *
     * @return Path to the data directory
     */
    public static Path getDataDirectory() {
        if (testDataDirectory != null) {
            return testDataDirectory;
        }
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, DATA_DIR_NAME);
    }

    /**
     * Ensures the data directory exists, creating it if necessary.
     *
     * @throws IOException if directory creation fails
     */
    public static void ensureDirectoryExists() throws IOException {
        Path dataDir = getDataDirectory();
        if (!Files.exists(dataDir)) {
            Files.createDirectories(dataDir);
        }
    }

    /**
     * Gets the settings file path (~/.pomodoro/settings.properties).
     *
     * @return Path to the settings file
     */
    public static Path getSettingsFile() {
        return getDataDirectory().resolve(SETTINGS_FILE_NAME);
    }

    /**
     * Gets the tasks file path (~/.pomodoro/tasks.json).
     *
     * @return Path to the tasks file
     */
    public static Path getTasksFile() {
        return getDataDirectory().resolve(TASKS_FILE_NAME);
    }

    /**
     * Sets a custom data directory for testing purposes.
     * Pass null to reset to default behavior.
     *
     * @param path the test directory path, or null to reset
     */
    public static void setDataDirectoryForTesting(Path path) {
        testDataDirectory = path;
    }
}
