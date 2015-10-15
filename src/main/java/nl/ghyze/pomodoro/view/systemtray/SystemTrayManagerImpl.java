package nl.ghyze.pomodoro.view.systemtray;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

public class SystemTrayManagerImpl extends AbstractSystemTrayManager {

    public SystemTrayManagerImpl() {
        initializeImages();
        PopupMenu menu = createPopupMenu();
        initTrayIcon(menu);
    }

    protected Dimension getTrayIconSize() {
        SystemTray tray = SystemTray.getSystemTray();
        Dimension iconsize = tray.getTrayIconSize();
        return iconsize;
    }

    protected void initTrayIcon(PopupMenu menu) {
        SystemTray tray = SystemTray.getSystemTray();
        icon = new TrayIcon(waitImage, "Pomo", menu);
        try {
            tray.add(icon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

}
