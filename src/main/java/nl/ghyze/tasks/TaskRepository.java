package nl.ghyze.tasks;

import com.google.gson.*;
import nl.ghyze.pomodoro.persistence.PersistenceManager;

import javax.swing.*;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository for loading and saving Task lists using JSON format.
 */
public class TaskRepository {

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
        public Task createInstance(java.lang.reflect.Type type) {
            try {
                // Create a Task with default values using reflection
                // The pcs field will be initialized by the constructor
                Constructor<Task> constructor = Task.class.getDeclaredConstructor(UUID.class, String.class, int.class, TaskState.class);
                constructor.setAccessible(true);
                return constructor.newInstance(UUID.randomUUID(), "", 0, TaskState.PENDING);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create Task instance", e);
            }
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

            List<Task> tasks = wrapper.tasks;

            // MIGRATION: Check if any tasks are missing UUIDs and generate them
            // This code can be removed after sufficient time (e.g., 1 year after release)
            boolean needsMigration = tasks.stream().anyMatch(task -> task.getId() == null);
            if (needsMigration) {
                System.out.println("Migrating tasks to add UUIDs...");
                List<Task> migratedTasks = new ArrayList<>();
                for (Task task : tasks) {
                    if (task.getId() == null) {
                        // Create new task with generated UUID
                        Task migratedTask = new Task(UUID.randomUUID(), task.getName(),
                                task.getEstimated(), task.getState());
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
