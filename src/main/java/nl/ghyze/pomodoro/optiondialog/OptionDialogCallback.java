package nl.ghyze.pomodoro.optiondialog;

public interface OptionDialogCallback
{

   void ok();

   void cancel();

   void timeout();

   default void continueAction(){
      // Does nothing by default, but could be overridden.
   }
}
