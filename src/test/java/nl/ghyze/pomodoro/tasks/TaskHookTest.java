package nl.ghyze.pomodoro.tasks;

import org.junit.Assert;
import org.junit.Test;

public class TaskHookTest {

    @Test
    public void testTaskHook() {
        TaskHook taskHook = new TaskHook();
        Task task = new Task("Test", 1);
        taskHook.setCurrentTask(task);
        taskHook.completed();

        Assert.assertEquals(1, task.getActual());
    }
}
