package nl.ghyze.pomodoro.optiondialog;

import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.ghyze.pomodoro.Stopwatch;

import static java.util.Objects.nonNull;
import static javax.swing.JOptionPane.CANCEL_OPTION;
import static javax.swing.JOptionPane.CLOSED_OPTION;
import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionDialogController
{
   private static OptionDialogController instance;
   private final long timeout = 5 * Stopwatch.MILLISECONDS_PER_MINUTE; // 5 minutes
   private Stopwatch stopwatch;

   private boolean showing = false;

   private JFrame frame;

   public static void init(JFrame frame){
      if (nonNull(instance)){
         throw new IllegalStateException("OptionDialogController already initialized");
      }
      instance = new OptionDialogController();
      instance.frame = frame;
   }

   public static void showDialog(OptionDialogModel model, OptionDialogCallback callback)
   {
      if (!isShowing())
      {
         instance.showing = true;
         instance.stopwatch = new Stopwatch();
         final JLabel label = new JLabel(model.getMessage());
         int timerDelay = Stopwatch.MILLISECONDS_PER_SECOND;
         new Timer(timerDelay, e -> {
            if (instance.stopwatch.isTimedOut(instance.timeout))
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

         int result = JOptionPane.showOptionDialog(instance.frame, label, model.getTitle(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, model.getChoices(), model.getDefaultChoice());
         instance.showing = false;
         handleResult(callback, result);
      }
   }

   private static void handleResult(OptionDialogCallback callback, int result)
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

   private static boolean isShowing()
   {
      return instance.showing;
   }

}
