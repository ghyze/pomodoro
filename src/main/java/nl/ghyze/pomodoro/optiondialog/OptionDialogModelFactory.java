package nl.ghyze.pomodoro.optiondialog;

import nl.ghyze.pomodoro.model.Pomodoro.Type;

public class OptionDialogModelFactory
{

   private Type type;

   public OptionDialogModelFactory(Type type)
   {
      this.type = type;
   }

   public OptionDialogModel getOptions()
   {
      OptionDialogModel model = new OptionDialogModel();
      model.setTitle(getDialogTitle());
      model.setMessage(getDialogMessage());
      model.setChoices(getDialogChoices());
      return model;
   }

   private String getDialogMessage()
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

   private String getDialogTitle()
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

   private Object[] getDialogChoices()
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

   public static OptionDialogModel createResetModel()
   {
      OptionDialogModel model = new OptionDialogModel();

      model.setTitle("Reset");
      model.setMessage("Resetting will set the current pomos done to 0, and set the status to Waiting. Proceed?");
      model.setChoices(okCancelOptions());

      return model;
   }
}
