package nl.ghyze.pomodoro.view;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import nl.ghyze.pomodoro.controller.PomoAction;
import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.type.PomodoroType;

import static java.util.Objects.nonNull;

class PomoButton
{

   private final int x;
   private final int y;
   private final int width;
   private final int height;
   private final List<PomodoroType> visibleTypes;
   
   private PomoAction action;
   
   private Image image;
   
   PomoButton(int x, int y, int width, int height){
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      visibleTypes = new ArrayList<>();
   }
   
   boolean isVisible(Pomodoro countdown){
      return visibleTypes.contains(countdown.getType());
   }
   
   boolean containsPoint(Point p){
      Rectangle r = new Rectangle(x,y,width,height);
      return r.contains(p);
   }
   
   void setAction(PomoAction action){
      this.action = action;
   }
   
   void executeAction(){
      if (nonNull(action)){
         action.execute();
      }
   }
   
   Image getImage() {
      return image;
   }
   
   void setImage(Image image){
      this.image = image;
   }
   
   int getX(){
      return x;
   }
   
   int getY(){
      return y;
   }
   
   void addVisibleType(PomodoroType type){
      visibleTypes.add(type);
   }
}
