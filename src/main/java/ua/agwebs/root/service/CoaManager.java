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
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.repo.BalanceBookRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        logger.info("Create a new balance book.");
        logger.debug("Passed parameters: {}", balanceBook);

        Assert.notNull(balanceBook);
        Assert.isNull(balanceBook.getId(), "Creation of balance book with specified Id is not allowed.");
        Assert.isTrue(!balanceBook.isDeleted(), "Can't create balance book with deleted status.");
        BalanceBook createdBalanceBook = repo.save(balanceBook);

        logger.debug("Created balance book: {}", createdBalanceBook);
        return createdBalanceBook;
    }

    @Override
    public BalanceBook updateBalanceBook(BalanceBook balanceBook) {
        logger.info("Update a balance book");
        logger.debug("Passed parameters: {}", balanceBook);

        Assert.notNull(balanceBook);
        Assert.notNull(balanceBook.getId(), "Can't update. Balance book Id is required.");

        BalanceBook selectedBook = repo.findOne(balanceBook.getId());
        Assert.notNull(selectedBook, "Can't update. Balance book doesn't exist.");
        Assert.isTrue(!selectedBook.isDeleted(), "Can't update deleted balance book.");
        BalanceBook updatedBalanceBook = repo.save(balanceBook);

        logger.debug("Created balance book: {}", updatedBalanceBook);
        return updatedBalanceBook;
    }

    @Override
    public void deleteBalanceBook(long id) {
        logger.info("Delete a balance book");
        logger.debug("Received balance book id: {}", id);

        BalanceBook balanceBook = repo.findOne(id);
        Assert.notNull(balanceBook, "Balance book doesn't exist.");
        Assert.isTrue(!balanceBook.isDeleted(), "Balance book doesn't exist.");

        balanceBook.setDeleted(true);
        BalanceBook result = repo.save(balanceBook);

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

        BalanceBook balanceBook = repo.findOne(specification);

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

        Page<BalanceBook> page = repo.findAll(specification, pageable);

        logger.debug("Balance books selected. Page {} from {}. Elements on this page {}. Total number of elements {}. ",
                page.getNumber(), page.getTotalPages(), page.getNumberOfElements(), page.getTotalElements());

        return page;
    }

    @Override
    public BalanceAccount createBalanceAccount(@Valid BalanceAccount balanceAccount) {
        return null;
    }
}
