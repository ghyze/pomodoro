package nl.ghyze.pomodoro.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.ghyze.pomodoro.Stopwatch;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class Pomodoro {

    @EqualsAndHashCode.Include
    private int minutes;
    private long startTime;
    private Stopwatch stopwatch;

    @EqualsAndHashCode.Include
    private PomodoroType type;

    private int pomosDone = 0;
    private int maxDone = 0;

    public Pomodoro(final int minutes, final PomodoroType type) {
        this.minutes = minutes;
        this.type = type;
        startTime = System.currentTimeMillis();
        stopwatch = new Stopwatch();
    }

    // Public constructor for testing with custom timing
    public Pomodoro(final int minutes, final PomodoroType type, final long startTime, final Stopwatch stopwatch) {
        this.minutes = minutes;
        this.type = type;
        this.startTime = startTime;
        this.stopwatch = stopwatch;
    }

    public int minutesLeft() {
        final int minutesPassed = stopwatch.timePassedMinutes();
        return Math.max(0, minutes - minutesPassed - 1);
    }

    int secondsOfMinuteLeft() {
        final int secondsPassed = (int) stopwatch.timePassedMillis() % Stopwatch.MILLISECONDS_PER_MINUTE;
        return Math.max(0, 59 - (secondsPassed / Stopwatch.MILLISECONDS_PER_SECOND));
    }

    public boolean isDone() {
        return stopwatch.isTimedOut(minutes * Stopwatch.MILLISECONDS_PER_MINUTE);
    }

    public int getMaxPomosDone() {
        return Math.max(pomosDone, maxDone);
    }

    public void setMaxPomosDone(final int maxDone) {
        this.maxDone = maxDone;
    }

    long getMillisSinceStart() {
        return System.currentTimeMillis() - startTime;
    }

    private String formatTimeLeft() {
        final int secondsLeft = secondsOfMinuteLeft();
        return minutesLeft() + ":" + (secondsLeft < 10 ? "0" : "") + secondsLeft;
    }

    public String getText() {
        if (type.isWait()) {
            return "Waiting for next";
        } else {
            return formatTimeLeft();
        }
    }
}
