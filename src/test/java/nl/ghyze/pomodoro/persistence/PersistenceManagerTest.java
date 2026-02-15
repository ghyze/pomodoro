package nl.ghyze.pomodoro.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class PersistenceManagerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() {
        // Reset test directory override before each test
        PersistenceManager.setDataDirectoryForTesting(null);
    }

    @After
    public void tearDown() {
        // Clean up test directory override after each test
        PersistenceManager.setDataDirectoryForTesting(null);
    }

    @Test
    public void testGetDataDirectoryReturnsDefaultPath() {
        Path dataDir = PersistenceManager.getDataDirectory();
        String userHome = System.getProperty("user.home");
        Path expected = Paths.get(userHome, ".pomodoro");
        assertEquals(expected, dataDir);
    }

    @Test
    public void testGetDataDirectoryReturnsTestPathWhenSet() {
        Path testPath = tempFolder.getRoot().toPath();
        PersistenceManager.setDataDirectoryForTesting(testPath);

        Path dataDir = PersistenceManager.getDataDirectory();
        assertEquals(testPath, dataDir);
    }

    @Test
    public void testSetDataDirectoryForTestingCanBeReset() {
        Path testPath = tempFolder.getRoot().toPath();
        PersistenceManager.setDataDirectoryForTesting(testPath);
        PersistenceManager.setDataDirectoryForTesting(null);

        Path dataDir = PersistenceManager.getDataDirectory();
        String userHome = System.getProperty("user.home");
        Path expected = Paths.get(userHome, ".pomodoro");
        assertEquals(expected, dataDir);
    }

    @Test
    public void testEnsureDirectoryExistsCreatesDirectory() throws IOException {
        Path testPath = tempFolder.getRoot().toPath().resolve("test-pomodoro");
        PersistenceManager.setDataDirectoryForTesting(testPath);

        assertFalse("Directory should not exist initially", Files.exists(testPath));

        PersistenceManager.ensureDirectoryExists();

        assertTrue("Directory should be created", Files.exists(testPath));
        assertTrue("Should be a directory", Files.isDirectory(testPath));
    }

    @Test
    public void testEnsureDirectoryExistsDoesNotFailIfDirectoryExists() throws IOException {
        Path testPath = tempFolder.getRoot().toPath();
        PersistenceManager.setDataDirectoryForTesting(testPath);

        // Should not throw exception even though directory already exists
        PersistenceManager.ensureDirectoryExists();

        assertTrue("Directory should still exist", Files.exists(testPath));
    }

    @Test
    public void testGetSettingsFileReturnsCorrectPath() {
        Path testPath = tempFolder.getRoot().toPath();
        PersistenceManager.setDataDirectoryForTesting(testPath);

        Path settingsFile = PersistenceManager.getSettingsFile();
        Path expected = testPath.resolve("settings.properties");

        assertEquals(expected, settingsFile);
    }

    @Test
    public void testGetTasksFileReturnsCorrectPath() {
        Path testPath = tempFolder.getRoot().toPath();
        PersistenceManager.setDataDirectoryForTesting(testPath);

        Path tasksFile = PersistenceManager.getTasksFile();
        Path expected = testPath.resolve("tasks.json");

        assertEquals(expected, tasksFile);
    }

    @Test
    public void testFilePathsUpdateWhenDataDirectoryChanges() {
        Path testPath1 = tempFolder.getRoot().toPath().resolve("dir1");
        Path testPath2 = tempFolder.getRoot().toPath().resolve("dir2");

        PersistenceManager.setDataDirectoryForTesting(testPath1);
        Path settingsFile1 = PersistenceManager.getSettingsFile();

        PersistenceManager.setDataDirectoryForTesting(testPath2);
        Path settingsFile2 = PersistenceManager.getSettingsFile();

        assertNotEquals("Settings file paths should differ", settingsFile1, settingsFile2);
        assertEquals(testPath1.resolve("settings.properties"), settingsFile1);
        assertEquals(testPath2.resolve("settings.properties"), settingsFile2);
    }
}
