package nl.ghyze.tasks;

import lombok.Setter;
import nl.ghyze.statistics.TaskStatisticsHook;

import java.util.Collections;
import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskHook taskHook;
    private final List<Task> tasks;

    private TaskStatisticsHook statisticsHook;

    @Setter
    private Runnable onChanged;

    private boolean activating = false;

    public TaskService(final TaskRepository taskRepository, final TaskHook taskHook) {
        this.taskRepository = taskRepository;
        this.taskHook = taskHook;
        this.tasks = taskRepository.loadAll();
        taskHook.setTaskService(this);
        tasks.forEach(this::registerStateListener);
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void addTask(final Task task) {
        tasks.add(task);
        registerStateListener(task);
        if (statisticsHook != null) {
            statisticsHook.registerTask(task);
            statisticsHook.logCreated(task);
        }
        save();
        notifyChanged();
    }

    public void activateTask(final Task task) {
        activating = true;
        try {
            tasks.stream()
                    .filter(Task::isActive)
                    .forEach(t -> {
                        if (statisticsHook != null) {
                            statisticsHook.logDeactivated(t);
                        }
                        if (t.getState() == TaskState.IN_PROGRESS) {
                            t.setState(TaskState.PENDING);
                        }
                        t.setActive(false);
                    });

            task.setActive(true);
            taskHook.setCurrentTask(task);

            if (task.getState() == TaskState.PENDING) {
                task.setState(TaskState.IN_PROGRESS);
            }

            if (statisticsHook != null) {
                statisticsHook.logActivated(task);
            }

            save();
        } finally {
            activating = false;
        }
        notifyChanged();
    }

    public void removeTask(final Task task) {
        if (statisticsHook != null) {
            statisticsHook.logRemoved(task);
        }
        tasks.remove(task);
        save();
        notifyChanged();
    }

    public void removeAllDoneTasks() {
        List<Task> doneTasks = tasks.stream()
                .filter(t -> t.getState() == TaskState.DONE)
                .toList();
        if (doneTasks.isEmpty()) {
            return;
        }
        doneTasks.forEach(t -> {
            if (statisticsHook != null) {
                statisticsHook.logRemoved(t);
            }
        });
        tasks.removeAll(doneTasks);
        save();
        notifyChanged();
    }

    public void editTask(final Task task) {
        if (statisticsHook != null) {
            statisticsHook.logEdited(task);
        }
        save();
    }

    public void save() {
        taskRepository.saveAll(tasks);
    }

    public void setStatisticsHook(final TaskStatisticsHook hook) {
        this.statisticsHook = hook;
        tasks.forEach(hook::registerTask);
    }

    private void registerStateListener(final Task task) {
        task.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())) {
                if (evt.getNewValue() == TaskState.DONE && task.isActive()) {
                    task.setActive(false);
                    taskHook.setCurrentTask(null);
                }
                save();
                if (!activating) {
                    notifyChanged();
                }
            }
        });
    }

    private void notifyChanged() {
        if (onChanged != null) {
            onChanged.run();
        }
    }
}
