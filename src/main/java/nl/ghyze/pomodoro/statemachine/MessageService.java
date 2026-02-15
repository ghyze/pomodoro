package nl.ghyze.pomodoro.statemachine;

import lombok.Setter;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;

/**
 * Manages message display for pomodoro lifecycle events.
 * Responsible for creating and displaying messages via the system tray.
 */
public class MessageService {
    @Setter
    private AbstractSystemTrayManager systemTrayManager;

    /**
     * Shows a message when a short break starts.
     */
    public void showShortBreakMessage() {
        showMessage("Well done! Short break");
    }

    /**
     * Shows a message when a long break starts.
     */
    public void showLongBreakMessage() {
        showMessage("Well done! Long break");
    }

    /**
     * Shows a message when a pomodoro starts.
     * @param pomoNumber The number of the pomodoro being started (1-based)
     */
    public void showPomodoroStartMessage(final int pomoNumber) {
        showMessage("Starting Pomodoro number " + pomoNumber);
    }

    private void showMessage(final String message) {
        if (systemTrayManager != null) {
            systemTrayManager.message(message);
        }
    }
}
