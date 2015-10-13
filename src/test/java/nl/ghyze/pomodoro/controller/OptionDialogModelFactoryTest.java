package nl.ghyze.pomodoro.controller;

import static org.junit.Assert.*;

import org.junit.Test;

import org.junit.Assert;
import nl.ghyze.pomodoro.model.Pomodoro.Type;

public class OptionDialogModelFactoryTest {

    @Test
    public void testGetOptionsForPomo() throws Exception {
	OptionDialogModelFactory factory = new OptionDialogModelFactory(Type.POMO);
	OptionDialogModel model = factory.getOptions();
	
	Object firstChoice = "Save";
	Object secondChoice = "Discard";
	
	assertOptionDialogModel(model, "Pomodoro finished", "Pomodoro finished. What would you like to do with this one?", new Object[]{firstChoice, secondChoice}, firstChoice);
    }
    
    @Test
    public void testGetOptionsForBreak() throws Exception {
	OptionDialogModelFactory factory = new OptionDialogModelFactory(Type.BREAK);
	OptionDialogModel model = factory.getOptions();
	
	Object firstChoice = "Ok";
	Object secondChoice = "Cancel";
	
	assertOptionDialogModel(model, "Break finished", "Ready to start next one?", new Object[]{firstChoice, secondChoice}, firstChoice);
    }
    
    @Test
    public void testGetOptionsForWait() throws Exception {
	OptionDialogModelFactory factory = new OptionDialogModelFactory(Type.WAIT);
	OptionDialogModel model = factory.getOptions();
	
	
	
	assertOptionDialogModel(model, "", "", new Object[]{}, null);
    }
    
    private void assertOptionDialogModel(OptionDialogModel model, String title, String message, Object[] choices, Object defaultChoice){
	Assert.assertEquals(title, model.getTitle());
	Assert.assertEquals(message, model.getMessage());
	Assert.assertEquals(choices.length, model.getChoices().length);
	for (int i = 0; i < choices.length; i++){
	    Assert.assertEquals(choices[i], model.getChoices()[i]);
	}
	Assert.assertEquals(defaultChoice, model.getDefaultChoice());
    }
}