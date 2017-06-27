package ua.agwebs.web.rest.accounting;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.agwebs.root.entity.Currency;
import ua.agwebs.root.entity.EntryLine;
import ua.agwebs.root.entity.Transaction;
import ua.agwebs.root.entity.TransactionType;
import ua.agwebs.root.service.BusinessTransactionService;
import ua.agwebs.root.service.EntryService;
import ua.agwebs.web.exceptions.PocketBalanceIllegalAccessException;
import ua.agwebs.web.rest.PermissionService;
import ua.agwebs.web.rest.transactions.AccountingTransactionProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class AccountingProvider implements AccountingService {

    private static Logger logger = LoggerFactory.getLogger(AccountingProvider.class);

    private EntryService entryService;
    private BusinessTransactionService transactionService;
    private PermissionService permissionService;
    private ModelMapper mapper;

    @Autowired
    public AccountingProvider(EntryService entryService,
                              BusinessTransactionService businessTransactionService,
                              PermissionService permissionService,
                              ModelMapper mapper) {
        this.entryService = entryService;
        this.transactionService = businessTransactionService;
        this.permissionService = permissionService;
        this.mapper = mapper;
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

        if (permissionService.checkPermission(dto.getBookId(), userId)) {
            Transaction tran = transactionService.findTransactionById(dto.getTranId());
            Currency currency = entryService.findCurrencyById(dto.getCurrencyId());

            Set<EntryLine> entryLines = tran.getDetails().stream().map(e -> {
                return new EntryLine(-1, e.getAccount(), dto.getAmount(), e.getEntrySide(), currency);
            }).collect(toSet());

            long ln = 1;
            for (EntryLine e : entryLines) {
                e.setLineId(ln++);
            }

            entryService.createEntry(tran.getBook(), entryLines, dto.getDesc(), dto.getValueDate());
        } else {
            throw new PocketBalanceIllegalAccessException("Creating entry - permission denied: bookId = " + dto.getBookId() + ", userId = " + userId);
        }
    }

    @Override
    public List<CurrencyDTO> findAllCurrency() {
        logger.info("Select all currency.");
        Page<Currency> currencies = entryService.findAllCurrency(new PageRequest(0, 9999999, Sort.Direction.ASC, "code"));
        List<CurrencyDTO> currencyDTOs = currencies.getContent().stream().map(e -> mapper.map(e, CurrencyDTO.class)).collect(toList());
        return currencyDTOs;
    }

    @Override
    public List<AccountingTransactionDTO> findAllBookTransactionByType(long bookId, String type, long userId) {
        logger.info("Select transaction by type and book.");

        Assert.isTrue(isTransactionType(type));
        if (permissionService.checkPermission(bookId, userId)) {
            Page<Transaction> transactions
                    = transactionService.findAllBookTransactionByType(bookId, TransactionType.valueOf(type), new PageRequest(0, 10_000, Sort.Direction.ASC, "name"));

            List<AccountingTransactionDTO> transactionDTOs
                    = transactions.getContent()
                                    .stream()
                                    .map(e -> mapper.map(e, AccountingTransactionDTO.class))
                                    .collect(toList());
            return transactionDTOs;
        } else {
            throw new PocketBalanceIllegalAccessException("Creating entry - permission denied: bookId = " + bookId + ", userId = " + userId);
        }
    }

    private boolean isTransactionType(String string) {
        return Arrays.stream(TransactionType.values()).anyMatch(e -> e.name().equals(string));
    }
}
