package ua.agwebs.root.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.repo.BalanceBookRepository;

import javax.validation.Valid;

@Service
public class CoaManager implements CoaService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    private BalanceBookRepository repo;

    @Autowired
    public CoaManager(BalanceBookRepository balanceBookRepository) {
        this.repo = balanceBookRepository;
    }

    @Override
    public BalanceBook createBalanceBook(BalanceBook balanceBook) {
        logger.info("Creation of new balance book.");
        logger.debug("Received params: {}", balanceBook);

        Assert.notNull(balanceBook);
        Assert.isNull(balanceBook.getId(), "Creation of balance book with specified Id is not allowed.");
        BalanceBook createdBalanceBook = repo.save(balanceBook);

        logger.debug("Created balance book: {}", createdBalanceBook);
        return createdBalanceBook;
    }

    @Override
    public BalanceBook updateBalanceBook(BalanceBook balanceBook) {
        logger.info("Updating of balance book.");
        logger.debug("Received params: {}", balanceBook);

        Assert.notNull(balanceBook);
        Assert.notNull(balanceBook.getId(), "Can't update. Balance book Id is required.");
        Assert.isTrue(repo.exists(balanceBook.getId()), "Can't update. Balance book doesn't exist.");
        BalanceBook updatedBalanceBook = repo.save(balanceBook);

        logger.debug("Created balance book: {}", updatedBalanceBook);
        return updatedBalanceBook;
    }

    @Override
    public BalanceBook mergeBalanceBook(BalanceBook balanceBook) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteBalanceBook(long id) {

    }

    @Override
    public BalanceBook findBalanceBookById(long id) {
        return null;
    }

    @Override
    public Page<BalanceBook> findAllBalanceBook(Specification<BalanceBook> specification, Pageable pageable) {
        return null;
    }
}
