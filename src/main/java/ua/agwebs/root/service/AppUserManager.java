package ua.agwebs.root.service;

import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import ua.agwebs.root.entity.AppUser;
import ua.agwebs.root.entity.BSCategory;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.repo.AppUserRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserManager implements AppUserService {

    private static final Logger logger = LoggerFactory.getLogger(AppUserManager.class);

    private AppUserRepository userRepo;

    private CoaService coaService;

    @Autowired
    public AppUserManager(AppUserRepository userRepo, CoaService coaService) {
        this.userRepo = userRepo;
        this.coaService = coaService;
    }

    @Override
    public boolean existsByEmail(@Email String email) {
        logger.info("Check if user exists.");
        logger.debug("Passed params: email = {}", email);

        AppUser appUser = userRepo.findByEmail(email);
        boolean isExists = appUser == null ? false : true;

        logger.debug("Existing user: {}", isExists);
        return isExists;
    }

    @Override
    public AppUser findByEmail(@Email String email) {
        logger.info("find user by email.");
        logger.debug("Passed params: email = {}", email);

        AppUser appUser = userRepo.findByEmail(email);

        logger.debug("Selected user: {}", appUser);
        return appUser;
    }

    @Transactional
    @Override
    public AppUser createAppUser(@Valid AppUser appUser) {
        logger.info("Create new user with own balance book and initial balance accounts.");
        logger.debug("Passed params: {}", appUser);

        Assert.notNull(appUser);
        Assert.isTrue(!this.existsByEmail(appUser.getEmail()), "User already exists.");

        AppUser createdAppUser = userRepo.save(appUser);

        BalanceBook book = new BalanceBook("Balance book", "Initial balance book", appUser);
        book = coaService.createBalanceBook(book);

        createdAppUser.addBook(book);
        return createdAppUser;
    }

}
