package ua.agwebs.root.validator;


import org.springframework.beans.factory.annotation.Autowired;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.repo.BalanceBookRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckBalanceBookEnable implements ConstraintValidator<EnabledBalanceBook, BalanceBook> {


    private BalanceBookRepository repo;

    @Override
    public void initialize(EnabledBalanceBook constraintAnnotation) {
    }


    @Override
    public boolean isValid(BalanceBook book, ConstraintValidatorContext constraintContext) {

        if (book == null || book.getId() == null) {
            return false;
        }

        BalanceBook resultedBook = repo.findOne(book.getId());
        if (resultedBook == null || resultedBook.isDeleted()) {
            return false;
        } else {
            return  true;
        }

    }

    @Autowired
    public void setRepo(BalanceBookRepository balanceBookRepository) {
        this.repo = balanceBookRepository;
    }
}
