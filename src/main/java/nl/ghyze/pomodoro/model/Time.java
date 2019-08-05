package nl.ghyze.pomodoro.model;

import java.util.Date;

public class Time {
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int MILLISECONDS_PER_MINUTE = 60 * MILLISECONDS_PER_SECOND;

    public static int minutesSince(long millisSinceLastAction) {
       Date now = new Date();
       long millis = (now.getTime() - millisSinceLastAction);
       return (int) (millis / MILLISECONDS_PER_MINUTE);
    }
}
