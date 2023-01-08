package ru.neostudy.loanConveyorProject.conveyor.util;

public class LoanApplicationNotCreatedException extends RuntimeException{
    public LoanApplicationNotCreatedException(String message){
        super(message);
    }
}
