package nl.ghyze.pomodoro.view.systemtray;

import java.awt.AWTException;
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

public class SystemTrayManager {

    private static SystemTrayManager INSTANCE = new SystemTrayManager();

    private Image[] pomoMinutes;
    private Image[] breakMinutes;
    private Image waitImage;
    private TrayIcon icon;
    private PomoController controller;

    private SystemTrayManager() {
	createPomoMinutesImages();
	createBreakMinutesImages();
	waitImage = createWaitImage();

	PopupMenu menu = new PopupMenu();
	MenuItem show = createShowMenuItem();
	menu.add(show);

	MenuItem settings = createSettingsMenuItem();
	menu.add(settings);

	MenuItem exit = createExitMenuItem();
	menu.add(exit);

	initTrayIcon(menu);

    }

    private void initTrayIcon(PopupMenu menu) {
	SystemTray tray = SystemTray.getSystemTray();
	icon = new TrayIcon(pomoMinutes[0], "Pomo", menu);
	try {
	    tray.add(icon);
	} catch (AWTException e) {
	    e.printStackTrace();
	}
    }

    private MenuItem createExitMenuItem() {
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

    private MenuItem createSettingsMenuItem() {
	MenuItem settings = new MenuItem("Settings");
	settings.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		if (controller != null) {
		    SettingsFrame settingsFrame = new SettingsFrame(controller
			    .getSettings());
		    settingsFrame.setVisible(true);
		}
	    }

	});
	return settings;
    }

    private MenuItem createShowMenuItem() {
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

    private void createBreakMinutesImages() {
	breakMinutes = new Image[100];
	for (int i = 0; i < 100; i++) {
	    breakMinutes[i] = createImage(new Color(0, 192, 0), i);
	}
    }

    private void createPomoMinutesImages() {
	pomoMinutes = new Image[100];
	for (int i = 0; i < 100; i++) {
	    pomoMinutes[i] = createImage(Color.red, i);
	}
    }

    private Image createWaitImage() {
	SystemTray tray = SystemTray.getSystemTray();
	Dimension iconsize = tray.getTrayIconSize();
	Image image = new BufferedImage(iconsize.width, iconsize.height,
		BufferedImage.TYPE_INT_RGB);
	Graphics gr = image.getGraphics();
	gr.setColor(Color.blue);
	gr.fillRect(0, 0, iconsize.width, iconsize.height);
	gr.setColor(Color.white);
	FontMetrics fm = gr.getFontMetrics();
	Rectangle2D bounds = fm.getStringBounds("...", gr);

	gr.drawString("...", (int) (iconsize.width - bounds.getWidth()) / 2,
		(int) (iconsize.height + bounds.getHeight()) / 2);
	return image;
    }

    private Image createImage(Color color, int number) {
	SystemTray tray = SystemTray.getSystemTray();
	Dimension iconsize = tray.getTrayIconSize();
	Image image = new BufferedImage(iconsize.width, iconsize.height,
		BufferedImage.TYPE_INT_RGB);
	Graphics gr = image.getGraphics();
	gr.setColor(color);
	gr.fillRect(0, 0, iconsize.width, iconsize.height);
	gr.setColor(Color.white);

	FontMetrics fm = gr.getFontMetrics();
	Rectangle2D bounds = fm.getStringBounds("" + number, gr);

	gr.drawString("" + number,
		(int) (iconsize.width - bounds.getWidth()) / 2,
		(int) (iconsize.height + bounds.getHeight()) / 2);
	return image;
    }

    public static SystemTrayManager getInstance() {
	return INSTANCE;
    }

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

    public void setPomoController(PomoController controller) {
	this.controller = controller;
    }

    public void message(String message) {
	icon.displayMessage("Pomodoro", message, MessageType.INFO);
    }
}
