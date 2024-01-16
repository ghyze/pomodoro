package nl.ghyze.pomodoro.optiondialog;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import nl.ghyze.pomodoro.controller.PomodoroStateMachine;

public class PomoOptionDialogCallbackTest extends EasyMockSupport
{

   private PomodoroStateMachine stateMachineMock;

   @Before
   public void before()
   {
      stateMachineMock = EasyMock.createMock(PomodoroStateMachine.class);
   }

   @Test
   public void testOk()
   {
      PomoOptionDialogCallback callback = new PomoOptionDialogCallback(stateMachineMock);
      stateMachineMock.handleAction(OptionDialogModel.Choice.CANCEL);
      replayAll();
      callback.ok();
      verifyAll();
   }
}
