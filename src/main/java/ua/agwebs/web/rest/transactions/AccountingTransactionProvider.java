package ua.agwebs.web.rest.transactions;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.agwebs.root.entity.Transaction;
import ua.agwebs.root.service.BusinessTransactionService;
import ua.agwebs.web.exceptions.PocketBalanceIllegalAccessException;
import ua.agwebs.web.rest.PermissionService;

import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountingTransactionProvider implements AccountingTransactionService {

    private static Logger logger = LoggerFactory.getLogger(AccountingTransactionProvider.class);

    private BusinessTransactionService transactionService;
    private ModelMapper mapper;
    private PermissionService permissionService;

    @Autowired
    public AccountingTransactionProvider(BusinessTransactionService businessTransactionService, ModelMapper mapper, PermissionService permissionService) {
        this.transactionService = businessTransactionService;
        this.mapper = mapper;
        this.permissionService = permissionService;
    }

    @Override
    public List<TransactionDTO> findTransactionAllByBookId(long bookId, long userId) {
        logger.debug("Find existing transactions by book id: bookId = {}, userId = {}", bookId, userId);
        if(permissionService.checkPermission(bookId, userId)){
            List<Transaction> transactions = transactionService.findAllTransactionByBookId(bookId);
            List<TransactionDTO> transactionDTOs = new ArrayList<>();
            for(Transaction e :  transactions){
                TransactionDTO dto = mapper.map(e, TransactionDTO.class);
                transactionDTOs.add(dto);
            }
            return transactionDTOs;
        }else {
            throw new PocketBalanceIllegalAccessException();
        }
    }
}
