package nl.ghyze.pomodoro.statemachine;

import java.util.ArrayList;
import java.util.List;

import nl.ghyze.pomodoro.controller.PomodoroHook;

/**
 * Manages pomodoro lifecycle hooks.
 * Responsible for registering hooks and notifying them of lifecycle events.
 */
public class HookManager {
    private final List<PomodoroHook> hooks = new ArrayList<>();

    /**
     * Registers a new hook to receive lifecycle notifications.
     * @param hook The hook to register
     */
    public void addHook(final PomodoroHook hook) {
        hooks.add(hook);
    }

    /**
     * Notifies all hooks that a pomodoro has started.
     */
    public void notifyStarted() {
        hooks.forEach(PomodoroHook::started);
    }

    /**
     * Notifies all hooks that a pomodoro has been completed and saved.
     */
    public void notifyCompleted() {
        hooks.forEach(PomodoroHook::completed);
    }

    /**
     * Notifies all hooks that a pomodoro has been canceled/discarded.
     */
    public void notifyCanceled() {
        hooks.forEach(PomodoroHook::canceled);
    }
}
