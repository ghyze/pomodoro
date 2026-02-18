package nl.ghyze.pomodoro.optiondialog;

import lombok.RequiredArgsConstructor;
import nl.ghyze.pomodoro.statemachine.PomodoroStateMachine;

@RequiredArgsConstructor
public class BreakOptionDialogCallback implements OptionDialogCallback
{
   private final PomodoroStateMachine stateMachine;

   @Override
   public void ok()
   {
      stateMachine.handleAction(OptionDialogModel.Choice.OK);
   }

   @Override
   public void cancel()
   {
      stateMachine.handleAction(OptionDialogModel.Choice.CANCEL);
   }

   @Override
   public void timeout()
   {
      stateMachine.startWait();
   }

   @Override
   public void continueAction() {
      stateMachine.handleAction(OptionDialogModel.Choice.CONTINUE_ACTION);
   }

}
