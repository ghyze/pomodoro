package nl.ghyze.pomodoro.model.optiondialog;

import nl.ghyze.pomodoro.model.Pomodoro.Type;

public class OptionDialogModelFactory {

    private Type type;
    
    public OptionDialogModelFactory(Type type){
	this.type = type;
    }
    
    public OptionDialogModel getOptions(){
	OptionDialogModel model = new OptionDialogModel();
	model.setTitle(getDialogTitle());
	model.setMessage(getDialogMessage());
	model.setChoices(getDialogChoices());
	return model;
    }

    private String getDialogMessage() {
	if (Type.POMO == type) {
	    return "Pomodoro finished. What would you like to do with this one?";
	} else if (Type.BREAK == type) {
	    return "Ready to start next one?";
	}
	return "";
    }

    private String getDialogTitle() {
	if (Type.POMO == type) {
	    return "Pomodoro finished";
	} else if (Type.BREAK == type) {
	    return "Break finished";
	}
	return "";
    }

    private Object[] getDialogChoices() {
	if (Type.POMO == type) {
	    return new Object[] { "Save", "Discard" };
	} else if (Type.BREAK == type) {
	    return new Object[] { "Ok", "Cancel" };
	}
	return new String[] {};
    }
}
