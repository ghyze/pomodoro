package nl.ghyze.pomodoro.optiondialog;

public class OptionDialogModel
{
   public enum Choice {
      SAVE("Save"),
      DISCARD("Discard"),
      CONTINUE_ACTION("Continue"),

      OK("Ok"),
      CANCEL("Cancel");

      private String message;

      Choice(String message){
         this.message = message;
      }

      public String toString(){
         return this.message;
      }
   }

   private String title;
   private String message;
   private Object[] choices;


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
