package nl.ghyze.pomodoro.optiondialog;

import java.awt.Window;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.ghyze.pomodoro.DateTimeUtil;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionDialogController
{
   private static final OptionDialogController instance = new OptionDialogController();
   private static final long timeout = 5 * DateTimeUtil.MILLISECONDS_PER_MINUTE; // 5 minutes
   private long showingSince;

   private boolean showing = false;

   @SuppressWarnings("serial")
   public static void showDialog(JFrame frame, OptionDialogModel model, OptionDialogCallback callback)
   {
      if (!isShowing())
      {
         instance.showing = true;
         instance.showingSince = new Date().getTime();
         final JLabel label = new JLabel(model.getMessage());
         int timerDelay = DateTimeUtil.MILLISECONDS_PER_SECOND;
         new Timer(timerDelay, e -> {
            if (instance.isTimedOut())
            {
               ((Timer) e.getSource()).stop();
               if (instance.showing)
               {
                  Window win = SwingUtilities.getWindowAncestor(label);
                  win.setVisible(false);
               }
            }
         })
            {
               {
                  setInitialDelay(0);
               }
            }.start();

         int result = JOptionPane.showOptionDialog(frame, label, model.getTitle(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, model.getChoices(), model.getDefaultChoice());
         instance.showing = false;
         handleResult(callback, result);
      }
   }

   private static void handleResult(OptionDialogCallback callback, int i)
   {
      if (callback != null)
      {
         switch (i)
         {
            case -1:
               callback.timeout();
               break;
            case 0:
               callback.ok();
               break;
            case 1:
               callback.cancel();
               break;
            case 2:
               callback.continueAction();
               break;
            default:
               //
         }
      }
   }

   private boolean isTimedOut()
   {
      Date now = new Date();
      return now.getTime() > showingSince + timeout;
   }

   private static boolean isShowing()
   {
      return instance.showing;
   }

}
