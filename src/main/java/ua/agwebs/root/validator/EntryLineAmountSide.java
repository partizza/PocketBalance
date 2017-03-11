package ua.agwebs.root.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckEntryLineAmountSide.class)
@Documented
public @interface EntryLineAmountSide {

    String message() default "Debit amount should be only positive not zero value. Credit amount should be negative not zero value.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
