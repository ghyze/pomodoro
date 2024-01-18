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
      settings = EasyMock.createMock(Settings.class);
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
      setupStartPomo();
      EasyMock.replay(settings);
      pomodoroStateMachine.startPomo();
      EasyMock.verify(settings);

      Pomodoro current = PomodoroStateMachine.getCurrent();
      Assert.assertEquals(current.getType(), PomodoroType.POMO);

      Assert.assertTrue(isDateCorrect(pomodoroStateMachine.getLastAction()));
   }

   private void setupStartPomo()
   {
      EasyMock.expect(settings.getPomoMinutes()).andReturn(1);
      EasyMock.expect(settings.getPomosBeforeLongBreak()).andReturn(1);
   }

   @Test
   public void testShouldChangeState() throws Exception
   {
      Assert.assertFalse(pomodoroStateMachine.shouldChangeState());

      Pomodoro pomodoroMock = EasyMock.createMock(Pomodoro.class);
      EasyMock.expect(pomodoroMock.getType()).andReturn(PomodoroType.POMO);
      EasyMock.expect(pomodoroMock.isDone()).andReturn(false);
      setCurrent(pomodoroMock);
      EasyMock.replay(pomodoroMock);
      Assert.assertFalse(pomodoroStateMachine.shouldChangeState());
      EasyMock.verify(pomodoroMock);

      EasyMock.reset(pomodoroMock);

      EasyMock.expect(pomodoroMock.getType()).andReturn(PomodoroType.POMO);
      EasyMock.expect(pomodoroMock.isDone()).andReturn(true);
      EasyMock.replay(pomodoroMock);
      Assert.assertTrue(pomodoroStateMachine.shouldChangeState());
      EasyMock.verify(pomodoroMock);

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

      EasyMock.expect(settings.getPomosBeforeLongBreak()).andReturn(1).times(2);
      Assert.assertTrue(isDateCorrect(pomodoroStateMachine.getLastAction()));
   }

   // TODO: inline this method.
   private boolean isDateCorrect(Stopwatch stopwatch)
   {
      return stopwatch.timePassedMillis() <= 100l;
   }

   @Test
   public void testGetNextFromPomoWithDiscard() throws Exception
   {
      Pomodoro pomodoro = new Pomodoro(1, PomodoroType.BREAK);
      setCurrent(pomodoro);
      systemTrayManager = EasyMock.createMock(AbstractSystemTrayManager.class);
      pomodoroStateMachine.setSystemTrayManager(systemTrayManager);

      EasyMock.expect(settings.getPomosBeforeLongBreak()).andReturn(1);
      Assert.assertTrue(isDateCorrect(pomodoroStateMachine.getLastAction()));

      EasyMock.replay(systemTrayManager, settings);
      pomodoroStateMachine.handleAction(OptionDialogModel.Choice.DISCARD);
      EasyMock.verify(systemTrayManager, settings);

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

      EasyMock.expect(settings.getLongBreakMinutes()).andReturn(5);

      EasyMock.replay(systemTrayManager, settings);
      pomodoroStateMachine.handleAction(OptionDialogModel.Choice.SAVE);
      EasyMock.verify(systemTrayManager, settings);

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

      EasyMock.expect(settings.getPomoMinutes()).andReturn(1);
      EasyMock.expect(settings.getPomosBeforeLongBreak()).andReturn(1);

      EasyMock.replay(settings);
      pomodoroStateMachine.handleAction(OptionDialogModel.Choice.OK);
      EasyMock.verify(settings);

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
