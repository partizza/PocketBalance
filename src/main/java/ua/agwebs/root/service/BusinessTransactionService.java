package ua.agwebs.root.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import ua.agwebs.root.entity.Transaction;
import ua.agwebs.root.entity.TransactionDetail;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Validated
public interface BusinessTransactionService {

    public Transaction createTransaction(@Valid Transaction transaction);

    public Transaction updateTransaction(@Valid Transaction transaction);

    public void deleteTransaction(long id);

    public TransactionDetail setTransactionDetail(@Valid TransactionDetail transactionDetail);

    public Transaction findTransactionById(long id);

    public Page<Transaction> findAllTransaction(Pageable pageable);

    public List<Transaction> findAllTransactionByBookId(long bookId);

    public List<TransactionDetail> findAllTransactionDetail(long transactionId);
}
