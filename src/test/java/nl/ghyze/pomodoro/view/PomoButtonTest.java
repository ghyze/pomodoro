package nl.ghyze.pomodoro.view;

import java.awt.Point;
import java.awt.image.BufferedImage;

import nl.ghyze.pomodoro.controller.PomoAction;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.PomodoroType;

public class PomoButtonTest {

    PomoButton button;
    Pomodoro pomodoro;
    
    @Before
    public void setUp(){
	button = new PomoButton(0,0,10,10);
	pomodoro = new Pomodoro(5, PomodoroType.BREAK);
    }
    
    @Test
    public void testContainsPoint(){
	Point inside = new Point(5,5);
	Point outside = new Point(10,10);
	
	Assert.assertTrue(button.containsPoint(inside));
	Assert.assertFalse(button.containsPoint(outside));
    }
    
    @Test
    public void testIsVisible(){
	button.addVisibleType(PomodoroType.POMO);
	Assert.assertFalse(button.isVisible(pomodoro));

	button.addVisibleType(PomodoroType.BREAK);
	Assert.assertTrue(button.isVisible(pomodoro));
	
    }
    
    @Test
    public void testExecuteAction(){
	button.executeAction();
	
	IMocksControl control = EasyMock.createControl();
	PomoAction action = control.createMock(PomoAction.class);
	action.execute();
	EasyMock.expectLastCall();
	button.setAction(action);
	EasyMock.replay(action);
	
	button.executeAction();
	
	EasyMock.verify(action);
    }
    
    @Test
    public void testImage(){
	BufferedImage image = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
	button.setImage(image);
	Assert.assertSame(image, button.getImage());
    }
    
    @Test
    public void testCoordinates(){
	Assert.assertEquals(0, button.getX());
	Assert.assertEquals(0, button.getY());
    }
}
