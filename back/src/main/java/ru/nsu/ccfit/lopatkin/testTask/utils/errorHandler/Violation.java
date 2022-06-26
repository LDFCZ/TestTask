package ru.nsu.ccfit.lopatkin.testTask.utils.errorHandler;

public class Violation {
    private final String name;
    private final String message;

    public Violation(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
