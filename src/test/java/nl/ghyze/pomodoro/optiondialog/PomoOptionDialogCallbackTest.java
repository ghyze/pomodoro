package nl.ghyze.pomodoro.optiondialog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.ghyze.pomodoro.controller.PomodoroStateMachine;
import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.PomodoroType;
import nl.ghyze.pomodoro.model.Settings;

import java.lang.reflect.Field;

public class PomoOptionDialogCallbackTest
{

   private PomodoroStateMachine stateMachine;
   private Settings settings;

   @Before
   public void before()
   {
      // Use real objects instead of mocks
      settings = new Settings();
      settings.setPomoMinutes(25);
      settings.setShortBreakMinutes(5);
      settings.setLongBreakMinutes(15);
      settings.setPomosBeforeLongBreak(3);

      stateMachine = new PomodoroStateMachine(settings);
   }

   @Test
   public void testOk() throws Exception
   {
      // Set up state: POMO is running
      Pomodoro pomo = new Pomodoro(1, PomodoroType.POMO);
      setCurrent(pomo);

      PomoOptionDialogCallback callback = new PomoOptionDialogCallback(stateMachine);

      // When ok() is called on callback, it should call handleAction(SAVE)
      // which saves the pomodoro and goes to break
      callback.ok();

      // Verify that we transitioned to BREAK state (because SAVE was passed)
      Pomodoro current = getCurrent();
      Assert.assertEquals(PomodoroType.BREAK, current.getType());
   }

   private void setCurrent(Pomodoro current) throws Exception
   {
      Field currentField = PomodoroStateMachine.class.getDeclaredField("current");
      currentField.setAccessible(true);
      currentField.set(stateMachine, current);
   }

   private Pomodoro getCurrent() throws Exception
   {
      Field currentField = PomodoroStateMachine.class.getDeclaredField("current");
      currentField.setAccessible(true);
      return (Pomodoro) currentField.get(stateMachine);
   }
}
