package ua.agwebs.tests.unit.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import ua.agwebs.root.entity.*;
import ua.agwebs.root.service.BusinessTransactionService;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.root.service.EntryService;
import ua.agwebs.web.rest.PermissionProvider;
import ua.agwebs.web.rest.PermissionService;
import ua.agwebs.web.rest.accounting.AccountingDTO;
import ua.agwebs.web.rest.accounting.AccountingProvider;
import ua.agwebs.web.rest.accounting.AccountingService;
import ua.agwebs.web.rest.transactions.AccountDTO;
import ua.agwebs.web.rest.transactions.AccountingTransactionProvider;

import java.time.LocalDate;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockAccountingProviderTest {

    @Mock
    private CoaService coaService;

    @Mock
    private EntryService entryService;

    @Mock
    private BusinessTransactionService transactionService;

    private PermissionService permissionService;

    private AccountingService accountingService;

    private Transaction transaction;

    private BalanceAccount accCt;

    private BalanceAccount accDt;

    private BalanceBook book;

    private Currency currency;

    @Before
    public void setUpMock() {
        AppUser appUser = new AppUser("b@u.cn");
        appUser.setId(777L);
        book = new BalanceBook("test", "Mockito test", appUser);
        book.setId(10L);

        accCt = new BalanceAccount(BSCategory.PROFIT, 700L, book, "test account");
        accDt = new BalanceAccount(BSCategory.ASSET, 100L, book, "test account");

        transaction = new Transaction("Testing transaction", book);
        transaction.setId(7L);

        TransactionDetail detailCt = new TransactionDetail(transaction, accCt, EntrySide.C);
        TransactionDetail detailDt = new TransactionDetail(transaction, accDt, EntrySide.D);
        transaction.addDetails(detailCt);
        transaction.addDetails(detailDt);

        currency = new Currency(980, "UAH", "Hryvnya");

        permissionService = new PermissionProvider(coaService);
        accountingService = new AccountingProvider(entryService, transactionService, permissionService);

    }

    @Test
    public void test_createEntry() {
        AccountingDTO dto = new AccountingDTO();
        dto.setTranId(transaction.getId());
        dto.setBookId(book.getId());
        dto.setDesc("test desc");
        dto.setCurrencyId(980L);
        dto.setValueDate(LocalDate.now());
        dto.setAmount(100L);

        when(coaService.findBalanceBookById(transaction.getBook().getId())).thenReturn(book);
        when(transactionService.findTransactionById(transaction.getId())).thenReturn(transaction);
        when(entryService.findCurrencyById(currency.getId())).thenReturn(currency);
        when(entryService.createEntry(eq(book), anySetOf(EntryLine.class), eq(dto.getDesc()), eq(dto.getValueDate()))).thenReturn(null);

        accountingService.createEntry(dto, book.getAppUser().getId());
    }
}
