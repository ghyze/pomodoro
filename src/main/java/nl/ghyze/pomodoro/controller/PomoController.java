package nl.ghyze.pomodoro.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.model.SettingsChangeListener;
import nl.ghyze.pomodoro.view.PomoFrame;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;

public class PomoController implements ActionListener, SettingsChangeListener {
    private PomoFrame frame;
    private Timer timer;

    private Settings settings;
    private PomodoroStateMachine stateMachine;
    private AbstractSystemTrayManager systemTrayManager;

    public PomoController() {

    }

    public void initializeSystemTrayManager(AbstractSystemTrayManager systemTrayManager) {
        this.systemTrayManager = systemTrayManager;
        this.systemTrayManager.setPomoController(this);
    }

    public void initialize() {
        settings = new Settings();
        settings.addListener(this);
        settings.load();
        frame = new PomoFrame(this);
        frame.position(settings.getPosition());
        stateMachine = new PomodoroStateMachine(settings);
        stateMachine.setSystemTrayManager(this.systemTrayManager);
        stateMachine.updateCurrent();
        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (stateMachine.shouldChangeState()) {
            OptionDialogModelFactory factory = new OptionDialogModelFactory(stateMachine.getCurrentType());
            OptionDialogModel model = factory.getOptions();
            int choice = JOptionPane.showOptionDialog(frame, model.getMessage(), model.getTitle(),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, model.getChoices(),
                    model.getDefaultChoice());
            stateMachine.handleAction(choice);
        }
        frame.update(stateMachine.getCurrent());
        systemTrayManager.update(stateMachine.getCurrent());
    }

    public void stopProgram() {
        systemTrayManager.stop();
        System.exit(0);
    }

    public void showFrame() {
        frame.setVisible(true);
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public void onChange(Settings settings) {
        this.settings = settings;
        frame.position(settings.getPosition());
    }

    public void stopCurrent() {
        stateMachine.stopCurrent();
    }

    public void startPomo() {
        stateMachine.startPomo();
    }

}
