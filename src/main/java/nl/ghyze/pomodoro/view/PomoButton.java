package nl.ghyze.pomodoro.view;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nl.ghyze.pomodoro.controller.PomoAction;
import nl.ghyze.pomodoro.model.Pomodoro;
import nl.ghyze.pomodoro.model.PomodoroType;

import static java.util.Objects.nonNull;

class PomoButton
{
   @Getter
   private final int x;
   @Getter
   private final int y;
   private final int width;
   private final int height;
   private final List<PomodoroType> visibleTypes;

   @Setter
   private PomoAction action;

   @Getter @Setter
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

   void executeAction(){
      if (nonNull(action)){
         action.execute();
      }
   }

   void addVisibleType(PomodoroType type){
      visibleTypes.add(type);
   }
}
