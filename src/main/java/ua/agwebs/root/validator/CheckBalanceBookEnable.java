package ua.agwebs.root.validator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.repo.BalanceBookRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckBalanceBookEnable implements ConstraintValidator<EnabledBalanceBook, BalanceBook> {

    private static final Logger logger = LoggerFactory.getLogger(CheckBalanceBookEnable.class);

    private BalanceBookRepository repo;

    @Override
    public void initialize(EnabledBalanceBook constraintAnnotation) {
    }


    @Override
    public boolean isValid(BalanceBook book, ConstraintValidatorContext constraintContext) {
        logger.debug("Check if balance book enable: ", book);

        if (book == null || book.getId() == null) {
            logger.debug("False. No balance book id provided.");
            return false;
        }

        BalanceBook resultedBook = repo.findOne(book.getId());
        if (resultedBook == null || resultedBook.isDeleted()) {
            logger.debug("False. Balance book doesn't exist or deleted.");
            return false;
        } else {
            logger.debug("Balance book is enable.");
            return  true;
        }

    }

    @Autowired
    public void setRepo(BalanceBookRepository balanceBookRepository) {
        this.repo = balanceBookRepository;
    }
}
