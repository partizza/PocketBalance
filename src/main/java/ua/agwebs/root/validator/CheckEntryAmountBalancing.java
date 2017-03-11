package ua.agwebs.root.validator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.entity.EntryLine;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CheckEntryAmountBalancing implements ConstraintValidator<EntryAmountBalancing, Set<EntryLine>> {

    private static final Logger logger = LoggerFactory.getLogger(CheckBalanceBookEnable.class);

    @Override
    public void initialize(EntryAmountBalancing constraintAnnotation) {

    }

    @Override
    public boolean isValid(Set<EntryLine> entryLines, ConstraintValidatorContext context) {
        logger.debug("Check entry balancing: {}", entryLines);

        if (entryLines == null) {
            logger.debug("Entry balancing - false. Entry lines is null.");
            return false;
        }

        Map<Long, Long> currencyBalancing = new HashMap<>();
        for (EntryLine e : entryLines) {
            Long curyId = e.getCurrency().getId();
            Long amt = currencyBalancing.get(curyId);
            amt = amt == null ? e.getTrnAmount() : amt + e.getTrnAmount();
            currencyBalancing.put(curyId, amt);
        }

        for (Long e : currencyBalancing.values()) {
            if (!e.equals(0L)) {
                logger.debug("Entry balancing - false.");
                return false;
            }
        }

        logger.debug("Entry balancing - true.");
        return true;
    }
}
