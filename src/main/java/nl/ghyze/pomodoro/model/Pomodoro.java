package nl.ghyze.pomodoro.model;

import lombok.Data;
import nl.ghyze.pomodoro.DateTimeUtil;
import nl.ghyze.pomodoro.type.PomodoroType;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Data
public class Pomodoro {

    private int minutes;
    private long startTime;

    private PomodoroType type;

    private int pomosDone = 0;
    private int maxDone = 0;

    public Pomodoro(int minutes, PomodoroType type) {
        this.minutes = minutes;
        this.type = type;
        startTime = System.currentTimeMillis();
    }

    public int minutesLeft() {
        long millisPassed = System.currentTimeMillis() - startTime;
        int minutesPassed = (int) millisPassed / DateTimeUtil.MILLISECONDS_PER_MINUTE;
        return Math.max(0, minutes - minutesPassed - 1);
    }

    int secondsOfMinuteLeft() {
        long millisPassed = System.currentTimeMillis() - startTime;
        int secondsPassed = (int) millisPassed % DateTimeUtil.MILLISECONDS_PER_MINUTE;
        return Math.max(0, 59 - (secondsPassed / DateTimeUtil.MILLISECONDS_PER_SECOND));
    }

    public boolean isDone() {
        long stopTime = startTime + (minutes * DateTimeUtil.MILLISECONDS_PER_MINUTE);
        return stopTime <= System.currentTimeMillis();
    }

    public int getMaxPomosDone() {
        return Math.max(pomosDone, maxDone);
    }

    public void setMaxPomosDone(int maxDone) {
        this.maxDone = maxDone;
    }

    long getMillisSinceStart() {
        return System.currentTimeMillis() - startTime;
    }

    public boolean equals(Object other) {
        if (other instanceof Pomodoro) {
            Pomodoro otherPomo = (Pomodoro) other;
            if (otherPomo.getType() != type) {
                return false;
            }

            return otherPomo.minutes == minutes;
        }
        return false;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 31);
        builder.append(type);
        builder.append(minutes);
        return builder.hashCode();
    }

    private String formatTimeLeft() {
        int secondsLeft = secondsOfMinuteLeft();
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
