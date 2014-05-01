package nl.ghyze.pomodoro.model;

import java.util.ArrayList;
import java.util.prefs.Preferences;

public class Settings
{
   public static enum Position {
      TOP_LEFT,
      TOP_RIGHT,
      BOTTOM_LEFT,
      BOTTOM_RIGHT
   }
   
   public static final String KEY_POSITION="position";
   public static final String KEY_POMO_MINUTES="pomoMinutes";
   public static final String KEY_SHORT_BREAK_MINUTES="shortBreakNMinutes";
   public static final String KEY_LONG_BREAK_MINUTES="longBreakMinutes";
   public static final String KEY_POMOS_BEFORE_LONG_BREAK="pomosBeforeLongBreak";
   
   private Position position;
   
   private int pomoMinutes;
   private int shortBreakMinutes;
   private int longBreakMinutes;
   private int pomosBeforeLongBreak;
   
   private ArrayList<SettingsChangeListener> listeners = new ArrayList<SettingsChangeListener>();
   
   public Position getPosition(){
      return position;
   }
   
   public void setPosition(Position position){
      this.position = position;
      notifyListeners();
   }

   public int getPomoMinutes(){
      return pomoMinutes;
   }
   
   public void setPomoMinutes(int minutes){
      this.pomoMinutes = minutes;
   }
   
   public int getShortBreakMinutes(){
      return shortBreakMinutes;
   }
   
   public void setShortBreakMinutes(int minutes){
      this.shortBreakMinutes = minutes;
   }

   public int getLongBreakMinutes(){
      return longBreakMinutes;
   }
   
   public void setLongBreakMinutes(int minutes){
      this.longBreakMinutes = minutes;
   }
   
   public int getPomosBeforeLongBreak(){
      return pomosBeforeLongBreak;
   }
   
   public void setPomosBeforeLongBreak(int number){
      this.pomosBeforeLongBreak = number;
   }
   
   
   private void notifyListeners(){
      for (SettingsChangeListener listener : listeners){
         listener.onChange(this);
      }
   }
   
   public void addListener(SettingsChangeListener listener){
      listeners.add(listener);
   }
   
   public void removeListener(SettingsChangeListener listener){
      listeners.remove(listener);
   }
   
   public void save(){
      Preferences prefs = Preferences.userNodeForPackage(this.getClass());
      
      prefs.putInt(KEY_POMO_MINUTES, pomoMinutes);
      prefs.putInt(KEY_SHORT_BREAK_MINUTES, shortBreakMinutes);
      prefs.putInt(KEY_LONG_BREAK_MINUTES, longBreakMinutes);
      prefs.putInt(KEY_POMOS_BEFORE_LONG_BREAK, pomosBeforeLongBreak);
      
      prefs.putInt(KEY_POSITION, position.ordinal());
   }
   
   public void load(){
      Preferences prefs = Preferences.userNodeForPackage(this.getClass());
      
      pomoMinutes = prefs.getInt(KEY_POMO_MINUTES, 25);
      shortBreakMinutes = prefs.getInt(KEY_SHORT_BREAK_MINUTES, 5);
      longBreakMinutes = prefs.getInt(KEY_LONG_BREAK_MINUTES, 15);
      pomosBeforeLongBreak = prefs.getInt(KEY_POMOS_BEFORE_LONG_BREAK, 3);
      
      position = Position.values()[prefs.getInt(KEY_POSITION, 3)];
      
   }
}
