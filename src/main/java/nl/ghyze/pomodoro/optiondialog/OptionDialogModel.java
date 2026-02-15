package nl.ghyze.pomodoro.optiondialog;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

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

   public Optional<Object> getDefaultChoice()
   {
      if (choices != null && choices.length > 0)
      {
         return Optional.of(choices[0]);
      }
      return Optional.empty();
   }
}
