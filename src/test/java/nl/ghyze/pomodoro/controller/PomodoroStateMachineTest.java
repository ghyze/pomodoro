package nl.ghyze.pomodoro.controller;

import java.lang.reflect.Field;

import nl.ghyze.pomodoro.Stopwatch;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.optiondialog.OptionDialogModel;
import nl.ghyze.pomodoro.model.PomodoroType;
import nl.ghyze.pomodoro.model.Settings;
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
      Assert.assertEquals(expected, PomodoroStateMachine.getCurrent());
   }

   @Test
   public void testGetCurrentType()
   {
      Assert.assertEquals(PomodoroType.WAIT, PomodoroStateMachine.getCurrentType());
   }

   @Test
   public void testStartPomo()
   {
      // No mock setup needed - using real Settings object
      settings.setPomoMinutes(1);
      settings.setPomosBeforeLongBreak(1);

      pomodoroStateMachine.startPomo();

      Pomodoro current = PomodoroStateMachine.getCurrent();
      Assert.assertEquals(current.getType(), PomodoroType.POMO);
   }

   @Test
   public void testShouldChangeState() throws Exception
   {
      // Initial state is WAIT, so should not change state
      Assert.assertFalse(pomodoroStateMachine.shouldChangeState());

      // Create a POMO that has just started (not done yet)
      Pomodoro notDonePomodoro = new Pomodoro(25, PomodoroType.POMO);
      setCurrent(notDonePomodoro);
      Assert.assertFalse(pomodoroStateMachine.shouldChangeState());

      // Create a POMO that has finished (set start time in the past)
      Pomodoro donePomodoro = new Pomodoro(1, PomodoroType.POMO);
      setStartTimeInPast(donePomodoro, 2); // Started 2 minutes ago, duration is 1 minute
      setCurrent(donePomodoro);
      Assert.assertTrue(pomodoroStateMachine.shouldChangeState());
   }

   private void setStartTimeInPast(Pomodoro pomodoro, int minutesAgo) throws Exception
   {
      // Set start time in the past
      Field startTimeField = Pomodoro.class.getDeclaredField("startTime");
      startTimeField.setAccessible(true);
      long pastTime = System.currentTimeMillis() - (minutesAgo * 60 * 1000);
      startTimeField.set(pomodoro, pastTime);

      // Also need to set the stopwatch's start time since isDone() uses it
      Field stopwatchField = Pomodoro.class.getDeclaredField("stopwatch");
      stopwatchField.setAccessible(true);

      // Create a stopwatch with start time in the past
      Stopwatch stopwatch = new Stopwatch();
      Field stopwatchStartField = Stopwatch.class.getDeclaredField("start");
      stopwatchStartField.setAccessible(true);
      stopwatchStartField.set(stopwatch, pastTime);

      stopwatchField.set(pomodoro, stopwatch);
   }

   private void setCurrent(Pomodoro current) throws Exception
   {
      Field currentField = PomodoroStateMachine.class.getDeclaredField("current");
      currentField.setAccessible(true);
      currentField.set(pomodoroStateMachine, current);
   }

   private void setupGetNextFromPomo() throws Exception
   {
      Pomodoro pomodoro = new Pomodoro(1, PomodoroType.POMO);
      setCurrent(pomodoro);
      systemTrayManager = EasyMock.createMock(AbstractSystemTrayManager.class);
      pomodoroStateMachine.setSystemTrayManager(systemTrayManager);

      // Use real Settings object - set value instead of mock expectation
      settings.setPomosBeforeLongBreak(1);
   }

   @Test
   public void testGetNextFromPomoWithDiscard() throws Exception
   {
      Pomodoro pomodoro = new Pomodoro(1, PomodoroType.BREAK);
      setCurrent(pomodoro);
      systemTrayManager = EasyMock.createMock(AbstractSystemTrayManager.class);
      pomodoroStateMachine.setSystemTrayManager(systemTrayManager);

      settings.setPomosBeforeLongBreak(1);

      EasyMock.replay(systemTrayManager);
      pomodoroStateMachine.handleAction(OptionDialogModel.Choice.DISCARD);
      EasyMock.verify(systemTrayManager);

      Pomodoro current = PomodoroStateMachine.getCurrent();
      Assert.assertEquals(0, current.getPomosDone());
      Assert.assertEquals(PomodoroType.WAIT, current.getType());
      Assert.assertEquals(0, current.minutesLeft());
   }

   @Test
   public void testGetNextFromPomoWithSave() throws Exception
   {
      setupGetNextFromPomo();

      systemTrayManager.message(EasyMock.eq("Well done! Long break"));
      EasyMock.expectLastCall();

      settings.setLongBreakMinutes(5);

      EasyMock.replay(systemTrayManager);
      pomodoroStateMachine.handleAction(OptionDialogModel.Choice.SAVE);
      EasyMock.verify(systemTrayManager);

      Pomodoro current = PomodoroStateMachine.getCurrent();
      Assert.assertEquals(0, current.getPomosDone());
      Assert.assertEquals(PomodoroType.BREAK, current.getType());
      Assert.assertEquals(4, current.minutesLeft());
   }

   @Test
   public void testHandleNextForBreakWithOk() throws Exception
   {
      Pomodoro breakPomo = new Pomodoro(5, PomodoroType.BREAK);
      setCurrent(breakPomo);

      settings.setPomoMinutes(1);
      settings.setPomosBeforeLongBreak(1);

      pomodoroStateMachine.handleAction(OptionDialogModel.Choice.OK);

      Assert.assertEquals(PomodoroType.POMO, PomodoroStateMachine.getCurrentType());
   }

   @Test
   public void testHandleNextForBreakWithCancel() throws Exception
   {
      Pomodoro breakPomo = new Pomodoro(5, PomodoroType.BREAK);
      setCurrent(breakPomo);

      pomodoroStateMachine.handleAction(OptionDialogModel.Choice.CANCEL);

      Assert.assertEquals(PomodoroType.WAIT, PomodoroStateMachine.getCurrentType());
   }

   @Test
   public void testHandleActionForWait() throws Exception
   {
      Pomodoro waitPomo = new Pomodoro(0, PomodoroType.WAIT);
      setCurrent(waitPomo);

      pomodoroStateMachine.handleAction(OptionDialogModel.Choice.CANCEL);

      Assert.assertEquals(PomodoroType.WAIT, PomodoroStateMachine.getCurrentType());
   }

   @Test
   public void testStopCurrent() throws Exception
   {
      Pomodoro pomo = new Pomodoro(1, PomodoroType.POMO);
      setCurrent(pomo);

      pomodoroStateMachine.stopCurrent();

      Assert.assertEquals(PomodoroType.WAIT, PomodoroStateMachine.getCurrentType());
   }

   @Test
   public void testReset() throws Exception
   {
      Field pomosDone = PomodoroStateMachine.class.getDeclaredField("pomosDone");
      pomosDone.setAccessible(true);
      pomosDone.set(pomodoroStateMachine, 2);

      pomodoroStateMachine.reset();
      Pomodoro wait = PomodoroStateMachine.getCurrent();
      Assert.assertEquals(0, wait.getPomosDone());
      Assert.assertEquals(PomodoroType.WAIT, wait.getType());
   }

}
