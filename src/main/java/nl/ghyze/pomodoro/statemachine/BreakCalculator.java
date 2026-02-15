package nl.ghyze.pomodoro.statemachine;

import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.PomodoroType;
import nl.ghyze.pomodoro.model.Settings;

/**
 * Calculates which type of break should be given based on completed pomodoros.
 * Responsible for determining short vs long break and creating the appropriate Pomodoro object.
 */
public class BreakCalculator {
    private final Settings settings;
    private final MessageService messageService;

    public BreakCalculator(final Settings settings, final MessageService messageService) {
        this.settings = settings;
        this.messageService = messageService;
    }

    /**
     * Creates the next break based on the number of completed pomodoros.
     * Returns a short break if fewer than the configured limit, otherwise a long break.
     *
     * @param pomosDone The number of pomodoros completed so far
     * @return A Pomodoro object representing the break, and the reset pomosDone counter
     */
    public BreakResult calculateNextBreak(final int pomosDone) {
        if (pomosDone < settings.getPomosBeforeLongBreak()) {
            return createShortBreak(pomosDone);
        } else {
            return createLongBreak();
        }
    }

    private BreakResult createShortBreak(final int pomosDone) {
        messageService.showShortBreakMessage();
        final Pomodoro breakPomo = new Pomodoro(settings.getShortBreakMinutes(), PomodoroType.BREAK);
        return new BreakResult(breakPomo, pomosDone); // Keep pomosDone unchanged
    }

    private BreakResult createLongBreak() {
        messageService.showLongBreakMessage();
        final Pomodoro breakPomo = new Pomodoro(settings.getLongBreakMinutes(), PomodoroType.BREAK);
        return new BreakResult(breakPomo, 0); // Reset pomosDone to 0
    }

    /**
     * Result of calculating the next break.
     * Contains the break Pomodoro and the updated pomosDone counter.
     */
    public static class BreakResult {
        public final Pomodoro breakPomodoro;
        public final int newPomosDone;

        public BreakResult(final Pomodoro breakPomodoro, final int newPomosDone) {
            this.breakPomodoro = breakPomodoro;
            this.newPomosDone = newPomosDone;
        }
    }
}
