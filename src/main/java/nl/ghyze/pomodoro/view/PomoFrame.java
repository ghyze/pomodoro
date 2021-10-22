package nl.ghyze.pomodoro.view;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import nl.ghyze.pomodoro.MultiScreenFactory;
import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.Settings;

public class PomoFrame extends JFrame {
    /**
     * <code>serialVersionUID</code> indicates/is used for.
     */
    private static final long serialVersionUID = 4110240101894844582L;
    private JPopupMenu popup = new JPopupMenu();
    private PomoPanel panel = new PomoPanel();

    private MultiScreenFactory multiScreenFactory = new MultiScreenFactory();

    public PomoFrame(final PomoController controller) {
        this.setAlwaysOnTop(true);
        this.setUndecorated(true);
        this.setType(Window.Type.UTILITY);
        this.add(panel);
        this.pack();

        this.setVisible(true);
        JMenuItem exit = new JMenuItem("Exit");
        popup.add(exit);
        exit.addActionListener(actionEvent -> controller.stopProgram());

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
