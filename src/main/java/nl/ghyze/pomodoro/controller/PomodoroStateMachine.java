package nl.ghyze.pomodoro.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.optiondialog.OptionDialogModel;
import nl.ghyze.pomodoro.type.PomodoroType;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;

public class PomodoroStateMachine
{

   private Pomodoro current;
   private Settings settings;
   private int pomosDone = 0;
   private AbstractSystemTrayManager systemTrayManager;

   private Date lastAction = new Date();

   private List<PomodoroHook> pomodoroHooks = new ArrayList<PomodoroHook>();

   public PomodoroStateMachine(Settings settings)
   {
      this.settings = settings;
      current = new Pomodoro(0, PomodoroType.WAIT);
   }

   public void setSystemTrayManager(AbstractSystemTrayManager systemTrayManager)
   {
      this.systemTrayManager = systemTrayManager;
   }

   public Pomodoro getCurrent()
   {
      return current;
   }

   public PomodoroType getCurrentType()
   {
      return current.getType();
   }

   public boolean shouldChangeState()
   {
      return (current.getType() != PomodoroType.WAIT && current.isDone());
   }

   public void handleAction(int choice)
   {
      lastAction = new Date();
      if (current.getType() == PomodoroType.POMO)
      {
         handleActionForPomo(choice);
      }
      else if (current.getType() == PomodoroType.BREAK)
      {
         handleActionForBreak(choice);
      }
   }

   private void handleActionForPomo(int choice)
   {
      if (choice == OptionDialogModel.SAVE)
      {
         pomosDone++;
         completeHooks();
      }
      else
      {
         cancelHooks();
      }
      Pomodoro next = createNextBreak();
      current = next;
      updateCurrent();
   }

   private void handleActionForBreak(int choice)
   {
      if (choice == OptionDialogModel.OK)
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

   public void stopCurrent()
   {
      cancelHooks();
      startWait();
   }

   public void startWait()
   {
      current = new Pomodoro(0, PomodoroType.WAIT);
      updateCurrent();
   }

   public void startPomo()
   {
      lastAction = new Date();
      current = new Pomodoro(settings.getPomoMinutes(), PomodoroType.POMO);
      updateCurrent();
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

   public Date getLastAction()
   {
      return new Date(lastAction.getTime());
   }

   public void addPomodoroHook(PomodoroHook hook)
   {
      pomodoroHooks.add(hook);
   }

   public List<PomodoroHook> getPomodoroHooks()
   {
      return pomodoroHooks;
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
}
