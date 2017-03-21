package ua.agwebs.root.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.entity.Transaction;
import ua.agwebs.root.entity.TransactionDetail;
import ua.agwebs.root.repo.TransactionDetailRepository;
import ua.agwebs.root.repo.TransactionRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.util.Set;

@Service
public class AccountingManager implements AccountingService {

    private static final Logger logger = LoggerFactory.getLogger(AccountingManager.class);

    private TransactionRepository tranRepo;

    private TransactionDetailRepository detRepo;

    @Autowired
    public AccountingManager(TransactionRepository tranRepo, TransactionDetailRepository detRepo) {
        this.tranRepo = tranRepo;
        this.detRepo = detRepo;
    }

    @Override
    public Transaction createTransaction(@Valid Transaction transaction) {
        logger.info("Create transaction.");
        logger.debug("Passed parameters: {}", transaction);

        Assert.isTrue(transaction.getId() == null, "Id should be null.");
        Assert.isTrue(!transaction.isDeleted(), "Can't create transaction with deleted status.");

        Transaction createdTransaction = tranRepo.save(transaction);

        logger.debug("Created transaction: {}", createdTransaction);
        return createdTransaction;
    }

    @Override
    public Transaction updateTransaction(@Valid Transaction transaction) {
        logger.info("Update transaction.");
        logger.debug("Passed parameters: {}", transaction);

        Assert.notNull(transaction.getId(), "Transaction Id required.");

        Transaction entity = tranRepo.findOne(transaction.getId());
        Assert.notNull(entity, "Transaction doesn't exist.");
        Assert.isTrue(!entity.isDeleted(), "Transaction doesn't exist.");

        Transaction updatedTransaction = tranRepo.save(transaction);

        logger.debug("Updated transaction: {}", updatedTransaction);
        return updatedTransaction;
    }

    @Override
    public void deleteTransaction(long id) {
        logger.info("Delete transaction.");
        logger.info("Passed parameters: {}", id);

        Transaction transaction = tranRepo.findOne(id);
        Assert.notNull(transaction, "Transaction doesn't exist.");
        Assert.isTrue(!transaction.isDeleted(), "Transaction doesn't exist.");

        transaction.setDeleted(true);
        transaction = tranRepo.save(transaction);

        logger.debug("Transaction deleted: {}", transaction);
    }

    @Override
    public TransactionDetail setTransactionDetail(@Valid TransactionDetail transactionDetail) {
        logger.info("Set transaction detail.");
        logger.debug("Passed parameters: {}", transactionDetail);

        Assert.notNull(transactionDetail);

        BalanceBook book = transactionDetail.getTransaction().getBook();
        BalanceAccount account = transactionDetail.getAccount();
        Assert.isTrue(book.getId().equals(account.getBook().getId()), "Transaction and balance account from different balance book.");

        TransactionDetail savedDetail = detRepo.save(transactionDetail);

        logger.debug("Saved transaction detail: {}", savedDetail);
        return savedDetail;
    }

    @Override
    public Transaction findTransactionById(long id) {
        logger.info("Select transaction.");
        logger.debug("Passed parameters: {}", id);

        Specification<Transaction> specification = new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.equal(root.get("id"), id);
                predicate = cb.and(predicate, cb.equal(root.get("deleted"), false));
                return predicate;
            }
        };

        Transaction transaction = tranRepo.findOne(specification);

        logger.debug("Selected transaction: {}", transaction);
        return transaction;
    }

    @Override
    public Page<Transaction> findAllTransaction(Pageable pageable) {
        logger.info("Select transaction.");
        logger.debug("Passed parameters: {}", pageable);

        Specification<Transaction> specification = new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.equal(root.get("deleted"), false);
                return predicate;
            }
        };

        Page<Transaction> page = tranRepo.findAll(specification, pageable);

        logger.debug("Balance accounts selected. Page {} from {}. Elements on this page {}. Total number of elements {}. ",
                page.getNumber(), page.getTotalPages(), page.getNumberOfElements(), page.getTotalElements());
        return page;
    }

    @Override
    public Set<TransactionDetail> findAllTransactionDetail(long transactionId) {
        return null;
    }
}
