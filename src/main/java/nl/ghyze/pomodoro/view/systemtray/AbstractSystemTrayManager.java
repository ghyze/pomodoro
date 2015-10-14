package nl.ghyze.pomodoro.view.systemtray;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.view.SettingsFrame;

public abstract class AbstractSystemTrayManager {

    protected Image[] pomoMinutes;
    private Image[] breakMinutes;
    private Image waitImage;
    protected TrayIcon icon;
    private PomoController controller;

    public void setPomoController(PomoController controller) {
        this.controller = controller;
    }
    
    protected PopupMenu createPopupMenu() {
        PopupMenu menu = new PopupMenu();
        MenuItem show = createShowMenuItem();
        menu.add(show);
        
        MenuItem settings = createSettingsMenuItem();
        menu.add(settings);
        
        MenuItem exit = createExitMenuItem();
        menu.add(exit);
        return menu;
    }
    
    protected MenuItem createShowMenuItem() {
        MenuItem show = new MenuItem("Show Frame");
        show.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (controller != null) {
                    controller.showFrame();
                }
            }

        });
        return show;
    }
    
    protected MenuItem createSettingsMenuItem() {
        MenuItem settings = new MenuItem("Settings");
        settings.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (controller != null) {
                    SettingsFrame settingsFrame = new SettingsFrame(controller.getSettings());
                    settingsFrame.setVisible(true);
                }
            }
            
        });
        return settings;
    }
    
    protected MenuItem createExitMenuItem() {
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (controller != null) {
                    controller.stopProgram();
                }
            }

        });
        return exit;
    }
    
    protected void initializeImages() {
        createPomoMinutesImages();
        createBreakMinutesImages();
        createWaitImage();
    }

    protected void createBreakMinutesImages() {
        breakMinutes = new Image[100];
        for (int i = 0; i < 100; i++) {
            breakMinutes[i] = createImage(new Color(0, 192, 0), "" + i);
        }
    }

    protected void createPomoMinutesImages() {
        pomoMinutes = new Image[100];
        for (int i = 0; i < 100; i++) {
            pomoMinutes[i] = createImage(Color.red, "" + i);
        }
    }

    protected void createWaitImage() {
        waitImage = createImage(Color.BLUE, "...");
    }

    private Image createImage(Color color, String number) {
        Dimension iconsize = getTrayIconSize();
        Image image = new BufferedImage(iconsize.width, iconsize.height, BufferedImage.TYPE_INT_RGB);
        Graphics gr = image.getGraphics();
        gr.setColor(color);
        gr.fillRect(0, 0, iconsize.width, iconsize.height);
        gr.setColor(Color.white);

        FontMetrics fm = gr.getFontMetrics();
        Rectangle2D bounds = fm.getStringBounds(number, gr);

        gr.drawString(number, (int) (iconsize.width - bounds.getWidth()) / 2,
                (int) (iconsize.height + bounds.getHeight()) / 2);
        return image;
    }

    protected abstract Dimension getTrayIconSize();

    public void stop() {
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(icon);
    }

    public void update(Pomodoro countdown) {
        if (countdown.getType() == Pomodoro.Type.WAIT) {
            icon.setImage(waitImage);
        } else if (countdown.getType() == Pomodoro.Type.POMO) {
            icon.setImage(pomoMinutes[countdown.minutesLeft()]);
        } else if (countdown.getType() == Pomodoro.Type.BREAK) {
            icon.setImage(breakMinutes[countdown.minutesLeft()]);
        }
    }

    public void message(String message) {
        icon.displayMessage("Pomodoro", message, MessageType.INFO);
    }

}
