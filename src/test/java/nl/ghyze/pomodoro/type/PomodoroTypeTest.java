package nl.ghyze.pomodoro.type;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class PomodoroTypeTest {

	@Test
	public void testGetBackgroundColor(){
		assertEquals(PomodoroType.POMO.getBackgroundColor(), Color.RED);
		assertEquals(PomodoroType.WAIT.getBackgroundColor(), Color.BLUE);
		assertEquals(PomodoroType.BREAK.getBackgroundColor(), new Color(0, 192, 0));
	}
	
	@Test
	public void testGetFontSize(){
		assertEquals(PomodoroType.POMO.getFontSize(), 30f, 0.001f);
		assertEquals(PomodoroType.WAIT.getFontSize(), 16f, 0.001f);
		assertEquals(PomodoroType.BREAK.getFontSize(), 30f, 0.001f);
	}
}
