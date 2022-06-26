package ru.nsu.ccfit.lopatkin.testTask.utils.errorHandler;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private List<Violation> violations = new ArrayList<>();

    public ErrorResponse() {
    }

    public List<Violation> getViolations() {
        return violations;
    }

}
