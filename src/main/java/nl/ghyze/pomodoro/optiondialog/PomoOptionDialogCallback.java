package nl.ghyze.pomodoro.optiondialog;

import nl.ghyze.pomodoro.controller.PomodoroStateMachine;

public class PomoOptionDialogCallback implements OptionDialogCallback
{

   private final PomodoroStateMachine stateMachine;

   public PomoOptionDialogCallback(final PomodoroStateMachine stateMachine)
   {
      this.stateMachine = stateMachine;
   }

   @Override
   public void ok()
   {
      stateMachine.handleAction(OptionDialogModel.Choice.SAVE);
   }

   @Override
   public void cancel()
   {
      stateMachine.handleAction(OptionDialogModel.Choice.DISCARD);
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
