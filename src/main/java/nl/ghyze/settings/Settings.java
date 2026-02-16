package nl.ghyze.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Settings
{
   public enum Position
   {
      TOP_LEFT(Vertical.TOP, Horizontal.LEFT),
      TOP_RIGHT(Vertical.TOP, Horizontal.RIGHT),
      BOTTOM_LEFT(Vertical.BOTTOM, Horizontal.LEFT),
      BOTTOM_RIGHT(Vertical.BOTTOM, Horizontal.RIGHT);

      public final Vertical vertical;
      public final Horizontal horizontal;

      Position(Vertical vertical, Horizontal horizontal){
         this.vertical = vertical;
         this.horizontal = horizontal;
      }

      public enum Vertical {
         TOP, BOTTOM
      }

      public enum Horizontal {
         LEFT, RIGHT
      }
   }

   private static final String KEY_POSITION = "position";
   private static final String KEY_POMO_MINUTES = "pomoMinutes";
   private static final String KEY_SHORT_BREAK_MINUTES = "shortBreakNMinutes";
   private static final String KEY_LONG_BREAK_MINUTES = "longBreakMinutes";
   private static final String KEY_POMOS_BEFORE_LONG_BREAK = "pomosBeforeLongBreak";
   private static final String KEY_AUTORESET = "autoReset";
   private static final String KEY_IDLE_TIME = "idleTime";
   private static final String KEY_SCREEN_INDEX = "screenIndex";

   private int screenIndex;
   private Position position;

   private int pomoMinutes;
   private int shortBreakMinutes;
   private int longBreakMinutes;
   private int pomosBeforeLongBreak;
   private int idleTime;
   private boolean autoreset;

   @Builder.Default
   private ArrayList<SettingsChangeListener> listeners = new ArrayList<>();

   public void setPosition(final Position position)
   {
      this.position = position;
      notifyListeners();
   }

   private void notifyListeners()
   {
      for (final SettingsChangeListener listener : listeners)
      {
         listener.onChange(this);
      }
   }

   public void addListener(final SettingsChangeListener listener)
   {
      listeners.add(listener);
   }

   public void removeListener(final SettingsChangeListener listener)
   {
      listeners.remove(listener);
   }
}
