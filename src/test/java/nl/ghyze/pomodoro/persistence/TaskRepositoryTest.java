package nl.ghyze.pomodoro.persistence;

import nl.ghyze.tasks.Task;
import nl.ghyze.tasks.TaskRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TaskRepositoryTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private TaskRepository repository;
    private Path testDataDir;

    @Before
    public void setUp() throws IOException {
        testDataDir = tempFolder.newFolder("test-pomodoro").toPath();
        PersistenceManager.setDataDirectoryForTesting(testDataDir);
        repository = new TaskRepository();
    }

    @After
    public void tearDown() {
        PersistenceManager.setDataDirectoryForTesting(null);
    }

    @Test
    public void testLoadFromNonExistentFileReturnsEmptyList() {
        List<Task> tasks = repository.loadAll();

        assertNotNull("Should return non-null list", tasks);
        assertTrue("Should return empty list", tasks.isEmpty());
    }

    @Test
    public void testSaveCreatesJsonFile() throws Exception {
        List<Task> tasks = new ArrayList<>();
        tasks.add(createTask("Task 1", 5));
        tasks.add(createTask("Task 2", 3));

        repository.saveAll(tasks);

        Path tasksFile = PersistenceManager.getTasksFile();
        assertTrue("Tasks file should exist", Files.exists(tasksFile));

        String json = Files.readString(tasksFile);
        assertTrue("JSON should contain 'tasks' key", json.contains("\"tasks\""));
        assertTrue("JSON should contain task name", json.contains("Task 1"));
        assertTrue("JSON should contain task name", json.contains("Task 2"));
    }

    @Test
    public void testRoundTripPreservesTaskFields() throws Exception {
        List<Task> original = new ArrayList<>();
        Task task1 = createTask("Write code", 8);
        task1.setActive(true);
        // Simulate completed pomodoros using reflection
        addCompletedPomoViaReflection(task1);
        addCompletedPomoViaReflection(task1);

        Task task2 = createTask("Review PR", 3);
        task2.setActive(false);

        original.add(task1);
        original.add(task2);

        repository.saveAll(original);
        List<Task> loaded = repository.loadAll();

        assertEquals("Should have same number of tasks", 2, loaded.size());

        Task loadedTask1 = loaded.get(0);
        assertEquals("Name should match", "Write code", loadedTask1.getName());
        assertEquals("Estimated should match", 8, loadedTask1.getEstimated());
        assertEquals("Actual should match", 2, loadedTask1.getActual());
        assertTrue("Active should match", loadedTask1.isActive());

        Task loadedTask2 = loaded.get(1);
        assertEquals("Name should match", "Review PR", loadedTask2.getName());
        assertEquals("Estimated should match", 3, loadedTask2.getEstimated());
        assertEquals("Actual should match", 0, loadedTask2.getActual());
        assertFalse("Active should match", loadedTask2.isActive());
    }

    @Test
    public void testSaveEmptyList() throws IOException {
        List<Task> emptyList = new ArrayList<>();

        repository.saveAll(emptyList);

        Path tasksFile = PersistenceManager.getTasksFile();
        assertTrue("Tasks file should exist", Files.exists(tasksFile));

        String json = Files.readString(tasksFile);
        assertTrue("JSON should contain 'tasks' key", json.contains("\"tasks\""));
        assertTrue("JSON should contain empty array", json.contains("[]"));

        List<Task> loaded = repository.loadAll();
        assertTrue("Loaded list should be empty", loaded.isEmpty());
    }

    @Test
    public void testPropertyChangeSupportExcludedFromJson() throws Exception {
        List<Task> tasks = new ArrayList<>();
        tasks.add(createTask("Test task", 5));

        repository.saveAll(tasks);

        Path tasksFile = PersistenceManager.getTasksFile();
        String json = Files.readString(tasksFile);

        assertFalse("JSON should not contain PropertyChangeSupport", json.contains("PropertyChangeSupport"));
        assertFalse("JSON should not contain 'pcs' field", json.contains("\"pcs\""));
    }

    @Test
    public void testLoadFromCorruptedFileReturnsEmptyList() throws IOException {
        PersistenceManager.ensureDirectoryExists();
        Path tasksFile = PersistenceManager.getTasksFile();
        Files.writeString(tasksFile, "this is not valid JSON @#$%");

        List<Task> tasks = repository.loadAll();

        assertNotNull("Should return non-null list", tasks);
        assertTrue("Should return empty list on error", tasks.isEmpty());
    }

    @Test
    public void testLoadFromEmptyFileReturnsEmptyList() throws IOException {
        PersistenceManager.ensureDirectoryExists();
        Path tasksFile = PersistenceManager.getTasksFile();
        Files.writeString(tasksFile, "");

        List<Task> tasks = repository.loadAll();

        assertNotNull("Should return non-null list", tasks);
        assertTrue("Should return empty list for empty file", tasks.isEmpty());
    }

    @Test
    public void testJsonStructureHasCorrectFormat() throws Exception {
        List<Task> tasks = new ArrayList<>();
        tasks.add(createTask("Test", 1));

        repository.saveAll(tasks);

        Path tasksFile = PersistenceManager.getTasksFile();
        String json = Files.readString(tasksFile);

        // Verify JSON structure starts with { and contains "tasks" array
        assertTrue("JSON should start with {", json.trim().startsWith("{"));
        assertTrue("JSON should end with }", json.trim().endsWith("}"));
        assertTrue("JSON should have 'tasks' property", json.contains("\"tasks\""));
        assertTrue("JSON should have array", json.contains("["));
    }

    @Test
    public void testMultipleSavesOverwritePreviousData() throws Exception {
        List<Task> firstBatch = new ArrayList<>();
        firstBatch.add(createTask("Task A", 1));
        firstBatch.add(createTask("Task B", 2));

        repository.saveAll(firstBatch);

        List<Task> secondBatch = new ArrayList<>();
        secondBatch.add(createTask("Task C", 3));

        repository.saveAll(secondBatch);

        List<Task> loaded = repository.loadAll();

        assertEquals("Should only have tasks from second save", 1, loaded.size());
        assertEquals("Should be Task C", "Task C", loaded.get(0).getName());
    }

    @Test
    public void testFileCreatedInCorrectLocation() {
        repository.saveAll(new ArrayList<>());

        Path expectedFile = testDataDir.resolve("tasks.json");
        assertTrue("Tasks file should be in test directory", Files.exists(expectedFile));
    }

    /**
     * Helper method to create a Task instance using reflection
     * since the Task constructor is package-private.
     */
    private Task createTask(String name, int estimated) {
        return Task.builder()
                .name(name)
                .estimated(estimated)
                .build();
    }

    /**
     * Helper method to call addCompletedPomo() using reflection
     * since it is package-private.
     */
    private void addCompletedPomoViaReflection(Task task) throws Exception {
        Method method = Task.class.getDeclaredMethod("addCompletedPomo");
        method.setAccessible(true);
        method.invoke(task);
    }
}
