package nl.ghyze.pomodoro.controller;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import nl.ghyze.pomodoro.Stopwatch;
import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.optiondialog.OptionDialogModel;
import nl.ghyze.pomodoro.model.PomodoroType;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;

public class PomodoroStateMachine
{

   private static Pomodoro current;
   private final Settings settings;
   private int pomosDone = 0;
   @Setter
   private AbstractSystemTrayManager systemTrayManager;

   private Stopwatch lastAction = new Stopwatch();

   private final List<PomodoroHook> pomodoroHooks = new ArrayList<>();

   public PomodoroStateMachine(Settings settings)
   {
      this.settings = settings;
      current = new Pomodoro(0, PomodoroType.WAIT);
   }

   static Pomodoro getCurrent()
   {
      return current;
   }

   static PomodoroType getCurrentType()
   {
      return current.getType();
   }

   boolean shouldChangeState()
   {
      return (!getCurrentType().isWait() && current.isDone());
   }

   public void handleAction(OptionDialogModel.Choice choice)
   {
      System.out.println("OptionDialog choice: "+choice);
      lastAction = new Stopwatch();
      switch (getCurrentType()){
         case POMO -> handleActionForPomo(choice);
         case BREAK -> handleActionForBreak(choice);
      }
   }

   private void handleActionForPomo(OptionDialogModel.Choice choice)
   {
      switch (choice){
         case SAVE -> {
            pomosDone++;
            completeHooks();
            current = createNextBreak();
         }
         case CANCEL -> {
            cancelHooks();
            current = createNextBreak();
         }
         case CONTINUE_ACTION -> {
            pomosDone++;
            completeHooks();
            startPomo();
         }
      }
      updateCurrent();
   }

   private void handleActionForBreak(OptionDialogModel.Choice choice)
   {
      if (choice ==  OptionDialogModel.Choice.OK)
      {
         startPomo();
      }
      else
      {
         startWait();
      }
   }

   private Pomodoro createNextBreak()
   {
      lastAction = new Stopwatch();
      if (pomosDone < settings.getPomosBeforeLongBreak())
      {
         return createShortBreak();
      }
      else
      {
         return createLongBreak();
      }
   }

   private Pomodoro createShortBreak()
   {
      String message = "Well done! Short break";
      showMessage(message);
      return new Pomodoro(settings.getShortBreakMinutes(), PomodoroType.BREAK);
   }

   private Pomodoro createLongBreak()
   {
      pomosDone = 0;
      String message = "Well done! Long break";
      showMessage(message);
      return new Pomodoro(settings.getLongBreakMinutes(), PomodoroType.BREAK);
   }

   void stopCurrent()
   {
      cancelHooks();
      startWait();
   }

   public void startWait()
   {
      current = new Pomodoro(0, PomodoroType.WAIT);
      updateCurrent();
   }

   void startPomo()
   {
      lastAction = new Stopwatch();
      current = new Pomodoro(settings.getPomoMinutes(), PomodoroType.POMO);
      updateCurrent();
      startHooks();
      String message = "Starting Pomodoro number " + (pomosDone + 1);
      showMessage(message);
   }

   private void showMessage(String message)
   {
      if (systemTrayManager != null)
      {
         systemTrayManager.message(message);
      }
   }

   public void updateCurrent()
   {
      current.setPomosDone(pomosDone);
      current.setMaxPomosDone(settings.getPomosBeforeLongBreak());
   }

   public void reset()
   {
      if (pomosDone > 0) {
         pomosDone = 0;
         startWait();
      }
   }

   Stopwatch getLastAction()
   {
      return lastAction;
   }

   public void addPomodoroHook(PomodoroHook hook)
   {
      pomodoroHooks.add(hook);
   }

   private void completeHooks()
   {
      pomodoroHooks.forEach(PomodoroHook::completed);
   }

   private void cancelHooks()
   {
      pomodoroHooks.forEach(PomodoroHook::canceled);
   }
   
   private void startHooks()
   {
      pomodoroHooks.forEach(PomodoroHook::started);
   }
}
