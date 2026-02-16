package nl.ghyze.pomodoro.view.menu;

import java.awt.MenuItem;
import java.awt.PopupMenu;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.settings.SettingsRepository;
import nl.ghyze.tasks.TaskFrame;
import nl.ghyze.settings.SettingsFrame;

public class MenuController {

    private PomoController controller;
    private TaskFrame taskFrame;
    private SettingsRepository settingsRepository;

    public MenuController() {
    }

    public void setPomoController(PomoController controller) {
        this.controller = controller;
    }

    public void setTaskFrame(TaskFrame taskFrame) {
        this.taskFrame = taskFrame;
    }

    public void setSettingsRepository(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public PopupMenu createPopupMenu() {
        PopupMenu menu = new PopupMenu();
        MenuItem show = createShowMenuItem();
        menu.add(show);

        MenuItem settings = createSettingsMenuItem();
        menu.add(settings);

        MenuItem tasks = createTaskMenuItem();
        menu.add(tasks);

        MenuItem exit = createExitMenuItem();
        menu.add(exit);

        MenuItem reset = createResetMenuItem();
        menu.add(reset);

        return menu;
    }

    private MenuItem createShowMenuItem() {
        MenuItem show = new MenuItem("Show Frame");
        show.addActionListener(actionEvent -> {
            if (controller != null) {
                controller.showFrame();
            }
        });
        return show;
    }

    private MenuItem createSettingsMenuItem() {
        MenuItem settings = new MenuItem("Settings");
        settings.addActionListener(actionEvent -> {
            if (controller != null && settingsRepository != null) {
                SettingsFrame settingsFrame = new SettingsFrame(controller.getSettings(), settingsRepository);
                settingsFrame.setVisible(true);
            }
        });
        return settings;
    }

    private MenuItem createExitMenuItem() {
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(actionEvent -> {
            if (controller != null) {
                controller.stopProgram();
            }
        });
        return exit;
    }

    private MenuItem createResetMenuItem() {
        MenuItem reset = new MenuItem("Reset");
        reset.addActionListener(actionEvent -> {
            if (controller != null) {
                controller.reset();
            }
        });
        return reset;
    }

    private MenuItem createTaskMenuItem() {
        MenuItem tasks = new MenuItem("Tasks");
        tasks.addActionListener(actionEvent -> {
            if (taskFrame != null) {
                taskFrame.setVisible(true);
            }
        });
        return tasks;
    }
}
