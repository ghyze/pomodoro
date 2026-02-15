package nl.ghyze.pomodoro.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SettingsTest
{

   Settings testSettings;

   @Before
   public void setup()
   {
      testSettings = new Settings();
   }

   @Test
   public void testPosition()
   {
      testSettings.setPosition(Settings.Position.TOP_RIGHT);
      Assert.assertEquals(Settings.Position.TOP_RIGHT, testSettings.getPosition());
   }

   @Test
   public void testPomodoroMinutes()
   {
      testSettings.setPomoMinutes(1);
      Assert.assertEquals(1, testSettings.getPomoMinutes());
   }

   @Test
   public void testShortBreakMinutes()
   {
      testSettings.setShortBreakMinutes(1);
      Assert.assertEquals(1, testSettings.getShortBreakMinutes());
   }

   @Test
   public void testLongBreakMinutes()
   {
      testSettings.setLongBreakMinutes(1);
      Assert.assertEquals(1, testSettings.getLongBreakMinutes());
   }

   @Test
   public void testPomosBeforeLongBreak()
   {
      testSettings.setPomosBeforeLongBreak(1);
      Assert.assertEquals(1, testSettings.getPomosBeforeLongBreak());
   }

   @Test
   public void testAutoreset() throws Exception
   {
      testSettings.setAutoreset(true);
      Assert.assertTrue(testSettings.isAutoreset());

      testSettings.setAutoreset(false);
      Assert.assertFalse(testSettings.isAutoreset());
   }

   @Test
   public void testIdleTime() throws Exception
   {
      testSettings.setIdleTime(1);
      Assert.assertEquals(1, testSettings.getIdleTime());
   }

   @Test
   public void testListeners()
   {
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

   class TestSettingsChangeListener implements SettingsChangeListener
   {

      private int numberOfTimesCalled = 0;

      @Override
      public void onChange(Settings settings)
      {
         numberOfTimesCalled++;
      }

      public int getNumberOfTimesCalled()
      {
         return numberOfTimesCalled;
      }
   }

}
