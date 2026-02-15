package nl.ghyze.pomodoro.optiondialog;

import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import nl.ghyze.pomodoro.Stopwatch;

import static java.util.Objects.nonNull;
import static javax.swing.JOptionPane.CANCEL_OPTION;
import static javax.swing.JOptionPane.CLOSED_OPTION;
import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

public class OptionDialogController
{
   private static final int DIALOG_TIMEOUT_MINUTES = 5;

   private final JFrame frame;
   private final long timeoutMillis;
   private Stopwatch stopwatch;
   private boolean showing = false;

   public OptionDialogController(JFrame frame) {
      this(frame, DIALOG_TIMEOUT_MINUTES * Stopwatch.MILLISECONDS_PER_MINUTE);
   }

   // Package-private constructor for testing with custom timeout
   OptionDialogController(JFrame frame, long timeoutMillis) {
      this.frame = frame;
      this.timeoutMillis = timeoutMillis;
   }

   public void showDialog(OptionDialogModel model, OptionDialogCallback callback)
   {
      if (!isShowing())
      {
         showing = true;
         stopwatch = new Stopwatch();
         final JLabel label = new JLabel(model.getMessage());
         int timerDelay = Stopwatch.MILLISECONDS_PER_SECOND;
         new Timer(timerDelay, e -> {
            if (stopwatch.isTimedOut(timeoutMillis))
            {
               ((Timer) e.getSource()).stop();
               if (showing)
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

         int result = JOptionPane.showOptionDialog(frame, label, model.getTitle(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, model.getChoices(), model.getDefaultChoice().orElse(null));
         showing = false;
         handleResult(callback, result);
      }
   }

   private void handleResult(OptionDialogCallback callback, int result)
   {
      if (nonNull(callback)) {
         switch (result) {
            case CLOSED_OPTION -> callback.timeout();
            case YES_OPTION -> callback.ok();
            case NO_OPTION -> callback.cancel();
            case CANCEL_OPTION -> callback.continueAction();
         }
      }
   }

   public boolean isShowing()
   {
      return showing;
   }

}
