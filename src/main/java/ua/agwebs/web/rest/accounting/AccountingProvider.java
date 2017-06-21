package ua.agwebs.web.rest.accounting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.agwebs.root.entity.Currency;
import ua.agwebs.root.entity.EntryLine;
import ua.agwebs.root.entity.Transaction;
import ua.agwebs.root.service.BusinessTransactionService;
import ua.agwebs.root.service.EntryService;
import ua.agwebs.web.exceptions.PocketBalanceIllegalAccessException;
import ua.agwebs.web.rest.PermissionService;
import ua.agwebs.web.rest.transactions.AccountingTransactionProvider;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class AccountingProvider implements AccountingService {

    private static Logger logger = LoggerFactory.getLogger(AccountingProvider.class);

    private EntryService entryService;
    private BusinessTransactionService transactionService;
    private PermissionService permissionService;

    @Autowired
    public AccountingProvider(EntryService entryService, BusinessTransactionService businessTransactionService, PermissionService permissionService) {
        this.entryService = entryService;
        this.transactionService = businessTransactionService;
        this.permissionService = permissionService;
    }

    @Override
    public void createEntry(AccountingDTO dto, long userId) {
        logger.debug("Create entry: dto = {}, userId = {}", dto, userId);

        Assert.notNull(dto);
        Assert.notNull(dto.getBookId(), "Book Id required.");
        Assert.notNull(dto.getTranId(), "Transaction Id required.");
        Assert.notNull(dto.getAmount(), "Amount required.");
        Assert.notNull(dto.getCurrencyId(), "Currency required.");
        Assert.notNull(dto.getValueDate(), "Entry value date required.");

        if(permissionService.checkPermission(dto.getBookId(), userId)){
            Transaction tran = transactionService.findTransactionById(dto.getTranId());
            Currency currency = entryService.findCurrencyById(dto.getCurrencyId());

            Set<EntryLine> entryLines = tran.getDetails().stream().map(e -> {
                return new EntryLine(-1, e.getAccount(), dto.getAmount(), e.getEntrySide(), currency);
            }).collect(toSet());

            long ln = 1;
            for(EntryLine e : entryLines){
                e.setLineId(ln++);
            }

            entryService.createEntry(tran.getBook(), entryLines, dto.getDesc(), dto.getValueDate());
        }else {
            throw new PocketBalanceIllegalAccessException("Creating entry - permission denied: bookId = " + dto.getBookId() + ", userId = " + userId);
        }
    }
}
