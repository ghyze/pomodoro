package nl.ghyze.pomodoro;

import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.view.systemtray.AbstractSystemTrayManager;
import nl.ghyze.pomodoro.view.systemtray.SystemTrayManagerImpl;

public class PomoApp
{

   public static void main(String[] args)
   {
//	   GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
//	   for(Font font : env.getAllFonts()){
//		   System.out.println(font);
//	   }
	   
      PomoController controller = new PomoController();
      AbstractSystemTrayManager systemTrayManager = new SystemTrayManagerImpl();
      controller.initializeSystemTrayManager(systemTrayManager);
      controller.initialize();
   }

}
