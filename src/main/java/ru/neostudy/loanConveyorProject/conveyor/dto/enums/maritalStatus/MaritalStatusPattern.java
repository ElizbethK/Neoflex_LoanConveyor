package ru.neostudy.loanConveyorProject.conveyor.dto.enums.maritalStatus;


import ru.neostudy.loanConveyorProject.conveyor.dto.enums.gender.EnumGenderPatternValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = MaritalStatusPatternValidator.class)
public @interface MaritalStatusPattern {
    String regexp();
    String message() default "must match \"{regexp}\"";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
