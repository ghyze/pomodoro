package nl.ghyze.pomodoro.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.PomodoroType;

public class PomoPanel extends JPanel {

	/**
	 * <code>serialVersionUID</code> indicates/is used for.
	 */
	private static final long serialVersionUID = 7646898602993436935L;

	private Pomodoro countdown = null;

	private final List<PomoButton> buttons = new ArrayList<>();

	PomoPanel() {
		this.setPreferredSize(new Dimension(140, 100));
		this.setLayout(null);
	}

	protected void paintComponent(Graphics gr) {
		if (countdown != null) {
			PomodoroType type = countdown.getType();
			paintBackground(gr, type.getBackgroundColor());
			float fontSize = type.getFontSize();
			paintText(gr, countdown.getText(), fontSize);
			drawButtons(gr);
			drawPomosDone(gr);
		}

	}

	private void paintText(Graphics gr, String text, float size) {
		Font originalFont = gr.getFont();
		gr.setColor(Color.white);
		Font bigFont = originalFont.deriveFont(size);
		gr.setFont(bigFont);
		paintText(text, gr);
		gr.setFont(originalFont);
	}

	private void paintBackground(Graphics gr, Color backgroundColor) {
		gr.setColor(backgroundColor);
		gr.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	private void paintText(String text, Graphics gr) {
		FontMetrics fm = gr.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(text, gr);
		int x = (int) (this.getWidth() - rect.getWidth()) / 2;
		int y = (int) (this.getHeight() + rect.getHeight()) / 2;
		gr.drawString(text, x, y);
	}

	private void drawPomosDone(Graphics gr) {
		if (countdown != null) {
			gr.setColor(Color.white);
			for (int i = 0; i < countdown.getMaxPomosDone(); i++) {
				int xOff = 2 + (i * 15);
				gr.drawRect(xOff, 2, 12, 12);
				if (i < countdown.getPomosDone()) {
					gr.fillRect(xOff + 3, 5, 7, 7);
				}
			}
		}
	}

	private void drawButtons(Graphics gr) {
		if (countdown != null) {
			for (PomoButton button : buttons) {
				if (button.isVisible(countdown)) {
					gr.drawImage(button.getImage(), button.getX(), button.getY(), null);
				}
			}
		}
	}

	PomoButton buttonClicked(MouseEvent e) {
		for (PomoButton button : buttons) {
			if (button.isVisible(countdown) && button.containsPoint(e.getPoint())) {
				return button;
			}
		}
		return null;
	}

	void update(Pomodoro countdown) {
		this.countdown = countdown;
	}

	void addButton(PomoButton button) {
		buttons.add(button);
	}
}
