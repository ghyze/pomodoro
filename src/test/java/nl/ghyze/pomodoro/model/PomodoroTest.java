package nl.ghyze.pomodoro.model;

import static org.junit.Assert.*;

import nl.ghyze.pomodoro.type.PomodoroType;

import org.junit.Test;

public class PomodoroTest {

    @Test
    public void testInitialization(){
	Pomodoro pomodoro = new Pomodoro(0, PomodoroType.WAIT);
	assertEquals(PomodoroType.WAIT, pomodoro.getType());
	assertTrue(pomodoro.getMillisSinceStart() >= 0);
    }
    
    @Test
    public void testIsDone() throws InterruptedException {
	Pomodoro pomodoro = new Pomodoro(0, PomodoroType.POMO);
	Thread.sleep(15);
	assertTrue(pomodoro.isDone());
	
	Pomodoro notDone = new Pomodoro(1, PomodoroType.POMO);
	assertFalse(notDone.isDone());
    }
    
    @Test
    public void testMinutesLeft(){
	Pomodoro pomodoro = new Pomodoro(2, PomodoroType.BREAK);
	pause(40);
	assertEquals(1, pomodoro.minutesLeft());
    }
    
    @Test
    public void testSecondsLeft(){
	Pomodoro pomodoro = new Pomodoro(1, PomodoroType.BREAK);
	pause(40);
	assertEquals(59, pomodoro.secondsOfMinuteLeft());
    }
    
    @Test
    public void testGetPromosDone(){
	Pomodoro pomodoro = new Pomodoro(0, PomodoroType.POMO);
	assertEquals(0, pomodoro.getPomosDone());
	
	pomodoro.setPomosDone(2);
	assertEquals(2, pomodoro.getPomosDone());
    }
    
    @Test
    public void testGetMaxPomosDone(){
	Pomodoro pomodoro = new Pomodoro(0, PomodoroType.POMO);
	assertEquals(0, pomodoro.getMaxPomosDone());
	
	pomodoro.setMaxPomosDone(1);
	assertEquals(1, pomodoro.getMaxPomosDone());
	
	pomodoro.setPomosDone(2);
	assertEquals(2, pomodoro.getMaxPomosDone());
	
	pomodoro.setMaxPomosDone(3);
	assertEquals(3, pomodoro.getMaxPomosDone());
    }
    
    @Test
    public void testEquals() throws Exception {
	Pomodoro pomodoro = new Pomodoro(0, PomodoroType.WAIT);
	assertFalse(pomodoro.equals(null));
	assertFalse(pomodoro.equals("Something"));
	assertFalse(pomodoro.equals(new Pomodoro(0, PomodoroType.BREAK)));
	assertFalse(pomodoro.equals(new Pomodoro(1, PomodoroType.WAIT)));
	assertTrue(pomodoro.equals(new Pomodoro(0, PomodoroType.WAIT)));
    }
    
    @Test
    public void testHashcode() throws Exception {
	Pomodoro pomodoro = new Pomodoro(0, PomodoroType.WAIT);
	assertNotEquals(pomodoro.hashCode(), new Pomodoro(0, PomodoroType.BREAK).hashCode());
	assertNotEquals(pomodoro.hashCode(), new Pomodoro(1, PomodoroType.WAIT).hashCode());
    }
    
    private void pause(long milliseconds){
	try {
	    Thread.sleep(milliseconds);
	} catch (InterruptedException ex){
	    
	}
    }
}
