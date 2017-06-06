package ua.agwebs.web.rest.transactions;


import java.util.List;
import java.util.Map;

public interface AccountingTransactionService {

    List<TransactionDTO> findTransactionAllByBookId(long bookId, long userId);

    void deleteTransactionById(long tranId, long userId);

    void createTransaction(TransactionDTO dto, long userId);

    void setTransactionDetails(TransactionDTO dto, long userId);

    Map<String,List<AccountDTO>> getGroupedBalanceAccountsByBookId(long bookId, long userId);
}
