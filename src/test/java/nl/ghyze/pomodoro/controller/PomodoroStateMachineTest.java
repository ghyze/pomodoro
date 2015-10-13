package nl.ghyze.pomodoro.controller;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.Pomodoro.Type;
import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.view.systemtray.SystemTrayManager;

public class PomodoroStateMachineTest {

	private Settings settings;
	private PomodoroStateMachine pomodoroStateMachine;
	private SystemTrayManager systemTrayManager;

	@Before
	public void setUp() {
		settings = EasyMock.createMock(Settings.class);
		pomodoroStateMachine = new PomodoroStateMachine(settings);
	}

	@Test
	public void testGetCurrent() {
		Pomodoro expected = new Pomodoro(0, Pomodoro.Type.WAIT);
		Assert.assertEquals(expected, pomodoroStateMachine.getCurrent());
	}

	@Test
	public void testGetCurrentType() {
		Assert.assertEquals(Pomodoro.Type.WAIT, pomodoroStateMachine.getCurrentType());
	}

	@Test
	public void testStartPomo() {
		setupStartPomo();
		EasyMock.replay(settings);
		pomodoroStateMachine.startPomo();
		EasyMock.verify(settings);

		Pomodoro current = pomodoroStateMachine.getCurrent();
		Assert.assertEquals(current.getType(), Type.POMO);

	}
	
	private void setupStartPomo(){
		EasyMock.expect(settings.getPomoMinutes()).andReturn(1);
		EasyMock.expect(settings.getPomosBeforeLongBreak()).andReturn(1);
	}

	@Test
	public void testShouldChangeState() throws Exception{
		Assert.assertFalse(pomodoroStateMachine.shouldChangeState());
		
		Pomodoro pomodoroMock = EasyMock.createMock(Pomodoro.class);
		EasyMock.expect(pomodoroMock.getType()).andReturn(Type.POMO);
		EasyMock.expect(pomodoroMock.isDone()).andReturn(false);
		setCurrent(pomodoroMock);
		EasyMock.replay(pomodoroMock);
		Assert.assertFalse(pomodoroStateMachine.shouldChangeState());
		EasyMock.verify(pomodoroMock);
		
		EasyMock.reset(pomodoroMock);
		
		EasyMock.expect(pomodoroMock.getType()).andReturn(Type.POMO);
		EasyMock.expect(pomodoroMock.isDone()).andReturn(true);
		EasyMock.replay(pomodoroMock);
		Assert.assertTrue(pomodoroStateMachine.shouldChangeState());
		EasyMock.verify(pomodoroMock);

	}
	
	private void setCurrent(Pomodoro current) throws Exception{
		Field currentField = PomodoroStateMachine.class.getDeclaredField("current");
		currentField.setAccessible(true);
		currentField.set(pomodoroStateMachine, current);
	}
	
	private void setupGetNextFromPomo() throws Exception{
	    Pomodoro pomodoro = new Pomodoro(1, Type.POMO);
	    setCurrent(pomodoro);
	    systemTrayManager = EasyMock.createMock(SystemTrayManager.class);
	    pomodoroStateMachine.setSystemTrayManager(systemTrayManager);
	    
	    EasyMock.expect(settings.getPomosBeforeLongBreak()).andReturn(1).times(2);
	}
	
	@Test
	public void testGetNextFromPomoWithDiscard() throws Exception {
	    setupGetNextFromPomo();
	    
	    systemTrayManager.message(EasyMock.eq("Well done: Short break"));
	    EasyMock.expectLastCall();
	    
	    EasyMock.expect(settings.getShortBreakMinutes()).andReturn(1);
	    
	    EasyMock.replay(systemTrayManager, settings);
	    pomodoroStateMachine.handleAction(OptionDialogModel.DISCARD);
	    EasyMock.verify(systemTrayManager, settings);
	    
	    Pomodoro current = pomodoroStateMachine.getCurrent();
	    Assert.assertEquals(0, current.getPomosDone());
	    Assert.assertEquals(Type.BREAK, current.getType());
	    Assert.assertEquals(0, current.minutesLeft());
	}
	
	@Test
	public void testGetNextFromPomoWithSave() throws Exception {
	    setupGetNextFromPomo();
	    
	    systemTrayManager.message(EasyMock.eq("Well done: Long break"));
	    EasyMock.expectLastCall();
	    
	    EasyMock.expect(settings.getLongBreakMinutes()).andReturn(5);
	    
	    EasyMock.replay(systemTrayManager, settings);
	    pomodoroStateMachine.handleAction(OptionDialogModel.SAVE);
	    EasyMock.verify(systemTrayManager, settings);
	    
	    Pomodoro current = pomodoroStateMachine.getCurrent();
	    Assert.assertEquals(0, current.getPomosDone());
	    Assert.assertEquals(Type.BREAK, current.getType());
	    Assert.assertEquals(4, current.minutesLeft());
	}
	
	
}