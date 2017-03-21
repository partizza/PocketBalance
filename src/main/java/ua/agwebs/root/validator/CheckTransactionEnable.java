package ua.agwebs.root.validator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ua.agwebs.root.entity.Transaction;
import ua.agwebs.root.repo.TransactionRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckTransactionEnable implements ConstraintValidator<EnabledTransaction, Transaction> {

    private static final Logger logger = LoggerFactory.getLogger(CheckTransactionEnable.class);

    private TransactionRepository tranRepo;

    @Override
    public void initialize(EnabledTransaction constraintAnnotation) {

    }

    @Override
    public boolean isValid(Transaction value, ConstraintValidatorContext context) {
        logger.debug("Check if transaction enable: {}", value);

        if(value == null || value.getId() == null){
            logger.debug("False. No transaction is provided.");
            return false;
        }

        Transaction transaction = tranRepo.findOne(value.getId());
        if(transaction == null || transaction.isDeleted()){
            logger.debug("False. Transaction doesn't exist or has been deleted.");
            return  false;
        } else {
            logger.debug("Transaction is enable.");
            return  true;
        }

    }

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository){
        this.tranRepo = transactionRepository;
    }
}
