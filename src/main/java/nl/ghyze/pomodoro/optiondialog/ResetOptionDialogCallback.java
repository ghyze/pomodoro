package nl.ghyze.pomodoro.optiondialog;

import nl.ghyze.pomodoro.controller.PomodoroStateMachine;

public class ResetOptionDialogCallback implements OptionDialogCallback
{
   private PomodoroStateMachine stateMachine;

   public ResetOptionDialogCallback(PomodoroStateMachine stateMachine)
   {
      this.stateMachine = stateMachine;
   }

   @Override
   public void ok()
   {
      stateMachine.reset();
   }

   @Override
   public void cancel()
   {
      ;
   }

   @Override
   public void timeout()
   {
      ;
   }

}
