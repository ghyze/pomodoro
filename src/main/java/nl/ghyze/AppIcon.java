package nl.ghyze;

import java.awt.Image;
import javax.swing.ImageIcon;

public class AppIcon {
    public static final Image ICON = load();

    private static Image load() {
        var url = AppIcon.class.getResource("/pomodoro.png");
        return url != null ? new ImageIcon(url).getImage() : null;
    }
}
