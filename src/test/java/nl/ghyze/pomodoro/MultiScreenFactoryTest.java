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

import nl.ghyze.settings.Settings;
import nl.ghyze.pomodoro.view.Screen;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class MultiScreenFactoryTest {

	private MultiScreenFactory factory;
	private GraphicsDevice graphicsDevice1;
	private GraphicsDevice graphicsDevice2;

	@Before
	public void setup(){
		graphicsDevice1 = createGraphicsDeviceMock(0, 0, 800, 600);
		graphicsDevice2 = createGraphicsDeviceMock(800, 150, 1024, 768);
		GraphicsDevice[] devices = new GraphicsDevice[]{graphicsDevice1, graphicsDevice2};
		factory = new MultiScreenFactory();
		factory.setAvailableGraphicsDevices(devices);
		factory.setToolkit(new TestToolkit());
	}
	
	@Test
	public void testGetAvailableScreenList(){
		
		List<Screen> screenList = factory.getAvailableScreenList();
		
		assertEquals(2, screenList.size());
		assertEquals("Screen: 1 (800, 600)", screenList.get(0).toString());
		assertEquals("Screen: 2 (1024, 768)", screenList.get(1).toString());
		EasyMock.verify(graphicsDevice1);
		EasyMock.verify(graphicsDevice2);
	}
	
	@Test
	public void testGetDefaultGraphicsDeviceOffset(){
		Settings settings = new Settings();
		settings.setScreenIndex(0);
		
		Point offset = factory.getSelectedScreen(settings).getGraphicsDeviceOffset();
		
		assertEquals(new Point(0,0), offset);
	}
	
	@Test
	public void testGetOtherGraphicsDeviceOffset(){
		Settings settings = new Settings();
		settings.setScreenIndex(1);
		
		Point offset = factory.getSelectedScreen(settings).getGraphicsDeviceOffset();
		
		assertEquals(new Point(800,150), offset);
	}
	
	@Test
	public void testGetOtherMostBottomRightPoint(){
		
		Settings settings = new Settings();
		settings.setScreenIndex(1);
		
		Point point = factory.getSelectedScreen(settings).getMostBottomRightPoint();
		assertEquals(new Point(800+1024,150+768), point);
	}

	private GraphicsDevice createGraphicsDeviceMock(int x, int y, int width, int height) {
		GraphicsDevice graphicsDevice = EasyMock.createMock(GraphicsDevice.class);
		DisplayMode displayMode = new DisplayMode(width, height, 24, 60);
		EasyMock.expect(graphicsDevice.getDisplayMode()).andReturn(displayMode);
		
		MockGraphicsConfiguration configuration = new MockGraphicsConfiguration();
		configuration.bounds = new Rectangle(x, y, width, height);
		EasyMock.expect(graphicsDevice.getDefaultConfiguration()).andReturn(configuration).anyTimes();
		
		EasyMock.replay(graphicsDevice);
		return graphicsDevice;
	}
	
	class MockGraphicsConfiguration extends GraphicsConfiguration{

		Rectangle bounds;
		
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
