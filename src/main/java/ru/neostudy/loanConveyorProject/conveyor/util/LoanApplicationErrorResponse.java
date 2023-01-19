package ru.neostudy.loanConveyorProject.conveyor.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanApplicationErrorResponse {
    private String message;
    private long timestamp;

    public LoanApplicationErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
