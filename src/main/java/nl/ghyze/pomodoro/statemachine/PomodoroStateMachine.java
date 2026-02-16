package nl.ghyze.pomodoro.statemachine;

import nl.ghyze.pomodoro.Stopwatch;
import nl.ghyze.pomodoro.controller.PomodoroHook;
import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.PomodoroType;
import nl.ghyze.settings.Settings;
import nl.ghyze.pomodoro.optiondialog.OptionDialogModel;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;

import java.util.logging.Logger;

/**
 * Manages pomodoro state transitions.
 * Coordinates between HookManager, MessageService, and BreakCalculator.
 */
public class PomodoroStateMachine {
    private static final Logger logger = Logger.getLogger(PomodoroStateMachine.class.getName());
    private Pomodoro current;
    private final Settings settings;
    private int pomosDone = 0;

    private Stopwatch lastAction = new Stopwatch();

    // Delegated responsibilities
    private final HookManager hookManager;
    private final MessageService messageService;
    private final BreakCalculator breakCalculator;

    public PomodoroStateMachine(final Settings settings) {
        this.settings = settings;
        this.current = new Pomodoro(0, PomodoroType.WAIT);

        // Initialize components
        this.hookManager = new HookManager();
        this.messageService = new MessageService();
        this.breakCalculator = new BreakCalculator(settings, messageService);
    }

    public Pomodoro getCurrentPomodoro() {
        return current;
    }

    public PomodoroType getCurrentPomodoroType() {
        return current.getType();
    }

    /**
     * Sets the system tray manager for message display.
     * @param systemTrayManager The system tray manager
     */
    public void setSystemTrayManager(final AbstractSystemTrayManager systemTrayManager) {
        messageService.setSystemTrayManager(systemTrayManager);
    }

    /**
     * Adds a hook to receive pomodoro lifecycle notifications.
     * @param hook The hook to add
     */
    public void addPomodoroHook(final PomodoroHook hook) {
        hookManager.addHook(hook);
    }

    public boolean shouldChangeState() {
        return (!getCurrentPomodoroType().isWait() && current.isDone());
    }

    public void handleAction(final OptionDialogModel.Choice choice) {
        logger.fine("OptionDialog choice: " + choice);
        lastAction = new Stopwatch();
        switch (getCurrentPomodoroType()) {
            case POMO -> handleActionForPomo(choice);
            case BREAK -> handleActionForBreak(choice);
        }
    }

    private void handleActionForPomo(final OptionDialogModel.Choice choice) {
        switch (choice) {
            case SAVE -> {
                pomosDone++;
                hookManager.notifyCompleted();
                transitionToBreak();
            }
            case CANCEL -> {
                hookManager.notifyCanceled();
                transitionToBreak();
            }
            case CONTINUE_ACTION -> {
                pomosDone++;
                hookManager.notifyCompleted();
                startPomo();
            }
        }
        updateCurrent();
    }

    private void handleActionForBreak(final OptionDialogModel.Choice choice) {
        if (choice == OptionDialogModel.Choice.OK) {
            startPomo();
        } else {
            startWait();
        }
    }

    private void transitionToBreak() {
        lastAction = new Stopwatch();
        final BreakCalculator.BreakResult result = breakCalculator.calculateNextBreak(pomosDone);
        current = result.breakPomodoro;
        pomosDone = result.newPomosDone;
    }

    public void stopCurrent() {
        hookManager.notifyCanceled();
        startWait();
    }

    public void startWait() {
        current = new Pomodoro(0, PomodoroType.WAIT);
        updateCurrent();
    }

    public void startPomo() {
        lastAction = new Stopwatch();
        current = new Pomodoro(settings.getPomoMinutes(), PomodoroType.POMO);
        updateCurrent();
        hookManager.notifyStarted();
        messageService.showPomodoroStartMessage(pomosDone + 1);
    }

    public void updateCurrent() {
        current.setPomosDone(pomosDone);
        current.setMaxPomosDone(settings.getPomosBeforeLongBreak());
    }

    public void reset() {
        if (pomosDone > 0) {
            pomosDone = 0;
            startWait();
        }
    }

    public Stopwatch getLastAction() {
        return lastAction;
    }

    // Package-private for testing
    void setCurrent(final Pomodoro current) {
        this.current = current;
    }

    // Package-private for testing
    void setPomosDone(final int pomosDone) {
        this.pomosDone = pomosDone;
    }
}
