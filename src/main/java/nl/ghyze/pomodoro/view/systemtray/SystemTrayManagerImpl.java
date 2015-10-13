package nl.ghyze.pomodoro.view.systemtray;

import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;

public class SystemTrayManagerImpl extends AbstractSystemTrayManager {

    public SystemTrayManagerImpl() {
	createPomoMinutesImages();
	createBreakMinutesImages();
	createWaitImage();

	PopupMenu menu = new PopupMenu();
	MenuItem show = createShowMenuItem();
	menu.add(show);

	MenuItem settings = createSettingsMenuItem();
	menu.add(settings);

	MenuItem exit = createExitMenuItem();
	menu.add(exit);

	initTrayIcon(menu);

    }
	
	protected Dimension getTrayIconSize() {
		SystemTray tray = SystemTray.getSystemTray();
		Dimension iconsize = tray.getTrayIconSize();
		return iconsize;
	}

}
