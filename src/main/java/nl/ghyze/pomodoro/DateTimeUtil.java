package nl.ghyze.pomodoro;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil
{

   public static String format(Date date){
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
      return formatter.format(date);
   }
}
