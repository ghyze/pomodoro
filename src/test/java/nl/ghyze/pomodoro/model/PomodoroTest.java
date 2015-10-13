package nl.ghyze.pomodoro.model;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import nl.ghyze.pomodoro.model.Pomodoro.Type;

public class PomodoroTest {

    @Test
    public void testInitialization(){
	Pomodoro pomodoro = new Pomodoro(0, Pomodoro.Type.WAIT);
	Assert.assertEquals(Pomodoro.Type.WAIT, pomodoro.getType());
	Assert.assertTrue(pomodoro.getMillisSinceStart() >= 0);
    }
    
    @Test
    public void testIsDone(){
	Pomodoro pomodoro = new Pomodoro(0, Pomodoro.Type.POMO);
	Assert.assertTrue(pomodoro.isDone());
	
	Pomodoro notDone = new Pomodoro(1, Pomodoro.Type.POMO);
	Assert.assertFalse(notDone.isDone());
    }
    
    @Test
    public void testMinutesLeft(){
	Pomodoro pomodoro = new Pomodoro(2, Pomodoro.Type.BREAK);
	pause(40);
	Assert.assertEquals(1, pomodoro.minutesLeft());
    }
    
    @Test
    public void testSecondsLeft(){
	Pomodoro pomodoro = new Pomodoro(1, Pomodoro.Type.BREAK);
	pause(40);
	Assert.assertEquals(59, pomodoro.secondsOfMinuteLeft());
    }
    
    @Test
    public void testGetPromosDone(){
	Pomodoro pomodoro = new Pomodoro(0, Pomodoro.Type.POMO);
	Assert.assertEquals(0, pomodoro.getPomosDone());
	
	pomodoro.setPomosDone(2);
	Assert.assertEquals(2, pomodoro.getPomosDone());
    }
    
    @Test
    public void testGetMaxPomosDone(){
	Pomodoro pomodoro = new Pomodoro(0, Pomodoro.Type.POMO);
	Assert.assertEquals(0, pomodoro.getMaxPomosDone());
	
	pomodoro.setMaxPomosDone(1);
	Assert.assertEquals(1, pomodoro.getMaxPomosDone());
	
	pomodoro.setPomosDone(2);
	Assert.assertEquals(2, pomodoro.getMaxPomosDone());
	
	pomodoro.setMaxPomosDone(3);
	Assert.assertEquals(3, pomodoro.getMaxPomosDone());
    }
    
    @Test
    public void testEquals() throws Exception {
	Pomodoro pomodoro = new Pomodoro(0, Type.WAIT);
	Assert.assertFalse(pomodoro.equals(null));
	Assert.assertFalse(pomodoro.equals("Something"));
	Assert.assertFalse(pomodoro.equals(new Pomodoro(0, Type.BREAK)));
	Assert.assertFalse(pomodoro.equals(new Pomodoro(1, Type.WAIT)));
	Assert.assertTrue(pomodoro.equals(new Pomodoro(0, Type.WAIT)));
    }
    
    @Test
    public void testHashcode() throws Exception {
	Pomodoro pomodoro = new Pomodoro(0, Type.WAIT);
	Assert.assertNotEquals(pomodoro.hashCode(), new Pomodoro(0, Type.BREAK).hashCode());
	Assert.assertNotEquals(pomodoro.hashCode(), new Pomodoro(1, Type.WAIT).hashCode());
    }
    
    private void pause(long milliseconds){
	try {
	    Thread.sleep(milliseconds);
	} catch (InterruptedException ex){
	    
	}
    }
}
