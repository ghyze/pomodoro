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

public class PomoPanel extends JPanel
{
   
   private Pomodoro countdown = null;
   
   private List<PomoButton> buttons = new ArrayList<PomoButton>();
   
   public PomoPanel(){
      this.setPreferredSize(new Dimension(140, 100));
      this.setLayout(null);
   }
   
   protected void paintComponent(Graphics gr){
      if (countdown != null){
         switch (countdown.getType()){
         case WAIT:
            paintWait(gr);
            break;
         case POMO:
            paintPomo(gr);
            break;
         case BREAK:
            paintBreak(gr);
            break;
         }
         drawButtons(gr);
         drawPomosDone(gr);
      }
      
   }
   
   private void paintWait(Graphics gr){
      gr.setColor(Color.BLUE);
      gr.fillRect(0, 0, this.getWidth(), this.getHeight());
      Font f = gr.getFont();
      gr.setColor(Color.white);
      Font bigFont = f.deriveFont(20f);
      gr.setFont(bigFont);
      paintText("Waiting for next", gr);
      
      gr.setFont(f);
   }
   
   private void paintText(String text, Graphics gr){
      FontMetrics fm = gr.getFontMetrics();
      Rectangle2D rect = fm.getStringBounds(text, gr);
      int x = (int) (this.getWidth()-rect.getWidth())/2;
      int y = (int) (this.getHeight()+rect.getHeight()) / 2;
      gr.drawString(text, x ,y);
   }
   
   private void paintPomo(Graphics gr){
      gr.setColor(Color.RED);
      gr.fillRect(0, 0, this.getWidth(), this.getHeight());
      Font f = gr.getFont();
      gr.setColor(Color.white);
      Font bigFont = f.deriveFont(30f);
      gr.setFont(bigFont);
      int secondsLeft = countdown.secondsOfMinuteLeft();
      String timeLeft = countdown.minutesLeft()+":" + (secondsLeft < 10? "0":"")+secondsLeft;
      paintText(timeLeft, gr);
      gr.setFont(f);
      
   }
   
   private void paintBreak(Graphics gr){
      gr.setColor(new Color(0,192,0));
      gr.fillRect(0, 0, this.getWidth(), this.getHeight());
      Font f = gr.getFont();
      gr.setColor(Color.white);
      Font bigFont = f.deriveFont(30f);
      gr.setFont(bigFont);
      int secondsLeft = countdown.secondsOfMinuteLeft();
      String timeLeft = countdown.minutesLeft()+":" + (secondsLeft < 10? "0":"")+secondsLeft;
      paintText(timeLeft, gr);
      gr.setFont(f);
      
   }
   
   private void drawPomosDone(Graphics gr){
      gr.setColor(Color.white);
      if (countdown != null){
         for (int i = 0; i < countdown.getMaxPomosDone() ; i++){
            int xOff = 2+(i*15);
            gr.drawRect(xOff, 2, 12, 12);
            if (i < countdown.getPomosDone()){
               gr.fillRect(xOff + 3, 5, 7, 7);
            }
         }
      }
   }
   
   
   private void drawButtons(Graphics gr){
      if (countdown != null){
         for (PomoButton button : buttons){
            if (button.isVisible(countdown)){
               gr.drawImage(button.getImage(), button.getX(), button.getY(), null);
            }
         }
      }
   }
   
   public PomoButton buttonClicked(MouseEvent e){
      for (PomoButton button : buttons){
         if (button.isVisible(countdown) && button.containsPoint(e.getPoint())){
            return button;
         }
      }
      return null;
   }
   
   public void update(Pomodoro countdown) {
      this.countdown = countdown;
   }
   
   public void addButton(PomoButton button){
      buttons.add(button);
   }
}
