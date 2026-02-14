package nl.ghyze.pomodoro.optiondialog;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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

   public Object getDefaultChoice()
   {
      if (choices != null && choices.length > 0)
      {
         return choices[0];
      }
      return null;
   }
}
