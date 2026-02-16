package nl.ghyze.pomodoro.tasks;

import nl.ghyze.tasks.Task;
import org.junit.Assert;
import org.junit.Test;


public class TaskTest {

    private boolean updated = false;

    @Test
    public void testAddCompletedPomo(){
        Task task = new Task("Test", 1);
        task.addPropertyChangeListener(evt -> updated = true);

        task.addCompletedPomo();
        Assert.assertTrue(updated);
        Assert.assertEquals("Task name: Test, estimated: 1, actual: 1, Active: false", task.toString());
    }
}
