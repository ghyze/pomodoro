package nl.ghyze.pomodoro.controller;

import java.lang.reflect.Field;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.Pomodoro.Type;
import nl.ghyze.pomodoro.model.Settings;

public class PomodoroStateMachineTest {

	private Settings settings;
	private PomodoroStateMachine pomodoroStateMachine;

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
}
