package nl.ghyze.pomodoro.optiondialog;

import nl.ghyze.pomodoro.model.PomodoroType;

public class OptionDialogModelFactory {
    public static OptionDialogModel createChangeStateModel(PomodoroType type) {
        OptionDialogModel model = new OptionDialogModel();
        model.setTitle(type.getDialogTitle());
        model.setMessage(type.getDialogMessage());
        model.setChoices(type.getDialogChoices());
        return model;
    }

    public static OptionDialogModel createResetModel() {
        OptionDialogModel model = new OptionDialogModel();

        model.setTitle("Reset");
        model.setMessage("Resetting will set the current pomos done to 0, and set the status to Waiting. Proceed?");
        model.setChoices(new Object[]{"Ok", "Cancel"});

        return model;
    }

}
