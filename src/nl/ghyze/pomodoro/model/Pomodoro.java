package nl.ghyze.pomodoro.model;

public class Pomodoro {
   
   public enum Type {
      POMO, BREAK, WAIT;
   }

   private int minutes = 0;
   private long startTime;
   
   private Type type;
   
   private int pomosDone = 0;
   
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
      if (millisPassed > startTime + (minutes * 60000)){
         return 0;
      }
      int secondsPassed = (int) millisPassed % 60000;
      
      return Math.max(0, 59-(secondsPassed/1000));
   }
   
   public boolean isDone(){
      long stopTime = startTime + (minutes * 60000l);
      return stopTime < System.currentTimeMillis();
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
}
