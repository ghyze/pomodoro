package nl.ghyze.pomodoro.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Pomodoro {
   
   public static enum Type {
      POMO, BREAK, WAIT;
   }

   private int minutes = 0;
   private long startTime;
   
   private Type type;
   
   private int pomosDone = 0;
   private int maxDone = 0;
   
   public Pomodoro(int minutes, Type type){
      this.minutes = minutes;
      this.type = type;
      startTime = System.currentTimeMillis();
   }
   
   public int minutesLeft(){
      long millisPassed = System.currentTimeMillis() - startTime;
      int minutesPassed = (int) millisPassed / 60000;
      return Math.max(0, minutes - minutesPassed-1);
   }
   
   public int secondsOfMinuteLeft(){
      long millisPassed = System.currentTimeMillis() - startTime;
      int secondsPassed = (int) millisPassed % 60000;
      return Math.max(0, 59-(secondsPassed/1000));
   }
   
   public boolean isDone(){
      long stopTime = startTime + (minutes * 60000l);
      return stopTime <= System.currentTimeMillis();
   }
   
   public Type getType(){
      return type;
   }

   public int getPomosDone()
   {
      return pomosDone;
   }

   public void setPomosDone(int pomosDone)
   {
      this.pomosDone = pomosDone;
   }
   
   public int getMaxPomosDone(){
      return Math.max(pomosDone, maxDone);
   }
   
   public void setMaxPomosDone(int maxDone){
      this.maxDone = maxDone;
   }
   
   public long getMillisSinceStart(){
      return System.currentTimeMillis() - startTime;
   }
   
   public boolean equals(Object other){
       if (other instanceof Pomodoro){
	   Pomodoro otherPomo = (Pomodoro) other;
	   if (otherPomo.getType() != type){
	       return false;
	   }
	   
	   if (otherPomo.minutes != minutes){
	       return false;
	   }
	   
	   return true;
       }
       return false;
   }
   
   @Override
   public int hashCode(){
       HashCodeBuilder builder = new HashCodeBuilder(17,31);
       builder.append(type);
       builder.append(minutes);
       return builder.hashCode();
   }
}
