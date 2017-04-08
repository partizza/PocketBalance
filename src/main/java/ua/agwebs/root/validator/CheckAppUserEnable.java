package ua.agwebs.root.validator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ua.agwebs.root.entity.AppUser;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceAccountId;
import ua.agwebs.root.repo.AppUserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckAppUserEnable implements ConstraintValidator<EnableAppUser, AppUser> {

    private static final Logger logger = LoggerFactory.getLogger(CheckBalanceAccountEnable.class);

    private AppUserRepository userRepo;

    @Override
    public void initialize(EnableAppUser constraintAnnotation) {

    }

    @Override
    public boolean isValid(AppUser appUser, ConstraintValidatorContext context) {
        logger.debug("Check if user enable: {}", appUser);

        if (appUser == null || appUser.getId() == null) {
            logger.debug("False. No user id provided.");
            return false;
        }

        AppUser resultedAppUser = userRepo.findOne(appUser.getId());
        if (resultedAppUser == null) {
            logger.debug("False. User doesn't exist.");
            return false;
        } else {
            logger.debug("True.");
            return true;
        }
    }

    @Autowired
    public void setUserRepo(AppUserRepository appUserRepository) {
        this.userRepo = appUserRepository;
    }
}
