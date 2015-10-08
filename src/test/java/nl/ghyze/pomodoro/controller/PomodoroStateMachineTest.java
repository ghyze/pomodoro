package nl.ghyze.pomodoro.controller;

import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;
import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.Settings;

public class PomodoroStateMachineTest {

    private Settings settings;
    private PomodoroStateMachine pomodoroStateMachine;
    
    @Before
    public void setUp(){
	createSettings();
	pomodoroStateMachine = new PomodoroStateMachine(settings);
    }
    
    private void createSettings(){
	settings = new Settings();
	settings.setPomoMinutes(1);
	settings.setShortBreakMinutes(2);
	settings.setLongBreakMinutes(3);
	settings.setPomosBeforeLongBreak(4);
	settings.setPosition(Settings.Position.TOP_LEFT);
    }
    
    @Test
    public void testGetCurrent(){
	Pomodoro expected = new Pomodoro(0, Pomodoro.Type.WAIT);
	Assert.assertEquals(expected, pomodoroStateMachine.getCurrent());
    }
    
    @Test
    public void testGetCurrentType(){
	Assert.assertEquals(Pomodoro.Type.WAIT, pomodoroStateMachine.getCurrentType());
    }
}
