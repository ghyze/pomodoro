package nl.ghyze.pomodoro.view;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	JPopupMenu popup = new JPopupMenu();
	JMenuItem exit = new JMenuItem("Exit");
	JMenuItem hide = new JMenuItem("Hide");
	PomoPanel panel = new PomoPanel();
	PomoController controller;

	MultiScreenFactory multiScreenFactory = new MultiScreenFactory();

	public PomoFrame(final PomoController controller) {
		this.controller = controller;
		this.setAlwaysOnTop(true);
		this.setUndecorated(true);
		this.setType(Window.Type.UTILITY);
		this.add(panel);
		this.pack();

		this.setVisible(true);
		popup.add(exit);
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.stopProgram();
			}

		});

		popup.add(hide);
		hide.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}

		});

		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				} else if (e.getButton() == 1) {
					PomoButton button = panel.buttonClicked(e);
					if (button != null) {
						button.executeAction();
					}
				}
			}
		});
	}

	public void update(Pomodoro countdown) {
		panel.update(countdown);
		this.repaint();
	}

	public void position(Settings settings) {
		Settings.Position position = settings.getPosition();
		Screen screen = multiScreenFactory.getSelectedScreen(settings);

		Point mostBottomRightPoint = screen.getMostBottomRightPoint();

		Point graphicsDeviceOffset = screen.getGraphicsDeviceOffset();

		switch (position) {
		case BOTTOM_RIGHT:
			int x = graphicsDeviceOffset.x + mostBottomRightPoint.x - this.getWidth();
			int y = graphicsDeviceOffset.y + mostBottomRightPoint.y - this.getHeight();
			this.setLocation(x, y);
			break;
		case BOTTOM_LEFT:
			this.setLocation(graphicsDeviceOffset.x + 0,
					graphicsDeviceOffset.y + mostBottomRightPoint.y - this.getHeight());
			break;
		case TOP_LEFT:
			this.setLocation(graphicsDeviceOffset.x + 0, graphicsDeviceOffset.y + 0);
			break;
		case TOP_RIGHT:
			this.setLocation(graphicsDeviceOffset.x + graphicsDeviceOffset.y + mostBottomRightPoint.x - this.getWidth(),
					0);
			break;
		default:

		}
	}
	
	public void addButton(PomoButton button){
		panel.addButton(button);
	}

}
