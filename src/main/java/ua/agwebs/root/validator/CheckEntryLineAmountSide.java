package ua.agwebs.root.validator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.entity.EntryLine;
import ua.agwebs.root.entity.EntrySide;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckEntryLineAmountSide implements ConstraintValidator<EntryLineAmountSide, EntryLine> {

    private static final Logger logger = LoggerFactory.getLogger(CheckEntryLineAmountSide.class);

    @Override
    public void initialize(EntryLineAmountSide constraintAnnotation) {

    }

    @Override
    public boolean isValid(EntryLine line, ConstraintValidatorContext context) {
        logger.debug("Check entry line amount side: {}", line);

        if(line == null){
            logger.debug("False entry line amount side. Entry line is null");
            return  false;
        }

        if( line.getType() == EntrySide.D & line.getTrnAmount() > 0){
            logger.debug("True entry line amount side.");
            return true;
        }

        if( line.getType() == EntrySide.C & line.getTrnAmount() < 0){
            logger.debug("True entry line amount side.");
            return true;
        }

        logger.debug("False entry line amount side.");
        return false;
    }
}
