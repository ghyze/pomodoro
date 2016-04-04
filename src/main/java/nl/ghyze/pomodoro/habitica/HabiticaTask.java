package nl.ghyze.pomodoro.habitica;

public class HabiticaTask
{
   private String text;
   private String id;
   private String type;

   public String getText()
   {
      return text;
   }

   public String getId()
   {
      return id;
   }

   public String getType()
   {
      return type;
   }

   public String toDebugString()
   {
      return "HabiticaTask [ID: " + id + " ; text: " + text + " ; type: " + type + "]";
   }

   public String toString()
   {
      return text;
   }
}
