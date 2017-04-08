package ua.agwebs.root.service;

import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import ua.agwebs.root.entity.AppUser;
import ua.agwebs.root.repo.AppUserRepository;

import javax.validation.Valid;

@Service
public class AppUserManager implements AppUserService {

    private static final Logger logger = LoggerFactory.getLogger(AppUserManager.class);

    private AppUserRepository userRepo;

    @Autowired
    public AppUserManager(AppUserRepository userRepo) {
        this.userRepo = userRepo;
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
    public AppUser createAppUser(@Valid AppUser appUser) {
        logger.info("Create new user.");
        logger.debug("Passed params: {}", appUser);

        Assert.notNull(appUser);
        Assert.isTrue(!this.existsByEmail(appUser.getEmail()), "User already exists.");

        AppUser createdAppUser = userRepo.save(appUser);

        logger.debug("Created user: {}", createdAppUser);
        return createdAppUser;
    }
}
