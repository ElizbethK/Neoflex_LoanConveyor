package ru.neostudy.loanConveyorProject.conveyor.dto.enums.maritalStatus;

import ru.neostudy.loanConveyorProject.conveyor.dto.enums.gender.EnumGenderPattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MaritalStatusPatternValidator implements ConstraintValidator<MaritalStatusPattern, Enum<?>> {
    private Pattern pattern;

    @Override
    public void initialize(MaritalStatusPattern annotation) {
        try {
            pattern = Pattern.compile(annotation.regexp());
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Given regex is invalid", e);
        }
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Matcher m = pattern.matcher(value.name());
        return m.matches();
    }
}
