package nl.ghyze.pomodoro.habitica;

public class HabitFilter implements HabiticaTaskFilter
{

   @Override
   public boolean accept(HabiticaTask task)
   {
      return task.getType().equalsIgnoreCase("habit");
   }

}
