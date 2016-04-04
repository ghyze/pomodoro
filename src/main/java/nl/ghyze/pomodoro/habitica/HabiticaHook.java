package nl.ghyze.pomodoro.habitica;

import nl.ghyze.pomodoro.controller.PomodoroHook;
import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.model.SettingsChangeListener;

public class HabiticaHook implements PomodoroHook, SettingsChangeListener
{

   private HabiticaService service;

   private Settings settings;

   public HabiticaHook(Settings settings)
   {
      init(settings);
   }

   private void init(Settings settings)
   {
      this.settings = settings;
      String userId = settings.getHabiticaUser();
      String apiToken = settings.getHabiticaApi();
      service = new HabiticaService(userId, apiToken);
   }

   @Override
   public void completed()
   {
      if (settings.isUseHabitica())
      {
         service.updateTask(settings.getHabiticaTaskId(), true);
      }

   }

   @Override
   public void canceled()
   {
      if (settings.isUseHabitica())
      {
         service.updateTask(settings.getHabiticaTaskId(), false);
      }

   }

   @Override
   public void onChange(Settings settings)
   {
      init(settings);
   }

}
