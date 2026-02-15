package nl.ghyze.pomodoro.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import nl.ghyze.pomodoro.tasks.Task;

import javax.swing.*;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for loading and saving Task lists using JSON format.
 */
public class TaskRepository {

    private final Gson gson;

    public TaskRepository() {
        // Configure Gson to exclude PropertyChangeSupport fields
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .setExclusionStrategies(new com.google.gson.ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(com.google.gson.FieldAttributes field) {
                        // Exclude PropertyChangeSupport fields
                        return field.getDeclaredType() == PropertyChangeSupport.class;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }

    /**
     * Loads all tasks from ~/.pomodoro/tasks.json.
     * If the file doesn't exist or is empty, returns an empty list.
     *
     * @return list of tasks (never null)
     */
    public List<Task> loadAll() {
        Path tasksFile = PersistenceManager.getTasksFile();

        try {
            if (!Files.exists(tasksFile)) {
                return new ArrayList<>();
            }

            String json = Files.readString(tasksFile);
            if (json == null || json.trim().isEmpty()) {
                return new ArrayList<>();
            }

            TaskListWrapper wrapper = gson.fromJson(json, TaskListWrapper.class);
            if (wrapper == null || wrapper.tasks == null) {
                return new ArrayList<>();
            }

            return wrapper.tasks;
        } catch (Exception e) {
            System.err.println("Failed to load tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Saves all tasks to ~/.pomodoro/tasks.json.
     *
     * @param tasks the list of tasks to save
     */
    public void saveAll(List<Task> tasks) {
        try {
            PersistenceManager.ensureDirectoryExists();
            Path tasksFile = PersistenceManager.getTasksFile();

            TaskListWrapper wrapper = new TaskListWrapper();
            wrapper.tasks = tasks;

            String json = gson.toJson(wrapper);
            Files.writeString(tasksFile, json);
        } catch (Exception e) {
            System.err.println("Failed to save tasks: " + e.getMessage());
            e.printStackTrace();
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null,
                            "Failed to save tasks: " + e.getMessage(),
                            "Save Error",
                            JOptionPane.WARNING_MESSAGE));
        }
    }

    /**
     * Wrapper class for JSON structure: {"tasks": [...]}
     */
    private static class TaskListWrapper {
        List<Task> tasks;
    }
}
