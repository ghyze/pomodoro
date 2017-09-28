package nl.ghyze.pomodoro.view;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.type.PomodoroType;

public class PomoButton
{

   private int x;
   private int y;
   private int width;
   private int height;
   private List<PomodoroType> visibleTypes;
   
   private PomoButtonAction action;
   
   private Image image;
   
   public PomoButton(int x, int y, int width, int height){
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      visibleTypes = new ArrayList<PomodoroType>();
   }
   
   public boolean isVisible(Pomodoro countdown){
      return visibleTypes.contains(countdown.getType());
   }
   
   public boolean containsPoint(Point p){
      Rectangle r = new Rectangle(x,y,width,height);
      return r.contains(p);
   }
   
   public void setAction(PomoButtonAction action){
      this.action = action;
   }
   
   public void executeAction(){
      if (action != null){
         action.execute();
      }
   }
   
   public Image getImage() {
      return image;
   }
   
   public void setImage(Image image){
      this.image = image;
   }
   
   public int getX(){
      return x;
   }
   
   public int getY(){
      return y;
   }
   
   public void addVisibleType(PomodoroType type){
      visibleTypes.add(type);
   }
}
