package nl.ghyze.pomodoro;

import static org.junit.Assert.assertEquals;

import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.util.List;

import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.view.Screen;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class MultiScreenFactoryTest {

	MultiScreenFactory factory;
	GraphicsDevice graphicsDevice1;
	GraphicsDevice graphicsDevice2;
	GraphicsDevice[] devices;
	
	@Before
	public void setup(){
		graphicsDevice1 = createGraphicsDeviceMock(0, 0, 800, 600);
		graphicsDevice2 = createGraphicsDeviceMock(800, 150, 1024, 768);
		devices = new GraphicsDevice[]{graphicsDevice1, graphicsDevice2};
		factory = new MultiScreenFactory();
		factory.setAvailableGraphicsDevices(devices);
	}
	
	@Test
	public void testGetAvailableScreenList(){
		
		List<Screen> screenList = factory.getAvailableScreenList();
		
		assertEquals(2, screenList.size());
		assertEquals("Screen 1 (800, 600)", screenList.get(0).toString());
		assertEquals("Screen 2 (1024, 768)", screenList.get(1).toString());
		EasyMock.verify(graphicsDevice1);
		EasyMock.verify(graphicsDevice2);
	}
	
	@Test
	public void testGetDefaultGraphicsDeviceOffset(){
		Settings settings = new Settings();
		settings.setScreenIndex(0);
		
		Point offset = factory.getGraphicsDeviceOffset(settings);
		
		assertEquals(new Point(0,0), offset);
	}
	
	@Test
	public void testGetOtherGraphicsDeviceOffset(){
		Settings settings = new Settings();
		settings.setScreenIndex(1);
		
		Point offset = factory.getGraphicsDeviceOffset(settings);
		
		assertEquals(new Point(800,150), offset);
	}
	
	@Test
	public void testGetOtherMostBottomRightPoint(){
		
		Settings settings = new Settings();
		settings.setScreenIndex(1);
		
		Point point = factory.getMostBottomRightPoint(settings);
		
		assertEquals(new Point(1024,768), point);
	}

	private GraphicsDevice createGraphicsDeviceMock(int x, int y, int width, int height) {
		GraphicsDevice graphicsDevice = EasyMock.createMock(GraphicsDevice.class);
		DisplayMode displayMode = new DisplayMode(width, height, 24, 60);
		EasyMock.expect(graphicsDevice.getDisplayMode()).andReturn(displayMode);
		
		MockGraphicsCofiguration configuration = new MockGraphicsCofiguration();	
		configuration.bounds = new Rectangle(x, y, width, height);
		EasyMock.expect(graphicsDevice.getDefaultConfiguration()).andReturn(configuration).anyTimes();
		
		EasyMock.replay(graphicsDevice);
		return graphicsDevice;
	}
	
	class MockGraphicsCofiguration extends GraphicsConfiguration{

		public Rectangle bounds;
		
		@Override
		public GraphicsDevice getDevice() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ColorModel getColorModel() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ColorModel getColorModel(int transparency) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AffineTransform getDefaultTransform() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AffineTransform getNormalizingTransform() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Rectangle getBounds() {
			return bounds;
		}
		
	}
}
