package nl.ghyze.pomodoro.optiondialog;

import nl.ghyze.pomodoro.model.Pomodoro.Type;

public class OptionDialogModelFactory
{
   public static OptionDialogModel createChangeStateModel(Type type)
   {
      OptionDialogModel model = new OptionDialogModel();
      model.setTitle(getDialogTitle(type));
      model.setMessage(getDialogMessage(type));
      model.setChoices(getDialogChoices(type));
      return model;
   }

   public static OptionDialogModel createResetModel()
   {
      OptionDialogModel model = new OptionDialogModel();

      model.setTitle("Reset");
      model.setMessage("Resetting will set the current pomos done to 0, and set the status to Waiting. Proceed?");
      model.setChoices(okCancelOptions());

      return model;
   }

   private static String getDialogMessage(Type type)
   {
      if (Type.POMO == type)
      {
         return "Pomodoro finished. What would you like to do with this one?";
      }
      else if (Type.BREAK == type)
      {
         return "Ready to start next one?";
      }
      return "";
   }

   private static String getDialogTitle(Type type)
   {
      if (Type.POMO == type)
      {
         return "Pomodoro finished";
      }
      else if (Type.BREAK == type)
      {
         return "Break finished";
      }
      return "";
   }

   private static Object[] getDialogChoices(Type type)
   {
      if (Type.POMO == type)
      {
         return new Object[] { "Save", "Discard" };
      }
      else if (Type.BREAK == type)
      {
         return okCancelOptions();
      }
      return new String[] {};
   }

   private static Object[] okCancelOptions()
   {
      return new Object[] { "Ok", "Cancel" };
   }

}
