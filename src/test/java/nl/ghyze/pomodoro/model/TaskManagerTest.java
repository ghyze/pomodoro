package nl.ghyze.pomodoro.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TaskManagerTest {

	TaskManager manager;
	
	@Before
	public void setup(){
		manager = new TaskManager();
	}
	
	@Test
	public void testInitialTaskList(){
		TaskManager manager = new TaskManager();
		assertEquals(1, manager.getNumberOfTasks());
		assertNotNull(manager.getTask(0));
		assertTrue(manager.getTask(0).isEmpty());
	}
	
	@Test
	public void testAddTask(){
		
	}
}
