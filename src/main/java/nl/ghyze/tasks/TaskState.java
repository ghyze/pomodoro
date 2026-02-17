package nl.ghyze.tasks;

import lombok.Getter;

@Getter
public enum TaskState {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String name;

    TaskState(final String name) {
        this.name = name;
    }
}
