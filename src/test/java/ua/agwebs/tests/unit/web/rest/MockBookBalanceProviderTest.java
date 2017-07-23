package ua.agwebs.tests.unit.web.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.agwebs.root.entity.AppUser;
import ua.agwebs.root.entity.BSCategory;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.repo.BalanceLine;
import ua.agwebs.root.repo.ShortBalanceLine;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.root.service.EntryService;
import ua.agwebs.web.exceptions.PocketBalanceIllegalAccessException;
import ua.agwebs.web.rest.PermissionProvider;
import ua.agwebs.web.rest.PermissionService;
import ua.agwebs.web.rest.balance.BalanceSheetDTO;
import ua.agwebs.web.rest.balance.BookBalanceProvider;
import ua.agwebs.web.rest.balance.BookBalanceService;
import ua.agwebs.web.rest.balance.ColumnDefinition;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockBookBalanceProviderTest {

    @Mock
    private CoaService coaService;

    @Mock
    private EntryService entryService;

    private PermissionService permissionService;

    private BookBalanceService balanceService;

    private BalanceBook book;

    @Before
    public void setUpMock() {
        AppUser appUser = new AppUser("b@u.cn");
        appUser.setId(777L);
        book = new BalanceBook("test", "Mockito test", appUser);
        book.setId(10L);

        permissionService = new PermissionProvider(coaService);
        balanceService = new BookBalanceProvider(entryService, permissionService);
    }

    // Short book balance
    @Test
    public void test_getShortBookBalance() {
        List<ShortBalanceLine> lines = new ArrayList<>(Arrays.asList(
                new ShortBalanceLine(book.getId(), BSCategory.ASSET, "UAH", 10_000L),
                new ShortBalanceLine(book.getId(), BSCategory.ASSET, "USD", 1_000L),
                new ShortBalanceLine(book.getId(), BSCategory.EQUITY, "UAH", -10_000L),
                new ShortBalanceLine(book.getId(), BSCategory.EQUITY, "USD", -1_000L)
        ));

        LocalDate reportDate = LocalDate.now();

        when(coaService.findBalanceBookById(book.getId())).thenReturn(book);
        when(entryService.getShortBookBalance(book.getId(), reportDate)).thenReturn(lines);


        BalanceSheetDTO resultDTO = balanceService.getShortBookBalance(book.getId(), reportDate, book.getAppUser().getId());


        Assert.assertEquals("Incorrect book.", book.getId(), resultDTO.getBookId());
        Assert.assertEquals("Incorrect date.", reportDate, resultDTO.getReportDate());


        List<ColumnDefinition> columns = new ArrayList<>(Arrays.asList(
                new ColumnDefinition("#", false),
                new ColumnDefinition("Article", false),
                new ColumnDefinition("UAH", true),
                new ColumnDefinition("USD", true)
        ));
        Assert.assertTrue("Incorrect columns.", columns.equals(resultDTO.getColumns()));

        List<String[]> data = new ArrayList<>(Arrays.asList(
                new String[]{"1", "ASSET", "100.00", "10.00"},
                new String[]{"2", "LIABILITY", "0.00", "0.00"},
                new String[]{"3", "EQUITY", "-100.00", "-10.00"},
                new String[]{"4", "PROFIT", "0.00", "0.00"},
                new String[]{"5", "LOSS", "0.00", "0.00"}
        ));
        Assert.assertEquals("Incorrect size of result array.", data.size(), resultDTO.getData().size());
        for (int i = 0; i < data.size(); i++) {
            Assert.assertTrue("Incorrect balance line.", Arrays.equals(data.get(i), resultDTO.getData().get(i)));
        }

    }

    @Test(expected = PocketBalanceIllegalAccessException.class)
    public void test_getShortBalance_AccessDenied() {
        LocalDate reportDate = LocalDate.now();
        when(coaService.findBalanceBookById(book.getId())).thenReturn(book);

        BalanceSheetDTO resultDTO = balanceService.getShortBookBalance(book.getId(), reportDate, book.getAppUser().getId() + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getShortBalance_NullReportDate() {
        when(coaService.findBalanceBookById(book.getId())).thenReturn(book);
        BalanceSheetDTO resultDTO = balanceService.getShortBookBalance(book.getId(), null, book.getAppUser().getId());
    }

    // Book balance
    @Test
    public void test_getBookBalance() {
        List<BalanceLine> lines = new ArrayList<>(Arrays.asList(
                new BalanceLine(book.getId(), BSCategory.ASSET, "Cash", 1001L, "UAH", 10_000L),
                new BalanceLine(book.getId(), BSCategory.ASSET, "Cash", 1001L, "USD", 1_000L),
                new BalanceLine(book.getId(), BSCategory.EQUITY, "Equity", 5001L, "UAH", -10_000L),
                new BalanceLine(book.getId(), BSCategory.EQUITY, "Equity", 5001L, "USD", -1_000L)
        ));

        LocalDate reportDate = LocalDate.now();

        when(coaService.findBalanceBookById(book.getId())).thenReturn(book);
        when(entryService.getBookBalance(book.getId(), reportDate)).thenReturn(lines);


        BalanceSheetDTO resultDTO = balanceService.getBookBalance(book.getId(), reportDate, book.getAppUser().getId());


        Assert.assertEquals("Incorrect book.", book.getId(), resultDTO.getBookId());
        Assert.assertEquals("Incorrect date.", reportDate, resultDTO.getReportDate());


        List<ColumnDefinition> columns = new ArrayList<>(Arrays.asList(
                new ColumnDefinition("Category", false),
                new ColumnDefinition("AccountId", true, false),
                new ColumnDefinition("Account", false),
                new ColumnDefinition("UAH", true),
                new ColumnDefinition("USD", true)
        ));
        Assert.assertTrue("Incorrect columns.", columns.equals(resultDTO.getColumns()));

        List<String[]> data = new ArrayList<>(Arrays.asList(
                new String[]{"ASSET", "1001", "Cash", "100.00", "10.00"},
                new String[]{"EQUITY", "5001", "Equity", "-100.00", "-10.00"}
        ));
        Assert.assertEquals("Incorrect size of result array.", data.size(), resultDTO.getData().size());
        for (int i = 0; i < data.size(); i++) {
            Assert.assertTrue("Incorrect balance line.", Arrays.equals(data.get(i), resultDTO.getData().get(i)));
        }

    }

    @Test(expected = PocketBalanceIllegalAccessException.class)
    public void test_getBalance_AccessDenied() {
        LocalDate reportDate = LocalDate.now();
        when(coaService.findBalanceBookById(book.getId())).thenReturn(book);

        BalanceSheetDTO resultDTO = balanceService.getBookBalance(book.getId(), reportDate, book.getAppUser().getId() + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getBalance_NullReportDate() {
        when(coaService.findBalanceBookById(book.getId())).thenReturn(book);
        BalanceSheetDTO resultDTO = balanceService.getBookBalance(book.getId(), null, book.getAppUser().getId());
    }

}
