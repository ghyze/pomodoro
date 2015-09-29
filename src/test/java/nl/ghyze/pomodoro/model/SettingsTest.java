package nl.ghyze.pomodoro.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SettingsTest {

    // originalSettings are used to save the currently stored settings, so they can be put back when the test is done.
    Settings originalSettings;
    
    Settings testSettings;
    
    @Before
    public void setup(){
	originalSettings = new Settings();
	originalSettings.load();
	
	testSettings = new Settings();
    }
    
    @After
    public void tearDown(){
	originalSettings.save();
    }
    
    @Test
    public void testPosition(){
	testSettings.setPosition(Settings.Position.TOP_RIGHT);
	Assert.assertEquals(Settings.Position.TOP_RIGHT, testSettings.getPosition());
    }
    
    @Test
    public void testPomodoroMinutes(){
	testSettings.setPomoMinutes(1);
	Assert.assertEquals(1, testSettings.getPomoMinutes());
    }
    
    @Test
    public void testShortBreakMinutes(){
	testSettings.setShortBreakMinutes(1);
	Assert.assertEquals(1, testSettings.getShortBreakMinutes());
    }
    
    @Test
    public void testLongBreakMinutes(){
	testSettings.setLongBreakMinutes(1);
	Assert.assertEquals(1, testSettings.getLongBreakMinutes());
    }
    
    @Test
    public void testPomosBeforeLongBreak(){
	testSettings.setPomosBeforeLongBreak(1);
	Assert.assertEquals(1,  testSettings.getPomosBeforeLongBreak());
    }
    
    @Test
    public void testListeners(){
	TestSettingsChangeListener listener = new TestSettingsChangeListener();
	Assert.assertEquals(0, listener.getNumberOfTimesCalled());
	testSettings.addListener(listener);
	Assert.assertEquals(0, listener.getNumberOfTimesCalled());
	testSettings.setPosition(Settings.Position.TOP_RIGHT);
	Assert.assertEquals(1, listener.getNumberOfTimesCalled());
	testSettings.removeListener(listener);
	Assert.assertEquals(1, listener.getNumberOfTimesCalled());
	testSettings.setPosition(Settings.Position.TOP_RIGHT);
	Assert.assertEquals(1, listener.getNumberOfTimesCalled());
    }
    
    class TestSettingsChangeListener implements SettingsChangeListener {

	private int numberOfTimesCalled = 0;
	
	@Override
	public void onChange(Settings settings) {
	    numberOfTimesCalled ++;
	}
	
	public int getNumberOfTimesCalled(){
	    return numberOfTimesCalled;
	}
    }
    
    @Test
    public void testPersistence(){
	testSettings.setPomoMinutes(1);
	testSettings.setShortBreakMinutes(2);
	testSettings.setLongBreakMinutes(3);
	testSettings.setPomosBeforeLongBreak(4);
	testSettings.setPosition(Settings.Position.TOP_LEFT);
	testSettings.save();
	
	Settings loadedSettings = new Settings();
	loadedSettings.load();
	Assert.assertEquals(1, loadedSettings.getPomoMinutes());
	Assert.assertEquals(2, loadedSettings.getShortBreakMinutes());
	Assert.assertEquals(3, loadedSettings.getLongBreakMinutes());
	Assert.assertEquals(4, loadedSettings.getPomosBeforeLongBreak());
	Assert.assertEquals(Settings.Position.TOP_LEFT, loadedSettings.getPosition());
    }
}
