package nl.ghyze.pomodoro.optiondialog;

public class OptionDialogModel
{
   public enum Choice {
      SAVE,
      DISCARD,
      CONTINUE_ACTION,

      OK,
      CANCEL
   }

   private String title;
   private String message;
   private Object[] choices;

   public static final int SAVE = 0;
   public static final int DISCARD = 1;
   public static final int CONTINUE_ACTION = 2;

   public static final int OK = 0;
   public static final int CANCEL = 1;

   public OptionDialogModel()
   {

   }

   public void setTitle(String title)
   {
      this.title = title;
   }

   public String getTitle()
   {
      return title;
   }

   public void setMessage(String message)
   {
      this.message = message;
   }

   public String getMessage()
   {
      return message;
   }

   public void setChoices(Object[] choices)
   {
      this.choices = choices;
   }

   public Object[] getChoices()
   {
      return choices;
   }

   public Object getDefaultChoice()
   {
      if (choices.length > 0)
      {
         return choices[0];
      }
      return null;
   }
}
