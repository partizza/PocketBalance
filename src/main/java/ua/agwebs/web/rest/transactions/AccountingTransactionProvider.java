package ua.agwebs.web.rest.transactions;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.agwebs.root.entity.*;
import ua.agwebs.root.service.BusinessTransactionService;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.web.exceptions.PocketBalanceIllegalAccessException;
import ua.agwebs.web.rest.PermissionService;

import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
public class AccountingTransactionProvider implements AccountingTransactionService {

    private static Logger logger = LoggerFactory.getLogger(AccountingTransactionProvider.class);

    private BusinessTransactionService transactionService;
    private ModelMapper mapper;
    private PermissionService permissionService;
    private CoaService coaService;

    @Autowired
    public AccountingTransactionProvider(BusinessTransactionService businessTransactionService,
                                         ModelMapper mapper,
                                         PermissionService permissionService,
                                         CoaService coaService) {
        this.transactionService = businessTransactionService;
        this.mapper = mapper;
        this.permissionService = permissionService;
        this.coaService = coaService;
    }

    @Override
    public List<TransactionDTO> findTransactionAllByBookId(long bookId, long userId) {
        logger.debug("Find existing transactions by book id: bookId = {}, userId = {}", bookId, userId);
        if (permissionService.checkPermission(bookId, userId)) {
            List<Transaction> transactions = transactionService.findAllTransactionByBookId(bookId);
            List<TransactionDTO> transactionDTOs = new ArrayList<>();
            for (Transaction e : transactions) {
                TransactionDTO dto = mapper.map(e, TransactionDTO.class);
                transactionDTOs.add(dto);
            }
            return transactionDTOs;
        } else {
            throw new PocketBalanceIllegalAccessException("Permission denied: bookId = " + bookId + ", userId = " + userId);
        }
    }

    @Override
    public void deleteTransactionById(long tranId, long userId) {
        logger.debug("Delete transaction: tranId = {}, userId = {}", tranId, userId);
        Transaction transaction = transactionService.findTransactionById(tranId);

        if (transaction == null) {
            throw new IllegalArgumentException("Non existing transaction ID");
        }

        if (permissionService.checkPermission(transaction.getBook().getId(), userId)) {
            transactionService.deleteTransaction(tranId);
        } else {
            throw new PocketBalanceIllegalAccessException("Permission denied: transactionId = " + tranId + ", userId = " + userId);
        }
    }

    @Transactional
    @Override
    public void createTransaction(TransactionDTO dto, long userId) {
        logger.debug("Create new transaction: {}", dto);

        Assert.notNull(dto);
        Assert.notNull(dto.getBookId());
        Assert.notNull(dto.getDetails());
        Assert.isTrue(dto.getDetails().size() == 2);

        if (permissionService.checkPermission(dto.getBookId(), userId)) {
            BalanceBook book = coaService.findBalanceBookById(dto.getBookId());
            Transaction tran = transactionService.createTransaction(new Transaction(dto.getName(), book, dto.getDesc()));

            dto.getDetails().stream().forEach(e -> {
                BalanceAccount account = coaService.findBalanceAccountById(book.getId(), e.getAccountAccId());
                TransactionDetail tDet = new TransactionDetail(tran, account, EntrySide.valueOf(e.getEntrySide()));
                transactionService.setTransactionDetail(tDet);
            });
        } else {
            throw new PocketBalanceIllegalAccessException("Permission denied: bookId = " + dto.getBookId() + ", userId = " + userId);
        }
    }

    @Transactional
    @Override
    public void setTransactionDetails(TransactionDTO dto, long userId) {
        logger.debug("Set transaction details: {}", dto);

        Assert.notNull(dto);
        Assert.notNull(dto.getBookId());
        Assert.notNull(dto.getDetails());
        Assert.isTrue(dto.getDetails().size() == 2);

        if (permissionService.checkPermission(dto.getBookId(), userId)) {
            Transaction tran = transactionService.findTransactionById(dto.getId());
            tran.getDetails().stream().forEach(e -> transactionService.deleteTransactionDetail(tran.getId(), e.getCoaId(), e.getBookId()));

            dto.getDetails().stream().forEach(e -> {
                BalanceAccount account = coaService.findBalanceAccountById(dto.getBookId(), e.getAccountAccId());
                TransactionDetail tDet = new TransactionDetail(tran, account, EntrySide.valueOf(e.getEntrySide()));
                transactionService.setTransactionDetail(tDet);
            });
        } else {
            throw new PocketBalanceIllegalAccessException("Permission denied: bookId = " + dto.getBookId() + ", userId = " + userId);
        }
    }

    @Override
    public Map<String, List<AccountDTO>> getGroupedBalanceAccountsByBookId(long bookId, long userId) {
        logger.debug("Get grouped balance accounts: bookId = {}, userId = {} ", bookId, userId);
        if (permissionService.checkPermission(bookId, userId)) {
            BalanceBook book = coaService.findBalanceBookById(bookId);
            Map<String, List<AccountDTO>> accountsByCategories = book.getAccounts()
                    .stream()
                    .collect(groupingBy(e -> e.getBsCategory().toString(),
                            collectingAndThen(toList(), l -> l.stream().sorted((e1,e2) -> e1.getName().compareTo(e2.getName())).collect(mapping(e -> mapper.map(e,AccountDTO.class),toList())))));
            return accountsByCategories;
        } else {
            throw new PocketBalanceIllegalAccessException("Permission denied: bookId = " + bookId + ", userId = " + userId);
        }
    }
}
