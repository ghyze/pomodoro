package nl.ghyze.pomodoro;

import java.time.Instant;

public class Stopwatch {

    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int MILLISECONDS_PER_MINUTE = 60 * MILLISECONDS_PER_SECOND;

    private final long start;

    public Stopwatch(){
        start = Instant.now().toEpochMilli();
    }

    // Public constructor for testing
    public Stopwatch(final long startTimeMillis){
        start = startTimeMillis;
    }

    public long timePassedMillis(){
        return Instant.now().toEpochMilli() - start;
    }

    public int timePassedMinutes(){
        return (int) (timePassedMillis() / MILLISECONDS_PER_MINUTE);
    }

    public boolean isTimedOut(long millis){
        return timePassedMillis() > millis;
    }
}
