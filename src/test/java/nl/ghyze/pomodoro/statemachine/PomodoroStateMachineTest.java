package nl.ghyze.pomodoro.statemachine;

import nl.ghyze.pomodoro.Stopwatch;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.optiondialog.OptionDialogModel;
import nl.ghyze.pomodoro.model.PomodoroType;
import nl.ghyze.settings.Settings;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;

public class PomodoroStateMachineTest
{

   private Settings settings;
   private PomodoroStateMachine pomodoroStateMachine;
   private AbstractSystemTrayManager systemTrayManager;

   @Before
   public void setUp()
   {
      // Use real Settings object with builder pattern
      settings = Settings.builder()
              .pomoMinutes(25)
              .shortBreakMinutes(5)
              .longBreakMinutes(15)
              .pomosBeforeLongBreak(3)
              .idleTime(60)
              .screenIndex(0)
              .position(Settings.Position.BOTTOM_RIGHT)
              .build();

      pomodoroStateMachine = new PomodoroStateMachine(settings);
   }

   @Test
   public void testGetCurrent()
   {
      Pomodoro expected = new Pomodoro(0, PomodoroType.WAIT);
      Assert.assertEquals(expected, pomodoroStateMachine.getCurrentPomodoro());
   }

   @Test
   public void testGetCurrentType()
   {
      Assert.assertEquals(PomodoroType.WAIT, pomodoroStateMachine.getCurrentPomodoroType());
   }

   @Test
   public void testStartPomo()
   {
      // No mock setup needed - using real Settings object
      settings.setPomoMinutes(1);
      settings.setPomosBeforeLongBreak(1);

      pomodoroStateMachine.startPomo();

      Pomodoro current = pomodoroStateMachine.getCurrentPomodoro();
      Assert.assertEquals(PomodoroType.POMO, current.getType());
   }

   @Test
   public void testShouldChangeState()
   {
      // Initial state is WAIT, so should not change state
      Assert.assertFalse(pomodoroStateMachine.shouldChangeState());

      // Create a POMO that has just started (not done yet)
      Pomodoro notDonePomodoro = new Pomodoro(25, PomodoroType.POMO);
      pomodoroStateMachine.setCurrent(notDonePomodoro);
      Assert.assertFalse(pomodoroStateMachine.shouldChangeState());

      // Create a POMO that has finished (started 2 minutes ago, duration is 1 minute)
      Pomodoro donePomodoro = createPomodoroStartedInPast(1, PomodoroType.POMO, 2);
      pomodoroStateMachine.setCurrent(donePomodoro);
      Assert.assertTrue(pomodoroStateMachine.shouldChangeState());
   }

   private Pomodoro createPomodoroStartedInPast(int minutes, PomodoroType type, int minutesAgo)
   {
      long pastTime = System.currentTimeMillis() - (minutesAgo * 60L * 1000L);
      Stopwatch stopwatch = new Stopwatch(pastTime);
      return new Pomodoro(minutes, type, pastTime, stopwatch);
   }

   private void setupGetNextFromPomo()   {
      Pomodoro pomodoro = new Pomodoro(1, PomodoroType.POMO);
      pomodoroStateMachine.setCurrent(pomodoro);
      systemTrayManager = EasyMock.createMock(AbstractSystemTrayManager.class);
      pomodoroStateMachine.setSystemTrayManager(systemTrayManager);

      // Use real Settings object - set value instead of mock expectation
      settings.setPomosBeforeLongBreak(1);
   }

   @Test
   public void testGetNextFromPomoWithDiscard()   {
      Pomodoro pomodoro = new Pomodoro(1, PomodoroType.BREAK);
      pomodoroStateMachine.setCurrent(pomodoro);
      systemTrayManager = EasyMock.createMock(AbstractSystemTrayManager.class);
      pomodoroStateMachine.setSystemTrayManager(systemTrayManager);

      settings.setPomosBeforeLongBreak(1);

      EasyMock.replay(systemTrayManager);
      pomodoroStateMachine.handleAction(OptionDialogModel.Choice.DISCARD);
      EasyMock.verify(systemTrayManager);

      Pomodoro current = pomodoroStateMachine.getCurrentPomodoro();
      Assert.assertEquals(0, current.getPomosDone());
      Assert.assertEquals(PomodoroType.WAIT, current.getType());
      Assert.assertEquals(0, current.minutesLeft());
   }

   @Test
   public void testGetNextFromPomoWithSave()   {
      setupGetNextFromPomo();

      systemTrayManager.message(EasyMock.eq("Well done! Long break"));
      EasyMock.expectLastCall();

      settings.setLongBreakMinutes(5);

      EasyMock.replay(systemTrayManager);
      pomodoroStateMachine.handleAction(OptionDialogModel.Choice.SAVE);
      EasyMock.verify(systemTrayManager);

      Pomodoro current = pomodoroStateMachine.getCurrentPomodoro();
      Assert.assertEquals(0, current.getPomosDone());
      Assert.assertEquals(PomodoroType.BREAK, current.getType());
      Assert.assertEquals(4, current.minutesLeft());
   }

   @Test
   public void testHandleNextForBreakWithOk()   {
      Pomodoro breakPomo = new Pomodoro(5, PomodoroType.BREAK);
      pomodoroStateMachine.setCurrent(breakPomo);

      settings.setPomoMinutes(1);
      settings.setPomosBeforeLongBreak(1);

      pomodoroStateMachine.handleAction(OptionDialogModel.Choice.OK);

      Assert.assertEquals(PomodoroType.POMO, pomodoroStateMachine.getCurrentPomodoroType());
   }

   @Test
   public void testHandleNextForBreakWithCancel()   {
      Pomodoro breakPomo = new Pomodoro(5, PomodoroType.BREAK);
      pomodoroStateMachine.setCurrent(breakPomo);

      pomodoroStateMachine.handleAction(OptionDialogModel.Choice.CANCEL);

      Assert.assertEquals(PomodoroType.WAIT, pomodoroStateMachine.getCurrentPomodoroType());
   }

   @Test
   public void testHandleActionForWait()   {
      Pomodoro waitPomo = new Pomodoro(0, PomodoroType.WAIT);
      pomodoroStateMachine.setCurrent(waitPomo);

      pomodoroStateMachine.handleAction(OptionDialogModel.Choice.CANCEL);

      Assert.assertEquals(PomodoroType.WAIT, pomodoroStateMachine.getCurrentPomodoroType());
   }

   @Test
   public void testStopCurrent()   {
      Pomodoro pomo = new Pomodoro(1, PomodoroType.POMO);
      pomodoroStateMachine.setCurrent(pomo);

      pomodoroStateMachine.stopCurrent();

      Assert.assertEquals(PomodoroType.WAIT, pomodoroStateMachine.getCurrentPomodoroType());
   }

   @Test
   public void testReset()
   {
      pomodoroStateMachine.setPomosDone(2);

      pomodoroStateMachine.reset();
      Pomodoro wait = pomodoroStateMachine.getCurrentPomodoro();
      Assert.assertEquals(0, wait.getPomosDone());
      Assert.assertEquals(PomodoroType.WAIT, wait.getType());
   }

}
