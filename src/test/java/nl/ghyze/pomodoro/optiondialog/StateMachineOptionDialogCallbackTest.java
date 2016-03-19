package nl.ghyze.pomodoro.optiondialog;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import nl.ghyze.pomodoro.controller.PomodoroStateMachine;

public class StateMachineOptionDialogCallbackTest extends EasyMockSupport
{

   private PomodoroStateMachine stateMachineMock;

   @Before
   public void before()
   {
      stateMachineMock = EasyMock.createMock(PomodoroStateMachine.class);
   }

   @Test
   public void testOk() throws Exception
   {
      StateMachineOptionDialogCallback callback = new StateMachineOptionDialogCallback(stateMachineMock);
      stateMachineMock.handleAction(0);
      replayAll();
      callback.ok();
      verifyAll();
   }
}
