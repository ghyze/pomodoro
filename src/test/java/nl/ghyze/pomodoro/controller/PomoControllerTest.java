package nl.ghyze.pomodoro.controller;

import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.PomodoroType;
import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.optiondialog.OptionDialogController;
import nl.ghyze.pomodoro.statemachine.PomodoroStateMachine;
import nl.ghyze.pomodoro.view.PomoFrame;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;

import static org.junit.Assert.*;

public class PomoControllerTest {

    private PomoController controller;
    private PomoFrame mockFrame;
    private Settings settings;
    private AbstractSystemTrayManager mockSystemTrayManager;
    private PomodoroStateMachine stateMachine;
    private OptionDialogController dialogController;

    @Before
    public void setUp() {
        controller = new PomoController();
        mockFrame = EasyMock.createMock(PomoFrame.class);
        settings = Settings.builder()
                .pomoMinutes(25)
                .shortBreakMinutes(5)
                .longBreakMinutes(15)
                .pomosBeforeLongBreak(3)
                .idleTime(60)
                .autoreset(false)
                .build();
        mockSystemTrayManager = EasyMock.createMock(AbstractSystemTrayManager.class);
        stateMachine = new PomodoroStateMachine(settings);
        dialogController = new OptionDialogController(mockFrame);
    }

    @Test
    public void testInitialize() {
        // Setup expectations
        mockFrame.position(settings);
        EasyMock.expectLastCall();
        mockSystemTrayManager.setPomoController(controller);
        EasyMock.expectLastCall();

        EasyMock.replay(mockFrame, mockSystemTrayManager);

        // Execute
        controller.initialize(mockFrame, settings, mockSystemTrayManager, stateMachine, dialogController);

        // Verify
        assertNotNull(controller.getSettings());
        assertEquals(settings, controller.getSettings());
        EasyMock.verify(mockFrame, mockSystemTrayManager);
    }

    @Test
    public void testStartPomo() {
        // Initialize controller
        mockFrame.position(settings);
        mockSystemTrayManager.setPomoController(controller);
        EasyMock.replay(mockFrame, mockSystemTrayManager);
        controller.initialize(mockFrame, settings, mockSystemTrayManager, stateMachine, dialogController);
        EasyMock.reset(mockFrame, mockSystemTrayManager);

        // Execute
        controller.startPomo();

        // Verify state changed
        assertEquals(PomodoroType.POMO, stateMachine.getCurrentPomodoroType());
        assertEquals(25, stateMachine.getCurrentPomodoro().getMinutes());
    }

    @Test
    public void testStopCurrent() {
        // Initialize and start a pomodoro
        mockFrame.position(settings);
        mockSystemTrayManager.setPomoController(controller);
        EasyMock.replay(mockFrame, mockSystemTrayManager);
        controller.initialize(mockFrame, settings, mockSystemTrayManager, stateMachine, dialogController);
        controller.startPomo();
        EasyMock.reset(mockFrame, mockSystemTrayManager);

        // Execute
        controller.stopCurrent();

        // Verify state changed to WAIT
        assertEquals(PomodoroType.WAIT, stateMachine.getCurrentPomodoroType());
    }

    @Test
    public void testActionPerformed_UpdatesFrameAndSystemTray() {
        // Initialize controller
        mockFrame.position(settings);
        mockSystemTrayManager.setPomoController(controller);
        EasyMock.replay(mockFrame, mockSystemTrayManager);
        controller.initialize(mockFrame, settings, mockSystemTrayManager, stateMachine, dialogController);
        EasyMock.reset(mockFrame, mockSystemTrayManager);

        // Start a pomodoro
        controller.startPomo();

        // Setup expectations for update
        Pomodoro currentPomo = stateMachine.getCurrentPomodoro();
        mockFrame.update(currentPomo);
        EasyMock.expectLastCall();
        mockSystemTrayManager.update(currentPomo);
        EasyMock.expectLastCall();

        EasyMock.replay(mockFrame, mockSystemTrayManager);

        // Execute action (simulate timer tick)
        ActionEvent event = new ActionEvent(this, 0, "test");
        controller.actionPerformed(event);

        // Verify
        EasyMock.verify(mockFrame, mockSystemTrayManager);
    }

    @Test
    public void testOnChange_UpdatesSettings() {
        // Initialize controller
        mockFrame.position(settings);
        mockSystemTrayManager.setPomoController(controller);
        EasyMock.replay(mockFrame, mockSystemTrayManager);
        controller.initialize(mockFrame, settings, mockSystemTrayManager, stateMachine, dialogController);
        EasyMock.reset(mockFrame, mockSystemTrayManager);

        // Create new settings
        Settings newSettings = Settings.builder()
                .pomoMinutes(30)
                .shortBreakMinutes(10)
                .longBreakMinutes(20)
                .pomosBeforeLongBreak(4)
                .idleTime(90)
                .autoreset(true)
                .build();

        // Setup expectations
        mockFrame.position(newSettings);
        EasyMock.expectLastCall();
        EasyMock.replay(mockFrame);

        // Execute
        controller.onChange(newSettings);

        // Verify
        assertEquals(newSettings, controller.getSettings());
        EasyMock.verify(mockFrame);
    }

    @Test
    public void testShowFrame() {
        // Initialize controller
        mockFrame.position(settings);
        mockSystemTrayManager.setPomoController(controller);
        EasyMock.replay(mockFrame, mockSystemTrayManager);
        controller.initialize(mockFrame, settings, mockSystemTrayManager, stateMachine, dialogController);
        EasyMock.reset(mockFrame, mockSystemTrayManager);

        // Setup expectations
        mockFrame.setVisible(true);
        EasyMock.expectLastCall();
        EasyMock.replay(mockFrame);

        // Execute
        controller.showFrame();

        // Verify
        EasyMock.verify(mockFrame);
    }
}
