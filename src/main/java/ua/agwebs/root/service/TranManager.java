package ua.agwebs.root.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ua.agwebs.root.entity.Transaction;
import ua.agwebs.root.entity.TransactionDetail;
import ua.agwebs.root.repo.TransactionDetailRepository;
import ua.agwebs.root.repo.TransactionRepository;

import javax.validation.Valid;

@Service
public class TranManager implements TranService {

    private static final Logger logger = LoggerFactory.getLogger(TranManager.class);

    private TransactionRepository tranRepo;

    private TransactionDetailRepository detRepo;

    @Autowired
    public TranManager(TransactionRepository tranRepo, TransactionDetailRepository detRepo) {
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
        return null;
    }
}
