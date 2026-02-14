package nl.ghyze.pomodoro.optiondialog;

import nl.ghyze.pomodoro.model.PomodoroType;

public class OptionDialogModelFactory {
    public static OptionDialogModel createChangeStateModel(PomodoroType type) {
        return OptionDialogModel.builder()
                .title(type.getDialogTitle())
                .message(type.getDialogMessage())
                .choices(type.getDialogChoices())
                .build();
    }

    public static OptionDialogModel createResetModel() {
        return OptionDialogModel.builder()
                .title("Reset")
                .message("Resetting will set the current pomos done to 0, and set the status to Waiting. Proceed?")
                .choices(new Object[]{"Ok", "Cancel"})
                .build();
    }

}
