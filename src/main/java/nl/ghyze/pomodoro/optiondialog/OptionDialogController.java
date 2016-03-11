package nl.ghyze.pomodoro.optiondialog;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class OptionDialogController
{
   private static OptionDialogController instance = new OptionDialogController();
   private long timeout = 5 * 60 * 1000l; // 5 minutes
   private long showingSince;

   private boolean showing = false;

   private OptionDialogController()
   {

   }

   @SuppressWarnings("serial")
   public static void showDialog(JFrame frame, OptionDialogModel model, OptionDialogCallback callback)
   {
      if (!isShowing())
      {
         instance.showing = true;
         instance.showingSince = new Date().getTime();
         final JLabel label = new JLabel(model.getMessage());
         int timerDelay = 1000;
         new Timer(timerDelay, new ActionListener()
            {

               @Override
               public void actionPerformed(ActionEvent e)
               {
                  if (instance.isTimedOut())
                  {
                     ((Timer) e.getSource()).stop();
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
         handleResult(callback, result);
         instance.showing = false;
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

   public static boolean isShowing()
   {
      return instance.showing;
   }

}
