package nl.ghyze.tasks;

import nl.ghyze.statistics.TaskStatisticsHook;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskServiceTest {

    private TaskRepository mockRepository;
    private TaskHook taskHook;
    private TaskService taskService;

    @Before
    public void setUp() {
        mockRepository = EasyMock.createMock(TaskRepository.class);
        taskHook = new TaskHook();

        EasyMock.expect(mockRepository.loadAll()).andReturn(new ArrayList<>());
        EasyMock.replay(mockRepository);

        taskService = new TaskService(mockRepository, taskHook);

        EasyMock.verify(mockRepository);
        EasyMock.reset(mockRepository);
    }

    @Test
    public void testAddTask_AddsToListAndSaves() {
        final Task task = Task.builder().name("Test").estimated(3).build();

        mockRepository.saveAll(EasyMock.anyObject());
        EasyMock.expectLastCall();
        EasyMock.replay(mockRepository);

        taskService.addTask(task);

        Assert.assertTrue(taskService.getTasks().contains(task));
        EasyMock.verify(mockRepository);
    }

    @Test
    public void testAddTask_LogsStatistics_WhenHookPresent() {
        final Task task = Task.builder().name("Test").estimated(3).build();
        final TaskStatisticsHook mockStatsHook = EasyMock.createMock(TaskStatisticsHook.class);
        taskService.setStatisticsHook(mockStatsHook);

        mockStatsHook.registerTask(task);
        EasyMock.expectLastCall();
        mockStatsHook.logCreated(task);
        EasyMock.expectLastCall();
        mockRepository.saveAll(EasyMock.anyObject());
        EasyMock.expectLastCall();
        EasyMock.replay(mockRepository, mockStatsHook);

        taskService.addTask(task);

        EasyMock.verify(mockRepository, mockStatsHook);
    }

    @Test
    public void testAddTask_NoThrow_WhenHookNull() {
        final Task task = Task.builder().name("Test").estimated(3).build();

        mockRepository.saveAll(EasyMock.anyObject());
        EasyMock.expectLastCall();
        EasyMock.replay(mockRepository);

        taskService.addTask(task);  // statisticsHook is null — should not throw

        EasyMock.verify(mockRepository);
    }

    @Test
    public void testActivateTask_DeactivatesPreviousTask() {
        final Task task1 = Task.builder().name("Task1").estimated(1).build();
        final Task task2 = Task.builder().name("Task2").estimated(1).build();

        mockRepository.saveAll(EasyMock.anyObject());
        EasyMock.expectLastCall().anyTimes();
        EasyMock.replay(mockRepository);

        taskService.addTask(task1);
        taskService.activateTask(task1);  // activate first
        taskService.activateTask(task2);  // activate second — should deactivate first

        Assert.assertFalse("task1 should be deactivated", task1.isActive());
        Assert.assertTrue("task2 should be active", task2.isActive());
    }

    @Test
    public void testActivateTask_AutoTransitions_PendingToInProgress() {
        final Task task = Task.builder().name("Task").estimated(1).build();

        mockRepository.saveAll(EasyMock.anyObject());
        EasyMock.expectLastCall().anyTimes();
        EasyMock.replay(mockRepository);

        taskService.addTask(task);
        Assert.assertEquals(TaskState.PENDING, task.getState());

        taskService.activateTask(task);

        Assert.assertEquals(TaskState.IN_PROGRESS, task.getState());
    }

    @Test
    public void testActivateTask_AutoTransitions_InProgressToPending_OnDeactivation() {
        final Task task1 = Task.builder().name("Task1").estimated(1).build();
        final Task task2 = Task.builder().name("Task2").estimated(1).build();

        mockRepository.saveAll(EasyMock.anyObject());
        EasyMock.expectLastCall().anyTimes();
        EasyMock.replay(mockRepository);

        taskService.addTask(task1);
        taskService.addTask(task2);
        taskService.activateTask(task1);  // task1 becomes IN_PROGRESS

        Assert.assertEquals(TaskState.IN_PROGRESS, task1.getState());

        taskService.activateTask(task2);  // task1 should revert to PENDING

        Assert.assertEquals(TaskState.PENDING, task1.getState());
    }

    @Test
    public void testActivateTask_SetsCurrentTaskOnHook() {
        final Task task = Task.builder().name("Task").estimated(1).build();

        mockRepository.saveAll(EasyMock.anyObject());
        EasyMock.expectLastCall().anyTimes();
        EasyMock.replay(mockRepository);

        taskService.addTask(task);
        taskService.activateTask(task);

        Assert.assertEquals(task, taskHook.getCurrentTask());
    }

    @Test
    public void testActivateTask_CallsNotifyChangedOnce() {
        final Task task = Task.builder().name("Task").estimated(1).build();
        final AtomicInteger notifyCount = new AtomicInteger(0);
        taskService.setOnChanged(notifyCount::incrementAndGet);

        mockRepository.saveAll(EasyMock.anyObject());
        EasyMock.expectLastCall().anyTimes();
        EasyMock.replay(mockRepository);

        taskService.addTask(task);
        notifyCount.set(0);  // reset after addTask

        taskService.activateTask(task);

        Assert.assertEquals("notifyChanged should be called exactly once during activation", 1, notifyCount.get());
    }

    @Test
    public void testRemoveTask_RemovesFromList() {
        final Task task = Task.builder().name("Task").estimated(1).build();

        mockRepository.saveAll(EasyMock.anyObject());
        EasyMock.expectLastCall().anyTimes();
        EasyMock.replay(mockRepository);

        taskService.addTask(task);
        Assert.assertTrue(taskService.getTasks().contains(task));

        taskService.removeTask(task);

        Assert.assertFalse(taskService.getTasks().contains(task));
    }

    @Test
    public void testEditTask_SavesWithoutCallingNotifyChanged() {
        final Task task = Task.builder().name("Task").estimated(1).build();
        final AtomicInteger notifyCount = new AtomicInteger(0);
        taskService.setOnChanged(notifyCount::incrementAndGet);

        mockRepository.saveAll(EasyMock.anyObject());
        EasyMock.expectLastCall().anyTimes();
        EasyMock.replay(mockRepository);

        taskService.addTask(task);
        notifyCount.set(0);  // reset after addTask

        taskService.editTask(task);

        Assert.assertEquals("editTask should not call notifyChanged", 0, notifyCount.get());
        EasyMock.verify(mockRepository);
    }

    @Test
    public void testStateChangeToDone_AutoDeactivatesActiveTask() {
        final Task task = Task.builder().name("Task").estimated(1).build();

        mockRepository.saveAll(EasyMock.anyObject());
        EasyMock.expectLastCall().anyTimes();
        EasyMock.replay(mockRepository);

        taskService.addTask(task);
        taskService.activateTask(task);
        Assert.assertTrue(task.isActive());
        Assert.assertEquals(task, taskHook.getCurrentTask());

        task.setState(TaskState.DONE);

        Assert.assertFalse("task should be auto-deactivated when set to DONE", task.isActive());
        Assert.assertNull("taskHook currentTask should be null after DONE", taskHook.getCurrentTask());
    }

    @Test
    public void testSetStatisticsHook_RegistersWithExistingTasks() {
        // Create service with pre-loaded tasks
        final Task existingTask = Task.builder().name("Existing").estimated(2).build();

        EasyMock.reset(mockRepository);
        EasyMock.expect(mockRepository.loadAll()).andReturn(new ArrayList<>(List.of(existingTask)));
        EasyMock.replay(mockRepository);

        final TaskService serviceWithTasks = new TaskService(mockRepository, taskHook);
        EasyMock.reset(mockRepository);

        final TaskStatisticsHook mockStatsHook = EasyMock.createMock(TaskStatisticsHook.class);
        mockStatsHook.registerTask(existingTask);
        EasyMock.expectLastCall();
        EasyMock.replay(mockStatsHook);

        serviceWithTasks.setStatisticsHook(mockStatsHook);

        EasyMock.verify(mockStatsHook);
    }
}
