package nl.ghyze.pomodoro.optiondialog;

import lombok.RequiredArgsConstructor;
import nl.ghyze.pomodoro.statemachine.PomodoroStateMachine;

@RequiredArgsConstructor
public class ResetOptionDialogCallback implements OptionDialogCallback
{
   private final PomodoroStateMachine stateMachine;

   @Override
   public void ok()
   {
      stateMachine.reset();
   }

   @Override
   public void cancel()
   {
   }

   @Override
   public void timeout()
   {
   }

}
