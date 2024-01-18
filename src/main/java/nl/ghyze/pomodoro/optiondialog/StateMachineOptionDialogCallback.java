package nl.ghyze.pomodoro.optiondialog;

import nl.ghyze.pomodoro.controller.PomodoroStateMachine;

public class StateMachineOptionDialogCallback implements OptionDialogCallback
{

   private final PomodoroStateMachine stateMachine;

   public StateMachineOptionDialogCallback(PomodoroStateMachine stateMachine)
   {
      this.stateMachine = stateMachine;
   }

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

}
