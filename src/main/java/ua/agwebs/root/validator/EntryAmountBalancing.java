package ua.agwebs.root.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckEntryAmountBalancing.class)
@Documented
public @interface EntryAmountBalancing {

    String message() default "Amount is not correct. Debit and credit should be equal in each currency.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
