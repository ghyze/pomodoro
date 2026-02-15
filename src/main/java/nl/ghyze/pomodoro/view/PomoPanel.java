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
import java.util.Optional;

import javax.swing.JPanel;

import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.PomodoroType;

public class PomoPanel extends JPanel {

	/**
	 * <code>serialVersionUID</code> indicates/is used for.
	 */
	private static final long serialVersionUID = 7646898602993436935L;

	// Window dimensions
	private static final int WINDOW_WIDTH = 140;
	private static final int WINDOW_HEIGHT = 100;

	// Pomodoro indicator drawing constants
	private static final int INDICATOR_LEFT_MARGIN = 2;
	private static final int INDICATOR_TOP_MARGIN = 2;
	private static final int INDICATOR_SPACING = 15;
	private static final int INDICATOR_SIZE = 12;
	private static final int FILLED_INDICATOR_MARGIN = 3;
	private static final int FILLED_INDICATOR_TOP = 5;
	private static final int FILLED_INDICATOR_SIZE = 7;

	private Pomodoro countdown = null;

	private final List<PomoButton> buttons = new ArrayList<>();

	PomoPanel() {
		this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
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
				int xOff = INDICATOR_LEFT_MARGIN + (i * INDICATOR_SPACING);
				gr.drawRect(xOff, INDICATOR_TOP_MARGIN, INDICATOR_SIZE, INDICATOR_SIZE);
				if (i < countdown.getPomosDone()) {
					gr.fillRect(xOff + FILLED_INDICATOR_MARGIN, FILLED_INDICATOR_TOP,
							FILLED_INDICATOR_SIZE, FILLED_INDICATOR_SIZE);
				}
			}
		}
	}

	private void drawButtons(Graphics gr) {
		if (countdown != null) {
			buttons.stream()
					.filter(button -> button.isVisible(countdown))
					.forEach(button -> gr.drawImage(button.getImage(), button.getX(), button.getY(), null));
		}
	}

	Optional<PomoButton> buttonClicked(MouseEvent e) {
		return buttons.stream()
				.filter(button -> button.isVisible(countdown))
				.filter(button -> button.containsPoint(e.getPoint()))
				.findAny();
	}

	void update(Pomodoro countdown) {
		this.countdown = countdown;
	}

	void addButton(PomoButton button) {
		buttons.add(button);
	}
}
