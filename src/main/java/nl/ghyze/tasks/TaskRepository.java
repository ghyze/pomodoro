package nl.ghyze.tasks;

import com.google.gson.*;
import lombok.NoArgsConstructor;
import nl.ghyze.pomodoro.persistence.PersistenceManager;

import javax.swing.*;
import java.beans.PropertyChangeSupport;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Repository for loading and saving Task lists using JSON format.
 */
public class TaskRepository {

    private static final Logger logger = Logger.getLogger(TaskRepository.class.getName());

    private final Gson gson;

    public TaskRepository() {
        // Configure Gson with custom Task deserializer to handle PropertyChangeSupport
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes field) {
                        // Exclude PropertyChangeSupport fields from serialization
                        return field.getDeclaredType() == PropertyChangeSupport.class;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(Task.class, new TaskInstanceCreator())
                .create();
    }

    /**
     * Custom instance creator for Task that properly initializes via constructor.
     * This ensures the PropertyChangeSupport field is initialized even though
     * it's excluded from JSON serialization.
     */
    private static class TaskInstanceCreator implements InstanceCreator<Task> {
        @Override
        public Task createInstance(final java.lang.reflect.Type type) {
            return Task.builder().build();
        }
    }

    /**
     * Loads all tasks from ~/.pomodoro/tasks.json.
     * If the file doesn't exist or is empty, returns an empty list.
     * Performs automatic UUID migration for tasks without IDs.
     *
     * @return list of tasks (never null)
     */
    public List<Task> loadAll() {
        final Path tasksFile = PersistenceManager.getTasksFile();

        try {
            if (!Files.exists(tasksFile)) {
                return new ArrayList<>();
            }

            final String json = Files.readString(tasksFile);
            if (json == null || json.trim().isEmpty()) {
                return new ArrayList<>();
            }

            final TaskListWrapper wrapper = gson.fromJson(json, TaskListWrapper.class);
            if (wrapper == null || wrapper.tasks == null) {
                return new ArrayList<>();
            }

            final List<Task> tasks = wrapper.tasks;

            // MIGRATION: Check if any tasks are missing UUIDs and generate them
            // This code can be removed after sufficient time (e.g., 1 year after release)
            final boolean needsMigration = tasks.stream().anyMatch(task -> task.getId() == null);
            if (needsMigration) {
                logger.info("Migrating tasks to add UUIDs...");
                final List<Task> migratedTasks = new ArrayList<>();
                for (final Task task : tasks) {
                    if (task.getId() == null) {
                        // Create new task with generated UUID
                        final Task migratedTask = Task.builder()
                                .name(task.getName())
                                .estimated(task.getEstimated())
                                .state(task.getState())
                                .build();
                        migratedTask.setActual(task.getActual());
                        migratedTask.setActive(task.isActive());
                        migratedTasks.add(migratedTask);
                    } else {
                        migratedTasks.add(task);
                    }
                }
                // Save migrated tasks
                saveAll(migratedTasks);
                return migratedTasks;
            }

            return tasks;
        } catch (final Exception e) {
            logger.warning("Failed to load tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Saves all tasks to ~/.pomodoro/tasks.json.
     *
     * @param tasks the list of tasks to save
     */
    public void saveAll(final List<Task> tasks) {
        try {
            PersistenceManager.ensureDirectoryExists();
            final Path tasksFile = PersistenceManager.getTasksFile();

            final TaskListWrapper wrapper = new TaskListWrapper();
            wrapper.tasks = tasks;

            final String json = gson.toJson(wrapper);
            Files.writeString(tasksFile, json);
        } catch (final Exception e) {
            logger.severe("Failed to save tasks: " + e.getMessage());
            logger.throwing(TaskRepository.class.getName(), "saveAll", e);
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
    @NoArgsConstructor
    private static class TaskListWrapper {
        List<Task> tasks;
    }
}
