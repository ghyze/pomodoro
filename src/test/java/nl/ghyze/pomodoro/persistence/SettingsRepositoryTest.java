package nl.ghyze.pomodoro.persistence;

import nl.ghyze.pomodoro.model.Settings;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.Assert.*;

public class SettingsRepositoryTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private SettingsRepository repository;
    private Path testDataDir;

    @Before
    public void setUp() throws IOException {
        testDataDir = tempFolder.newFolder("test-pomodoro").toPath();
        PersistenceManager.setDataDirectoryForTesting(testDataDir);
        repository = new SettingsRepository();
    }

    @After
    public void tearDown() {
        PersistenceManager.setDataDirectoryForTesting(null);
    }

    @Test
    public void testLoadFromNonExistentFileReturnsDefaults() {
        Settings settings = repository.load();

        assertNotNull(settings);
        assertEquals(25, settings.getPomoMinutes());
        assertEquals(5, settings.getShortBreakMinutes());
        assertEquals(15, settings.getLongBreakMinutes());
        assertEquals(3, settings.getPomosBeforeLongBreak());
        assertEquals(0, settings.getScreenIndex());
        assertEquals(Settings.Position.BOTTOM_RIGHT, settings.getPosition());
        assertFalse(settings.isAutoreset());
        assertEquals(60, settings.getIdleTime());
    }

    @Test
    public void testSaveCreatesPropertiesFile() throws IOException {
        Settings settings = Settings.builder()
                .pomoMinutes(30)
                .shortBreakMinutes(10)
                .longBreakMinutes(20)
                .pomosBeforeLongBreak(4)
                .screenIndex(1)
                .position(Settings.Position.TOP_LEFT)
                .autoreset(true)
                .idleTime(120)
                .build();

        repository.save(settings);

        Path settingsFile = PersistenceManager.getSettingsFile();
        assertTrue("Settings file should exist", Files.exists(settingsFile));

        // Verify it's a valid properties file
        Properties props = new Properties();
        props.load(Files.newInputStream(settingsFile));

        assertEquals("30", props.getProperty("pomoMinutes"));
        assertEquals("10", props.getProperty("shortBreakNMinutes"));
        assertEquals("20", props.getProperty("longBreakMinutes"));
        assertEquals("4", props.getProperty("pomosBeforeLongBreak"));
        assertEquals("1", props.getProperty("screenIndex"));
        assertEquals("TOP_LEFT", props.getProperty("position"));
        assertEquals("true", props.getProperty("autoReset"));
        assertEquals("120", props.getProperty("idleTime"));
    }

    @Test
    public void testRoundTripPreservesAllFields() {
        Settings original = Settings.builder()
                .pomoMinutes(35)
                .shortBreakMinutes(7)
                .longBreakMinutes(18)
                .pomosBeforeLongBreak(5)
                .screenIndex(2)
                .position(Settings.Position.BOTTOM_LEFT)
                .autoreset(true)
                .idleTime(90)
                .build();

        repository.save(original);
        Settings loaded = repository.load();

        assertEquals(original.getPomoMinutes(), loaded.getPomoMinutes());
        assertEquals(original.getShortBreakMinutes(), loaded.getShortBreakMinutes());
        assertEquals(original.getLongBreakMinutes(), loaded.getLongBreakMinutes());
        assertEquals(original.getPomosBeforeLongBreak(), loaded.getPomosBeforeLongBreak());
        assertEquals(original.getScreenIndex(), loaded.getScreenIndex());
        assertEquals(original.getPosition(), loaded.getPosition());
        assertEquals(original.isAutoreset(), loaded.isAutoreset());
        assertEquals(original.getIdleTime(), loaded.getIdleTime());
    }

    @Test
    public void testLoadFromExistingPropertiesFile() throws IOException {
        // Manually create a properties file
        Properties props = new Properties();
        props.setProperty("pomoMinutes", "40");
        props.setProperty("shortBreakNMinutes", "8");
        props.setProperty("longBreakMinutes", "25");
        props.setProperty("pomosBeforeLongBreak", "6");
        props.setProperty("screenIndex", "3");
        props.setProperty("position", "TOP_RIGHT");
        props.setProperty("autoReset", "true");
        props.setProperty("idleTime", "180");

        PersistenceManager.ensureDirectoryExists();
        Path settingsFile = PersistenceManager.getSettingsFile();
        props.store(Files.newOutputStream(settingsFile), "Test settings");

        Settings loaded = repository.load();

        assertEquals(40, loaded.getPomoMinutes());
        assertEquals(8, loaded.getShortBreakMinutes());
        assertEquals(25, loaded.getLongBreakMinutes());
        assertEquals(6, loaded.getPomosBeforeLongBreak());
        assertEquals(3, loaded.getScreenIndex());
        assertEquals(Settings.Position.TOP_RIGHT, loaded.getPosition());
        assertTrue(loaded.isAutoreset());
        assertEquals(180, loaded.getIdleTime());
    }

    @Test
    public void testPositionEnumSerializationAllValues() {
        for (Settings.Position position : Settings.Position.values()) {
            Settings settings = Settings.builder()
                    .pomoMinutes(25)
                    .shortBreakMinutes(5)
                    .longBreakMinutes(15)
                    .pomosBeforeLongBreak(3)
                    .screenIndex(0)
                    .position(position)
                    .autoreset(false)
                    .idleTime(60)
                    .build();

            repository.save(settings);
            Settings loaded = repository.load();

            assertEquals("Position should be preserved: " + position, position, loaded.getPosition());
        }
    }

    @Test
    public void testCorruptedPropertiesFileReturnsDefaults() throws IOException {
        // Create a corrupted properties file
        PersistenceManager.ensureDirectoryExists();
        Path settingsFile = PersistenceManager.getSettingsFile();
        Files.writeString(settingsFile, "this is not a valid properties file\n@#$%^&*()");

        Settings settings = repository.load();

        // Should return defaults instead of crashing
        assertNotNull(settings);
        assertEquals(25, settings.getPomoMinutes());
    }

    @Test
    public void testPropertiesFileWithMissingFieldsUsesDefaults() throws IOException {
        // Create properties file with only some fields
        Properties props = new Properties();
        props.setProperty("pomoMinutes", "50");
        // Missing other fields

        PersistenceManager.ensureDirectoryExists();
        Path settingsFile = PersistenceManager.getSettingsFile();
        props.store(Files.newOutputStream(settingsFile), "Partial settings");

        Settings loaded = repository.load();

        assertEquals(50, loaded.getPomoMinutes());
        // Other fields should use defaults
        assertEquals(5, loaded.getShortBreakMinutes());
        assertEquals(15, loaded.getLongBreakMinutes());
    }

    @Test
    public void testBooleanParsing() throws IOException {
        Properties props = new Properties();
        props.setProperty("pomoMinutes", "25");
        props.setProperty("shortBreakNMinutes", "5");
        props.setProperty("longBreakMinutes", "15");
        props.setProperty("pomosBeforeLongBreak", "3");
        props.setProperty("screenIndex", "0");
        props.setProperty("position", "BOTTOM_RIGHT");
        props.setProperty("autoReset", "true");
        props.setProperty("idleTime", "60");

        PersistenceManager.ensureDirectoryExists();
        Path settingsFile = PersistenceManager.getSettingsFile();
        props.store(Files.newOutputStream(settingsFile), "Test");

        Settings loaded = repository.load();
        assertTrue("autoReset should be true", loaded.isAutoreset());

        props.setProperty("autoReset", "false");
        props.store(Files.newOutputStream(settingsFile), "Test");

        loaded = repository.load();
        assertFalse("autoReset should be false", loaded.isAutoreset());
    }

    @Test
    public void testIntegerParsing() throws IOException {
        Properties props = new Properties();
        props.setProperty("pomoMinutes", "999");
        props.setProperty("shortBreakNMinutes", "1");
        props.setProperty("longBreakMinutes", "500");
        props.setProperty("pomosBeforeLongBreak", "10");
        props.setProperty("screenIndex", "5");
        props.setProperty("position", "BOTTOM_RIGHT");
        props.setProperty("autoReset", "false");
        props.setProperty("idleTime", "300");

        PersistenceManager.ensureDirectoryExists();
        Path settingsFile = PersistenceManager.getSettingsFile();
        props.store(Files.newOutputStream(settingsFile), "Test");

        Settings loaded = repository.load();

        assertEquals(999, loaded.getPomoMinutes());
        assertEquals(1, loaded.getShortBreakMinutes());
        assertEquals(500, loaded.getLongBreakMinutes());
        assertEquals(10, loaded.getPomosBeforeLongBreak());
        assertEquals(5, loaded.getScreenIndex());
        assertEquals(300, loaded.getIdleTime());
    }

    @Test
    public void testFileCreatedInCorrectLocation() {
        repository.load(); // Triggers migration and save

        Path expectedFile = testDataDir.resolve("settings.properties");
        assertTrue("Settings file should be in test directory", Files.exists(expectedFile));
    }

    @Test
    public void testMultipleSavesDontCorruptFile() {
        Settings settings1 = Settings.builder()
                .pomoMinutes(25)
                .shortBreakMinutes(5)
                .longBreakMinutes(15)
                .pomosBeforeLongBreak(3)
                .screenIndex(0)
                .position(Settings.Position.BOTTOM_RIGHT)
                .autoreset(false)
                .idleTime(60)
                .build();

        repository.save(settings1);

        Settings settings2 = Settings.builder()
                .pomoMinutes(30)
                .shortBreakMinutes(10)
                .longBreakMinutes(20)
                .pomosBeforeLongBreak(4)
                .screenIndex(1)
                .position(Settings.Position.TOP_LEFT)
                .autoreset(true)
                .idleTime(120)
                .build();

        repository.save(settings2);

        Settings loaded = repository.load();

        // Should match the last saved settings
        assertEquals(settings2.getPomoMinutes(), loaded.getPomoMinutes());
        assertEquals(settings2.getPosition(), loaded.getPosition());
        assertTrue(loaded.isAutoreset());
    }
}
