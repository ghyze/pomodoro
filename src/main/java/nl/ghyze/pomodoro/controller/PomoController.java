package nl.ghyze.pomodoro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.model.SettingsChangeListener;
import nl.ghyze.pomodoro.optiondialog.BreakOptionDialogCallback;
import nl.ghyze.pomodoro.optiondialog.OptionDialogCallback;
import nl.ghyze.pomodoro.optiondialog.OptionDialogController;
import nl.ghyze.pomodoro.optiondialog.OptionDialogModel;
import nl.ghyze.pomodoro.optiondialog.OptionDialogModelFactory;
import nl.ghyze.pomodoro.optiondialog.ResetOptionDialogCallback;
import nl.ghyze.pomodoro.optiondialog.PomoOptionDialogCallback;
import nl.ghyze.pomodoro.view.PomoFrame;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;

import static nl.ghyze.pomodoro.Stopwatch.MILLISECONDS_PER_MINUTE;

@NoArgsConstructor
public class PomoController implements ActionListener, SettingsChangeListener {
    private PomoFrame frame;

    @Getter
    private Settings settings;
    private PomodoroStateMachine stateMachine;
    private AbstractSystemTrayManager systemTrayManager;


    public void initialize(final PomoFrame frame, final Settings settings, final AbstractSystemTrayManager systemTrayManager, final PomodoroStateMachine stateMachine) {
        this.frame = frame;
        this.settings = settings;
        this.systemTrayManager = systemTrayManager;
        this.stateMachine = stateMachine;

        this.frame.position(settings);
        this.systemTrayManager.setPomoController(this);
        settings.addListener(this);
        OptionDialogController.init(frame);
        final Timer timer = new Timer(20, this);
        timer.start();
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if (stateMachine.shouldChangeState()) {
            final OptionDialogModel model = OptionDialogModelFactory.createChangeStateModel(PomodoroStateMachine.getCurrentType());
            OptionDialogController.showDialog(model, getCallback());
        }
        checkMinutesSinceLastAction();
        frame.update(PomodoroStateMachine.getCurrent());
        systemTrayManager.update(PomodoroStateMachine.getCurrent());
    }

    private OptionDialogCallback getCallback(){
      if (PomodoroStateMachine.getCurrentType().isPomo()) {
         return new PomoOptionDialogCallback(stateMachine);
      } else {
         return new BreakOptionDialogCallback(stateMachine);
      }
   }

   private void checkMinutesSinceLastAction(){
        if (stateMachine.getLastAction().isTimedOut((long) settings.getIdleTime() * MILLISECONDS_PER_MINUTE) &&
                PomodoroStateMachine.getCurrentType().isWait()) {
            stateMachine.reset();
        }
    }

    public void stopProgram() {
        systemTrayManager.stop();
        System.exit(0);
    }

    public void showFrame() {
        frame.setVisible(true);
    }

    @Override
    public void onChange(final Settings settings) {
        this.settings = settings;
        frame.position(settings);
    }

    public void stopCurrent() {
        stateMachine.stopCurrent();
    }

    public void startPomo() {
        stateMachine.startPomo();
    }

    public void reset() {
        final OptionDialogModel resetModel = OptionDialogModelFactory.createResetModel();
        OptionDialogController.showDialog(resetModel, new ResetOptionDialogCallback(stateMachine));
    }
}
