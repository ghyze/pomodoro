package nl.ghyze.pomodoro.optiondialog;

import nl.ghyze.pomodoro.type.PomodoroType;

import org.junit.Assert;
import org.junit.Test;

public class OptionDialogModelFactoryTest
{

   @Test
   public void testGetOptionsForPomo() throws Exception
   {
      OptionDialogModel model = OptionDialogModelFactory.createChangeStateModel(PomodoroType.POMO);

      Object firstChoice = "Save";
      Object secondChoice = "Discard";

      assertOptionDialogModel(model, "Pomodoro finished", "Pomodoro finished. What would you like to do with this one?", new Object[] { firstChoice, secondChoice }, firstChoice);
   }

   @Test
   public void testGetOptionsForBreak() throws Exception
   {
      OptionDialogModel model = OptionDialogModelFactory.createChangeStateModel(PomodoroType.BREAK);

      Object firstChoice = "Ok";
      Object secondChoice = "Cancel";

      assertOptionDialogModel(model, "Break finished", "Ready to start next one?", new Object[] { firstChoice, secondChoice }, firstChoice);
   }

   @Test
   public void testGetOptionsForWait() throws Exception
   {
      OptionDialogModel model = OptionDialogModelFactory.createChangeStateModel(PomodoroType.WAIT);

      assertOptionDialogModel(model, "", "", new Object[] {}, null);
   }

   private void assertOptionDialogModel(OptionDialogModel model, String title, String message, Object[] choices, Object defaultChoice)
   {
      Assert.assertEquals(title, model.getTitle());
      Assert.assertEquals(message, model.getMessage());
      Assert.assertEquals(choices.length, model.getChoices().length);
      for (int i = 0; i < choices.length; i++)
      {
         Assert.assertEquals(choices[i], model.getChoices()[i]);
      }
      Assert.assertEquals(defaultChoice, model.getDefaultChoice());
   }

   @Test
   public void testCreateResetModel() throws Exception
   {
      OptionDialogModel model = OptionDialogModelFactory.createResetModel();

      Object firstChoice = "Ok";
      Object secondChoice = "Cancel";

      assertOptionDialogModel(model, "Reset", "Resetting will set the current pomos done to 0, and set the status to Waiting. Proceed?", new Object[] { firstChoice, secondChoice }, firstChoice);
   }
}
