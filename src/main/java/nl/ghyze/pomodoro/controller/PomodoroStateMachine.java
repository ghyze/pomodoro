package nl.ghyze.pomodoro.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
   private AbstractSystemTrayManager systemTrayManager;

   private Date lastAction = new Date();

   private final List<PomodoroHook> pomodoroHooks = new ArrayList<>();

   public PomodoroStateMachine(Settings settings)
   {
      this.settings = settings;
      current = new Pomodoro(0, PomodoroType.WAIT);
   }

   public void setSystemTrayManager(AbstractSystemTrayManager systemTrayManager)
   {
      this.systemTrayManager = systemTrayManager;
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
      lastAction = new Date();
      if (getCurrentType().isPomo())
      {
         handleActionForPomo(choice);
      }
      else if (getCurrentType().isBreak())
      {
         handleActionForBreak(choice);
      }
   }

   private void handleActionForPomo(OptionDialogModel.Choice choice)
   {
      if (choice == OptionDialogModel.Choice.SAVE)
      {
         pomosDone++;
         completeHooks();
         current = createNextBreak();
      }
      else if (choice == OptionDialogModel.Choice.CANCEL)
      {
         cancelHooks();
         current = createNextBreak();
      }
      else if (choice == OptionDialogModel.Choice.CONTINUE_ACTION){
         pomosDone ++;
         completeHooks();
         startPomo();
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
      lastAction = new Date();
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
      lastAction = new Date();
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
      pomosDone = 0;
      startWait();
   }

   Date getLastAction()
   {
      return new Date(lastAction.getTime());
   }

   public void addPomodoroHook(PomodoroHook hook)
   {
      pomodoroHooks.add(hook);
   }

   private void completeHooks()
   {
      for (PomodoroHook hook : pomodoroHooks)
      {
         hook.completed();
      }
   }

   private void cancelHooks()
   {
      for (PomodoroHook hook : pomodoroHooks)
      {
         hook.canceled();
      }
   }
   
   private void startHooks()
   {
      for (PomodoroHook hook : pomodoroHooks)
      {
         hook.started();
      }
   }
}
