package ua.agwebs.web.rest.balance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ua.agwebs.root.repo.ShortBalanceLine;
import ua.agwebs.root.service.EntryService;
import ua.agwebs.web.exceptions.PocketBalanceIllegalAccessException;
import ua.agwebs.web.rest.PermissionService;
import ua.agwebs.web.rest.accounting.AccountingProvider;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookBalanceProvider implements BookBalanceService {

    private static Logger logger = LoggerFactory.getLogger(BookBalanceProvider.class);

    private EntryService entryService;
    private PermissionService permissionService;

    @Autowired
    public BookBalanceProvider(EntryService entryService, PermissionService permissionService) {
        this.entryService = entryService;
        this.permissionService = permissionService;
    }

    @Override
    public BalanceSheetDTO getShortBookBalance(long bookId, LocalDate reportDate, long userId) {
        logger.info("Calculate short book balance.");
        logger.debug("Passed parameters: bookId = {}, reportDate = {}, userId = {}", bookId, reportDate, userId);

        Assert.notNull(reportDate);

        if (permissionService.checkPermission(bookId, userId)) {
            List<ShortBalanceLine> balanceLines = entryService.getBookBalance(bookId, reportDate);

            return null;
        } else {
            throw new PocketBalanceIllegalAccessException("Creating entry - permission denied: bookId = " + bookId + ", userId = " + userId);
        }
    }
}

