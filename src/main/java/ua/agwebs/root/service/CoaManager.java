package ua.agwebs.root.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceAccountId;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.repo.BalanceAccountRepository;
import ua.agwebs.root.repo.BalanceBookRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoaManager implements CoaService {


    private static final Logger logger = LoggerFactory.getLogger(CoaManager.class);


    private BalanceBookRepository bookRepo;

    private BalanceAccountRepository accountRepo;

    @Autowired
    public CoaManager(BalanceBookRepository balanceBookRepository, BalanceAccountRepository balanceAccountRepository) {
        Assert.notNull(balanceBookRepository);
        Assert.notNull(balanceAccountRepository);

        this.bookRepo = balanceBookRepository;
        this.accountRepo = balanceAccountRepository;
    }

    public BalanceBook createBalanceBook(BalanceBook balanceBook) {
        logger.info("Create a new balance book.");
        logger.debug("Passed parameters: {}", balanceBook);

        Assert.notNull(balanceBook);
        Assert.isNull(balanceBook.getId(), "Creation of balance book with specified Id is not allowed.");
        Assert.isTrue(!balanceBook.isDeleted(), "Can't create balance book with deleted status.");
        BalanceBook createdBalanceBook = bookRepo.save(balanceBook);

        logger.debug("Created balance book: {}", createdBalanceBook);
        return createdBalanceBook;
    }

    @Override
    public BalanceBook updateBalanceBook(BalanceBook balanceBook) {
        logger.info("Update a balance book");
        logger.debug("Passed parameters: {}", balanceBook);

        Assert.notNull(balanceBook);
        Assert.notNull(balanceBook.getId(), "Can't update. Balance book Id is required.");

        BalanceBook selectedBook = bookRepo.findOne(balanceBook.getId());
        Assert.notNull(selectedBook, "Can't update. Balance book doesn't exist.");
        Assert.isTrue(!selectedBook.isDeleted(), "Can't update deleted balance book.");
        BalanceBook updatedBalanceBook = bookRepo.save(balanceBook);

        logger.debug("Updated balance book: {}", updatedBalanceBook);
        return updatedBalanceBook;
    }

    @Override
    public void deleteBalanceBook(long id) {
        logger.info("Delete a balance book");
        logger.debug("Received balance book id: {}", id);

        BalanceBook balanceBook = bookRepo.findOne(id);
        Assert.notNull(balanceBook, "Balance book doesn't exist.");
        Assert.isTrue(!balanceBook.isDeleted(), "Balance book doesn't exist.");

        balanceBook.setDeleted(true);
        BalanceBook result = bookRepo.save(balanceBook);

        logger.debug("Deleted balance book: {}", result);
    }

    @Override
    public BalanceBook findBalanceBookById(long id) {
        logger.info("Find balance book by id");
        logger.debug("Received balance book id: {}", id);

        Specification<BalanceBook> specification = new Specification<BalanceBook>() {
            @Override
            public Predicate toPredicate(Root<BalanceBook> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.equal(root.get("deleted"), false);
                predicate = cb.and(predicate, cb.equal(root.get("id"), id));
                return predicate;
            }
        };

        BalanceBook balanceBook = bookRepo.findOne(specification);

        logger.debug("Selected balance book: {}", balanceBook);
        return balanceBook;
    }

    @Override
    public Page<BalanceBook> findAllBalanceBook(Pageable pageable) {
        logger.info("Find all balance book");
        logger.debug("Passed parameters: {}", pageable);

        Assert.notNull(pageable, "Pageable can't be null.");

        Specification<BalanceBook> specification = new Specification<BalanceBook>() {
            @Override
            public Predicate toPredicate(Root<BalanceBook> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.equal(root.get("deleted"), false);
                return predicate;
            }
        };

        Page<BalanceBook> page = bookRepo.findAll(specification, pageable);

        logger.debug("Balance books selected. Page {} from {}. Elements on this page {}. Total number of elements {}. ",
                page.getNumber(), page.getTotalPages(), page.getNumberOfElements(), page.getTotalElements());

        return page;
    }

    @Override
    public BalanceAccount createBalanceAccount(@Valid BalanceAccount balanceAccount) {
        logger.info("Create a balance account.");
        logger.debug("Passed parameters: {}", balanceAccount);

        Assert.notNull(balanceAccount);
        Assert.notNull(balanceAccount.getAccId(), "Balance account id required.");
        Assert.isTrue(balanceAccount.getAccId() > 0, "Balance account id should be positive value.");

        Assert.notNull(balanceAccount.getBook(), "Balance book required.");
        Long bookId = balanceAccount.getBook().getId();
        Assert.notNull(bookId, "Balance book id required.");

        boolean isExisting = accountRepo.exists(new BalanceAccountId(balanceAccount.getBook().getId(), balanceAccount.getAccId()));
        Assert.isTrue(!isExisting);

        BalanceAccount account = accountRepo.save(balanceAccount);
        logger.debug("Created balance account: {}", account);
        return account;
    }

    @Transactional
    @Override
    public List<BalanceAccount> createBalanceAccount(Iterable<BalanceAccount> balanceAccounts) {
        Assert.notNull(balanceAccounts);
        List<BalanceAccount> accounts = new ArrayList<>();
        for (BalanceAccount e : balanceAccounts) {
            accounts.add(this.createBalanceAccount(e));
        }
        return accounts;
    }

    @Override
    public BalanceAccount updateBalanceAccount(@Valid BalanceAccount balanceAccount) {
        logger.info("Update a balance account.");
        logger.debug("Passed parameters: {}", balanceAccount);

        Assert.notNull(balanceAccount);
        Assert.notNull(balanceAccount.getAccId(), "Balance account id required.");

        Assert.notNull(balanceAccount.getBook(), "Balance book required.");
        Long bookId = balanceAccount.getBook().getId();
        Assert.notNull(bookId, "Balance book id required.");

        BalanceAccountId balanceAccountId = new BalanceAccountId(balanceAccount.getBook().getId(), balanceAccount.getAccId());
        boolean accountExists = accountRepo.exists(balanceAccountId);
        Assert.isTrue(accountExists, "Balance account doesn't exist.");

        BalanceAccount updatedBalanceAccount = accountRepo.save(balanceAccount);
        logger.debug("Updated balance account: {}", updatedBalanceAccount);
        return updatedBalanceAccount;
    }

    @Override
    public Page<BalanceAccount> findAllBalanceAccount(Pageable pageable) {
        logger.info("Find all balance account");
        logger.debug("Passed parameters: {}", pageable);

        Assert.notNull(pageable, "Pageable can't be null.");

        Page<BalanceAccount> page = accountRepo.findAll(pageable);

        logger.debug("Balance accounts selected. Page {} from {}. Elements on this page {}. Total number of elements {}. ",
                page.getNumber(), page.getTotalPages(), page.getNumberOfElements(), page.getTotalElements());

        return page;
    }

    @Override
    public BalanceAccount findBalanceAccountById(long bookId, long accId) {
        logger.info("Find balance account by id");
        logger.debug("Received balance book id: bookId = {}, accId = {}", bookId, accId);

        BalanceAccount account = accountRepo.findOne(new BalanceAccountId(bookId, accId));

        logger.debug("Selected balance account: {}", account);
        return account;
    }

}
