package nl.ghyze.pomodoro.tasks;

import org.junit.Assert;
import org.junit.Test;


public class TaskTest {

    private boolean updated = false;

    @Test
    public void testAddCompletedPomo(){
        Task task = new Task("Test", 1);
        task.addObserver((o, arg) -> updated = true);

        task.addCompletedPomo();
        Assert.assertTrue(updated);
        Assert.assertEquals("Task name: Test, estimated: 1, actual: 1, Active: false", task.toString());
    }
}
