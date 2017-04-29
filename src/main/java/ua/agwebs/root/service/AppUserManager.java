package ua.agwebs.root.service;

import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<BalanceAccount> accounts = this.getInitBalanceAccounts(book);
        coaService.createBalanceAccount(accounts);

        createdAppUser.addBook(book);
        return createdAppUser;
    }

    private List<BalanceAccount> getInitBalanceAccounts(BalanceBook book) {
        List<BalanceAccount> initAccounts = new ArrayList<>();
        initAccounts.add(new BalanceAccount(BSCategory.ASSET, 110L, book, "Cash on hand"));
        initAccounts.add(new BalanceAccount(BSCategory.ASSET, 120L, book, "Current accounts"));
        initAccounts.add(new BalanceAccount(BSCategory.ASSET, 130L, book, "Saving accounts"));
        initAccounts.add(new BalanceAccount(BSCategory.ASSET, 140L, book, "Deposits"));
        initAccounts.add(new BalanceAccount(BSCategory.ASSET, 150L, book, "Loans to others"));
        initAccounts.add(new BalanceAccount(BSCategory.ASSET, 190L, book, "Other cash"));

        initAccounts.add(new BalanceAccount(BSCategory.LIABILITY, 210L, book, "Credit cards"));
        initAccounts.add(new BalanceAccount(BSCategory.LIABILITY, 220L, book, "Loans from others"));
        initAccounts.add(new BalanceAccount(BSCategory.LIABILITY, 290L, book, "Other debts"));

        initAccounts.add(new BalanceAccount(BSCategory.PROFIT, 310L, book, "Salary"));
        initAccounts.add(new BalanceAccount(BSCategory.PROFIT, 320L, book, "Interest incomes"));
        initAccounts.add(new BalanceAccount(BSCategory.PROFIT, 390L, book, "Other incomes"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 411L, book, "Rent/mortgage expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 412L, book, "Phone expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 413L, book, "Electricity expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 414L, book, "Gas expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 415L, book, "Water and sewer expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 416L, book, "Internet expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 417L, book, "Housing supplies expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 419L, book, "Other expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 421L, book, "Bus fare expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 422L, book, "Taxi fare expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 423L, book, "Fuel expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 430L, book, "Insurance expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 441L, book, "Food expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 442L, book, "Dining out expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 443L, book, "Medical expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 444L, book, "Clothing expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 445L, book, "Gym expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 446L, book, "Organization due expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 447L, book, "Events/parties expenses"));
        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 448L, book, "Gifts expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 451L, book, "Loans expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 461L, book, "Pets expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 471L, book, "Children expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.LOSS, 490L, book, "Other expenses"));

        initAccounts.add(new BalanceAccount(BSCategory.EQUITY, 500L, book, "Start-up capital"));

        return initAccounts;
    }
}
