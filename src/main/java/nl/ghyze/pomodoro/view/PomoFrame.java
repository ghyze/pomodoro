package nl.ghyze.pomodoro.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import nl.ghyze.pomodoro.MultiScreenFactory;
import nl.ghyze.pomodoro.controller.PomoController;
import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.Settings;

public class PomoFrame extends JFrame
{
   JPopupMenu popup = new JPopupMenu();
   JMenuItem exit = new JMenuItem("Exit");
   JMenuItem hide = new JMenuItem("Hide");
   PomoPanel panel = new PomoPanel();
   PomoController controller;
   
   MultiScreenFactory multiScreenFactory = new MultiScreenFactory();
   
   private static int controlButtonWidth = 18;
   private static int controlButtonHeight = 18;
   
   public PomoFrame(final PomoController controller){
      this.controller = controller;
      this.setAlwaysOnTop(true);
      this.setUndecorated(true);
      this.setType(Window.Type.UTILITY);
      this.add(panel);
      this.pack();
      createButtons();
      
      this.setVisible(true);
      popup.add(exit);
      exit.addActionListener(new ActionListener(){

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    controller.stopProgram();
	}
	  
      });
      
      popup.add(hide);
      hide.addActionListener(new ActionListener(){

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    setVisible(false);
	}
	  
      });
      
      this.addMouseListener( new MouseAdapter(){
         public void mouseClicked(MouseEvent e){
            if (e.getButton() == 3){
               popup.show(e.getComponent(), e.getX(), e.getY());
            } else if (e.getButton() == 1 ){
               PomoButton button = panel.buttonClicked(e);
               if (button != null){
                  button.executeAction();
               }
            }
         }
      });
   }

   public void update(Pomodoro countdown){
      panel.update(countdown);
      this.repaint();
   }
   
   private void createButtons(){
      panel.addButton(createStopButton());
      panel.addButton(createPlayButton());
      panel.addButton(createCloseButton());
      panel.addButton(createMinimizeButton());
   }
   
   private PomoButton createStopButton(){
      
      
      BufferedImage image = new BufferedImage(controlButtonWidth, controlButtonHeight, BufferedImage.TYPE_INT_ARGB);
      
      Graphics gr = image.getGraphics();
      gr.setColor(Color.white);
      gr.drawRect(0, 0, controlButtonWidth-1, controlButtonHeight-1);
      gr.fillRect(controlButtonWidth/4, controlButtonHeight/4, controlButtonWidth/2, controlButtonHeight/2);
      
      PomoButton stopButton = new PomoButton(2, 80, controlButtonWidth, controlButtonHeight);
      stopButton.addVisibleType(Pomodoro.Type.BREAK);
      stopButton.addVisibleType(Pomodoro.Type.POMO);
      stopButton.setImage(image);
      stopButton.setAction(new PomoButtonAction(){

         @Override
         public void execute()
         {
            controller.stopCurrent();
         }
         
      });
      
      return stopButton;
   }
   
   private PomoButton createPlayButton(){
      
      
      BufferedImage image = new BufferedImage(controlButtonWidth, controlButtonHeight, BufferedImage.TYPE_INT_ARGB);
      
      Graphics gr = image.getGraphics();
      gr.setColor(Color.white);
      gr.drawRect(0, 0, controlButtonWidth-1, controlButtonHeight-1);

      Polygon pol = new Polygon();
      pol.addPoint(controlButtonWidth / 4, controlButtonHeight / 4);
      pol.addPoint(controlButtonWidth / 4, controlButtonHeight - (controlButtonHeight / 4));
      pol.addPoint(controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight / 2);
      
      gr.fillPolygon(pol);
      
      PomoButton playButton = new PomoButton(22,80,18,18);
      playButton.addVisibleType(Pomodoro.Type.WAIT);
      playButton.setImage(image);
      playButton.setAction(new PomoButtonAction(){

         @Override
         public void execute()
         {
            controller.startPomo();
         }
         
      });
      
      return playButton;
   }

   private PomoButton createCloseButton(){
      
      
      BufferedImage image = new BufferedImage(controlButtonWidth, controlButtonHeight, BufferedImage.TYPE_INT_ARGB);
      
      Graphics gr = image.getGraphics();
      gr.setColor(Color.white);
      gr.drawRect(0, 0, controlButtonWidth-1, controlButtonHeight-1);
      gr.drawLine(controlButtonWidth / 4, controlButtonHeight / 4, controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight - (controlButtonHeight / 4));
      gr.drawLine(controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight / 4, controlButtonWidth / 4, controlButtonHeight - (controlButtonHeight / 4));
      
      PomoButton closeButton = new PomoButton(118,2,18,18);
      closeButton.addVisibleType(Pomodoro.Type.BREAK);
      closeButton.addVisibleType(Pomodoro.Type.POMO);
      closeButton.addVisibleType(Pomodoro.Type.WAIT);
      closeButton.setImage(image);
      closeButton.setAction(new PomoButtonAction(){

         @Override
         public void execute()
         {
            controller.stopProgram();
         }
         
      });
      return closeButton;
   }
   
   private PomoButton createMinimizeButton(){
      
      
      BufferedImage image = new BufferedImage(controlButtonWidth, controlButtonHeight, BufferedImage.TYPE_INT_ARGB);
      
      Graphics gr = image.getGraphics();
      gr.setColor(Color.white);
      gr.drawRect(0, 0, controlButtonWidth-1, controlButtonHeight-1);
      gr.drawLine(controlButtonWidth / 4, controlButtonHeight - (controlButtonHeight / 4), controlButtonWidth - (controlButtonWidth / 4), controlButtonHeight - (controlButtonHeight / 4));
      
      PomoButton minimizeButton = new PomoButton(98,2,18,18);
      minimizeButton.addVisibleType(Pomodoro.Type.BREAK);
      minimizeButton.addVisibleType(Pomodoro.Type.POMO);
      minimizeButton.addVisibleType(Pomodoro.Type.WAIT);
      minimizeButton.setImage(image);
      minimizeButton.setAction(new PomoButtonAction(){

         @Override
         public void execute()
         {
            setVisible(false);
         }
         
      });
      
      return minimizeButton;
   }
   
   public void position(Settings settings){
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
            this.setLocation(graphicsDeviceOffset.x + 0, graphicsDeviceOffset.y + mostBottomRightPoint.y - this.getHeight());
            break;
         case TOP_LEFT:
            this.setLocation(graphicsDeviceOffset.x + 0, graphicsDeviceOffset.y + 0);
            break;
         case TOP_RIGHT:
            this.setLocation(graphicsDeviceOffset.x + graphicsDeviceOffset.y + mostBottomRightPoint.x - this.getWidth(), 0);
            break;
         default:
               
      }
   }


   
}
