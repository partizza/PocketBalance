package ua.agwebs.root.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceAccountId;
import ua.agwebs.root.repo.BalanceAccountRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class CheckBalanceAccountEnable implements ConstraintValidator<EnableBalanceAccount, BalanceAccount> {

    private static final Logger logger = LoggerFactory.getLogger(CheckBalanceAccountEnable.class);

    private BalanceAccountRepository accRepo;

    @Override
    public void initialize(EnableBalanceAccount constraintAnnotation) {

    }

    @Override
    public boolean isValid(BalanceAccount account, ConstraintValidatorContext context) {
        logger.debug("Check if balance account enable: {}", account);

        if (account == null || account.getAccId() == null || account.getBook().getId() == null) {
            logger.debug("False. No account id provided.");
            return false;
        }

        BalanceAccount resultedAccount = accRepo.findOne(new BalanceAccountId(account.getBook().getId(), account.getAccId()));
        if (resultedAccount == null || !resultedAccount.isEnable()) {
            logger.debug("False. Balance account doesn't exist or disabled.");
            return false;
        } else {
            logger.debug("True.");
            return true;
        }
    }

    @Autowired
    public void setBalanceAccountRepository(BalanceAccountRepository balanceAccountRepository) {
        this.accRepo = balanceAccountRepository;
    }
}
