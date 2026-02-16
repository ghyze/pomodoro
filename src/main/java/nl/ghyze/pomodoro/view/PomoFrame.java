package nl.ghyze.pomodoro.view;

import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import nl.ghyze.pomodoro.controller.PomoAction;
import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.settings.Settings;

public class PomoFrame extends JFrame {
    /**
     * <code>serialVersionUID</code> indicates/is used for.
     */
    private static final long serialVersionUID = 4110240101894844582L;
    private final JPopupMenu popup = new JPopupMenu();
    private final PomoPanel panel = new PomoPanel();

    public PomoFrame(final PomoAction action) {
        this.setAlwaysOnTop(true);
        this.setUndecorated(true);
        this.setType(Window.Type.UTILITY);
        this.add(panel);
        this.pack();

        this.setVisible(true);
        JMenuItem exit = new JMenuItem("Exit");
        popup.add(exit);
        exit.addActionListener(actionEvent -> action.execute());

        JMenuItem hide = new JMenuItem("Hide");
        popup.add(hide);
        hide.addActionListener(actionEvent -> setVisible(false));

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                } else if (e.getButton() == 1) {
                    panel.buttonClicked(e)
                            .ifPresent(PomoButton::executeAction);
                }
            }
        });
    }

    public void update(Pomodoro countdown) {
        panel.update(countdown);
        this.repaint();
    }

    public void position(Settings settings) {
        FrameLocation frameLocation = new FrameLocation(settings, this.getSize());
        this.setLocation(frameLocation.getLocation());
    }

    public void addButton(PomoButton button) {
        panel.addButton(button);
    }

}
