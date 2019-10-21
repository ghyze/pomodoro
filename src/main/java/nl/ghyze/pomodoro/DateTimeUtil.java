package nl.ghyze.pomodoro;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil
{

   public static final int MILLISECONDS_PER_SECOND = 1000;
   public static final int MILLISECONDS_PER_MINUTE = 60 * MILLISECONDS_PER_SECOND;

   public static String format(Date date){
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
      return formatter.format(date);
   }

   public static int minutesSince(long millisSinceLastAction) {
      Date now = new Date();
      long millis = (now.getTime() - millisSinceLastAction);
      return (int) (millis / MILLISECONDS_PER_MINUTE);
   }
}
