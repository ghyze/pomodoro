package nl.ghyze.statistics;

import nl.ghyze.pomodoro.model.PomodoroType;

import java.time.Instant;

/**
 * Immutable record representing a pomodoro state change event.
 * Used for logging pomodoro lifecycle to CSV files.
 */
public record PomodoroEvent(
        Instant timestamp,
        String eventType,      // started, completed, canceled, break_ended
        PomodoroType fromState,
        PomodoroType toState,
        int pomoCount
) {
    /**
     * Creates a pomodoro started event.
     */
    public static PomodoroEvent started(PomodoroType fromState, int pomoCount) {
        return new PomodoroEvent(Instant.now(), "started", fromState, PomodoroType.POMO, pomoCount);
    }

    /**
     * Creates a pomodoro completed event.
     */
    public static PomodoroEvent completed(int pomoCount) {
        return new PomodoroEvent(Instant.now(), "completed", PomodoroType.POMO, PomodoroType.BREAK, pomoCount);
    }

    /**
     * Creates a pomodoro canceled event.
     */
    public static PomodoroEvent canceled(int pomoCount) {
        return new PomodoroEvent(Instant.now(), "canceled", PomodoroType.POMO, PomodoroType.BREAK, pomoCount);
    }

    /**
     * Creates a break ended event.
     */
    public static PomodoroEvent breakEnded(PomodoroType toState, int pomoCount) {
        return new PomodoroEvent(Instant.now(), "break_ended", PomodoroType.BREAK, toState, pomoCount);
    }
}
