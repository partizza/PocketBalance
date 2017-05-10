package ua.agwebs.web.rest.transactions;


import java.util.List;

public interface AccountingTransactionService {

    List<TransactionDTO> findTransactionAllByBookId(long bookId, long userId);

    void deleteTransactionById(long tranId, long userId);
}
