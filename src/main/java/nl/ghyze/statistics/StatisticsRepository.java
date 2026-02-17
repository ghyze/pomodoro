package nl.ghyze.statistics;

import nl.ghyze.pomodoro.persistence.PersistenceManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Repository for logging statistics events to daily-rotated CSV files.
 * Files are stored in ~/.pomodoro/logs/ directory.
 * Uses silent failure with stderr logging to avoid interrupting user workflow.
 */
public class StatisticsRepository {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // CSV headers
    private static final String POMODORO_HEADER = "timestamp,event_type,from_state,to_state,pomo_count\n";
    private static final String TASK_HEADER = "timestamp,task_id,event_type,task_name,task_state,pomo_actual,pomo_estimated\n";

    /**
     * Gets the logs directory, creating it if it doesn't exist.
     */
    private Path getLogsDirectory() {
        Path logsDir = PersistenceManager.getDataDirectory().resolve("logs");
        try {
            Files.createDirectories(logsDir);
        } catch (IOException e) {
            System.err.println("Failed to create logs directory: " + e.getMessage());
        }
        return logsDir;
    }

    /**
     * Gets the log file path for today's date.
     * Format: {baseName}_YYYY-MM-DD.csv
     */
    private Path getLogFile(String baseName) {
        String date = LocalDate.now().format(DATE_FORMATTER);
        String filename = baseName + "_" + date + ".csv";
        return getLogsDirectory().resolve(filename);
    }

    /**
     * Ensures the CSV file exists and has a header.
     * If the file doesn't exist, creates it with the header.
     */
    private void ensureFileWithHeader(Path file, String header) {
        try {
            if (!Files.exists(file)) {
                Files.writeString(file, header);
            }
        } catch (IOException e) {
            System.err.println("Failed to create CSV file with header: " + e.getMessage());
        }
    }

    /**
     * Appends a line to a CSV file.
     * Silent failure with stderr logging.
     */
    private void appendToFile(Path file, String line) {
        try {
            Files.writeString(file, line + "\n", StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Failed to append to CSV file " + file + ": " + e.getMessage());
        }
    }

    /**
     * Escapes a string for CSV format.
     * Wraps in quotes if it contains comma, quote, or newline.
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Logs a pomodoro event to pomodoro_events_YYYY-MM-DD.csv.
     */
    public void logPomodoroEvent(PomodoroEvent event) {
        Path file = getLogFile("pomodoro_events");
        ensureFileWithHeader(file, POMODORO_HEADER);

        String line = String.format("%s,%s,%s,%s,%d",
                event.timestamp(),
                event.eventType(),
                event.fromState(),
                event.toState(),
                event.pomoCount()
        );

        appendToFile(file, line);
    }

    /**
     * Logs a task event to task_events_YYYY-MM-DD.csv.
     */
    public void logTaskEvent(TaskEvent event) {
        Path file = getLogFile("task_events");
        ensureFileWithHeader(file, TASK_HEADER);

        String line = String.format("%s,%s,%s,%s,%s,%d,%d",
                event.timestamp(),
                event.taskId(),
                event.eventType(),
                escapeCsv(event.taskName()),
                event.taskState(),
                event.pomoActual(),
                event.pomoEstimated()
        );

        appendToFile(file, line);
    }
}
