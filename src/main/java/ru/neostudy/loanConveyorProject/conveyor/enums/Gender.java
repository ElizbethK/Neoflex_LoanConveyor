package ru.neostudy.loanConveyorProject.conveyor.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE"),
    NOTBINARY("NOTBINARY");

    private String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
