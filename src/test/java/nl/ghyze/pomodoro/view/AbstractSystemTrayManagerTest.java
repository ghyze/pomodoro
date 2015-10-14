package nl.ghyze.pomodoro.view;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.PopupMenu;

import org.junit.Test;

import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;

public class AbstractSystemTrayManagerTest {

    @Test
    public void testCreatePopupMenu() throws Exception {
        TestSystemTrayManager manager = new TestSystemTrayManager();
        PopupMenu menu = manager.getPopupMenu();
        assertEquals(3, menu.getItemCount());
        assertEquals("Show Frame",menu.getItem(0).getLabel());
        assertEquals("Settings",menu.getItem(1).getLabel());
        assertEquals("Exit",menu.getItem(2).getLabel());
    }
    
    
    class TestSystemTrayManager extends AbstractSystemTrayManager{

        @Override
        protected Dimension getTrayIconSize() {
            return new Dimension(32,32);
        }
        
        PopupMenu getPopupMenu(){
            return createPopupMenu();
        }
        
    }
}
