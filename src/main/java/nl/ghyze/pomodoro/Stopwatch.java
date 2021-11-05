package nl.ghyze.pomodoro;

import java.util.Date;

public class Stopwatch {

    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int MILLISECONDS_PER_MINUTE = 60 * MILLISECONDS_PER_SECOND;

    private final long start;

    public Stopwatch(){
        start = new Date().getTime();
    }

    public long timePassedMillis(){
        return new Date().getTime() - start;
    }

    public int timePassedMinutes(){
        return (int) (timePassedMillis() / MILLISECONDS_PER_MINUTE);
    }

    public boolean isTimedOut(long millis){
        return timePassedMillis() > millis;
    }
}
