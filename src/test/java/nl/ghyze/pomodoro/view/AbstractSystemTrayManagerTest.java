package nl.ghyze.pomodoro.view;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.event.ActionListener;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;

public class AbstractSystemTrayManagerTest {

    private TestSystemTrayManager manager;
    
    @Before
    public void setUp(){
        manager = new TestSystemTrayManager();
    }
    
    @Test
    public void testCreatePopupMenu() throws Exception {
        PopupMenu menu = manager.getPopupMenu();
        assertEquals(3, menu.getItemCount());
        assertEquals("Show Frame",menu.getItem(0).getLabel());
        assertEquals("Settings",menu.getItem(1).getLabel());
        assertEquals("Exit",menu.getItem(2).getLabel());
    }
    
    @Test
    public void testInitializeImages() throws Exception {
        manager.doInitializeImages();
        assertEquals(100, manager.getPomoImages().length);
        checkImageArray(manager.getPomoImages());
        assertEquals(100, manager.getBreakImages().length);
        checkImageArray(manager.getBreakImages());
        assertNotNull(manager.getWaitImage());
        assertImageSize(manager.getWaitImage());
    }
    
    @Test
    public void testInvokeShowMenuAction() throws Exception {
        PomoController mockPomoController = EasyMock.createMock(PomoController.class);
        manager.setPomoController(mockPomoController);
        mockPomoController.showFrame();
        EasyMock.expectLastCall();
        EasyMock.replay(mockPomoController);
        
        PopupMenu menu = manager.getPopupMenu();
        ActionListener[] listeners = menu.getItem(0).getActionListeners();
        callActionListeners(listeners);
        EasyMock.verify(mockPomoController);
    }
    
    @Test
    public void testInvokeExitMenuAction() throws Exception {
        PomoController mockPomoController = EasyMock.createMock(PomoController.class);
        manager.setPomoController(mockPomoController);
        mockPomoController.stopProgram();
        EasyMock.expectLastCall();
        EasyMock.replay(mockPomoController);
        
        PopupMenu menu = manager.getPopupMenu();
        ActionListener[] listeners = menu.getItem(2).getActionListeners();
        callActionListeners(listeners);
        EasyMock.verify(mockPomoController);
    }

    private void callActionListeners(ActionListener[] listeners) {
        for (ActionListener listener : listeners){
            listener.actionPerformed(null);
        }
    }
    
    private void checkImageArray(Image[] array){
        for (int i = 0; i < array.length; i++){
            assertNotNull(array[i]);
            assertImageSize(array[i]);
        }
    }
    
    private void assertImageSize(Image image){
        Dimension actual = new Dimension(image.getWidth(null), image.getHeight(null));
        assertEquals(manager.getTrayIconSize(), actual);
    }
    
    
    class TestSystemTrayManager extends AbstractSystemTrayManager{

        @Override
        protected Dimension getTrayIconSize() {
            return new Dimension(32,32);
        }
        
        PopupMenu getPopupMenu(){
            return createPopupMenu();
        }
        
        void doInitializeImages(){
            initializeImages();
        }
        
        Image[] getPomoImages(){
            return pomoImages;
        }
        
        Image[] getBreakImages(){
            return breakImages;
        }
        
        Image getWaitImage(){
            return waitImage;
        }
    }
}
