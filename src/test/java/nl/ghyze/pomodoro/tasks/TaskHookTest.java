package nl.ghyze.pomodoro.tasks;

import nl.ghyze.tasks.Task;
import nl.ghyze.tasks.TaskFrame;
import nl.ghyze.tasks.TaskHook;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TaskHookTest {

    private TaskHook taskHook;
    private Task task;

    @Before
    public void setUp() {
        taskHook = new TaskHook();
        task = new Task("Test Task", 5);
    }

    @Test
    public void testCompleted_IncrementsActualCount() {
        taskHook.setCurrentTask(task);
        taskHook.completed();

        Assert.assertEquals("Actual count should be 1 after completion", 1, task.getActual());
    }

    @Test
    public void testCompleted_MultipleCompletions() {
        taskHook.setCurrentTask(task);

        taskHook.completed();
        taskHook.completed();
        taskHook.completed();

        Assert.assertEquals("Actual count should be 3 after three completions", 3, task.getActual());
    }

    @Test
    public void testCompleted_WithNoCurrentTask_DoesNotCrash() {
        // No task set
        taskHook.completed();  // Should not throw exception
    }

    @Test
    public void testCompleted_WithTaskFrame_SavesTasks() {
        TaskFrame mockTaskFrame = EasyMock.createMock(TaskFrame.class);

        taskHook.setCurrentTask(task);
        taskHook.setTaskFrame(mockTaskFrame);

        // Expect saveTasks to be called
        mockTaskFrame.saveTasks();
        EasyMock.expectLastCall();
        EasyMock.replay(mockTaskFrame);

        taskHook.completed();

        EasyMock.verify(mockTaskFrame);
    }

    @Test
    public void testCompleted_WithoutTaskFrame_DoesNotCrash() {
        taskHook.setCurrentTask(task);
        // No task frame set

        taskHook.completed();  // Should not throw exception
        Assert.assertEquals(1, task.getActual());
    }

    @Test
    public void testCanceled_DoesNothing() {
        taskHook.setCurrentTask(task);

        taskHook.canceled();

        // Actual count should still be 0
        Assert.assertEquals("Canceled should not increment actual count", 0, task.getActual());
    }

    @Test
    public void testStarted_DoesNothing() {
        taskHook.setCurrentTask(task);

        taskHook.started();

        // Actual count should still be 0
        Assert.assertEquals("Started should not increment actual count", 0, task.getActual());
    }

    @Test
    public void testSetCurrentTask_ChangesActiveTask() {
        Task firstTask = new Task("First", 3);
        Task secondTask = new Task("Second", 5);

        taskHook.setCurrentTask(firstTask);
        taskHook.completed();
        Assert.assertEquals("First task should have 1 completion", 1, firstTask.getActual());

        taskHook.setCurrentTask(secondTask);
        taskHook.completed();
        taskHook.completed();

        Assert.assertEquals("First task should still have 1 completion", 1, firstTask.getActual());
        Assert.assertEquals("Second task should have 2 completions", 2, secondTask.getActual());
    }

    @Test
    public void testCompleted_AfterSwitchingTasks_UpdatesCorrectTask() {
        Task task1 = new Task("Task 1", 2);
        Task task2 = new Task("Task 2", 3);

        // Complete some for task1
        taskHook.setCurrentTask(task1);
        taskHook.completed();

        // Switch to task2 and complete
        taskHook.setCurrentTask(task2);
        taskHook.completed();
        taskHook.completed();

        // Verify each task has correct count
        Assert.assertEquals(1, task1.getActual());
        Assert.assertEquals(2, task2.getActual());
    }
}
