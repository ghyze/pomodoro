package nl.ghyze.pomodoro.optiondialog;

import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Test;

import nl.ghyze.pomodoro.controller.PomodoroStateMachine;

public class StateMachineOptionDialogCallbackTest extends EasyMockSupport
{

   @Mock
   PomodoroStateMachine stateMachine;

   @Test
   public void testOk() throws Exception
   {
      StateMachineOptionDialogCallback callback = new StateMachineOptionDialogCallback(stateMachine);
      stateMachine.handleAction(0);
      replayAll();
      callback.ok();
      verifyAll();
   }
}
