package ua.agwebs.tests.unit.web.rest.accounts;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import ua.agwebs.root.entity.*;
import ua.agwebs.root.service.BusinessTransactionService;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.web.exceptions.PocketBalanceIllegalAccessException;
import ua.agwebs.web.rest.PermissionProvider;
import ua.agwebs.web.rest.PermissionService;
import ua.agwebs.web.rest.transactions.AccountingTransactionProvider;
import ua.agwebs.web.rest.transactions.TransactionDTO;
import ua.agwebs.web.rest.transactions.TransactionDetailDTO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockTestAccountingTransactionProvider {

    @Mock
    private CoaService coaService;

    @Mock
    private BusinessTransactionService transactionService;

    private AccountingTransactionProvider accountingTransactionProvider;

    private PermissionService permissionService;

    private ModelMapper mapper;

    private Transaction transaction;

    @Before
    public void setUpMock() {
        AppUser appUser = new AppUser("b@u.cn");
        appUser.setId(777L);
        BalanceBook book = new BalanceBook("test", "Mockito test", appUser);
        book.setId(10L);

        BalanceAccount accCt = new BalanceAccount(BSCategory.PROFIT, 700L, book, "test account");
        BalanceAccount accDt = new BalanceAccount(BSCategory.ASSET, 100L, book, "test account");

        transaction = new Transaction("Testing transaction", book);
        transaction.setId(7L);

        TransactionDetail detailCt = new TransactionDetail(transaction, accCt, EntrySide.C);
        TransactionDetail detailDt = new TransactionDetail(transaction, accDt, EntrySide.D);
        transaction.addDetails(detailCt);
        transaction.addDetails(detailDt);

        mapper = new ModelMapper();
        permissionService = new PermissionProvider(coaService);
        accountingTransactionProvider = new AccountingTransactionProvider(transactionService, mapper, permissionService);

    }

    @Test
    public void test_findTransactionAllByBookId() {
        when(coaService.findBalanceBookById(transaction.getBook().getId())).thenReturn(transaction.getBook());

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        when(transactionService.findAllTransactionByBookId(transaction.getBook().getId())).thenReturn(transactionList);

        TransactionDTO expectedDTO = mapper.map(transaction, TransactionDTO.class);

        List<TransactionDTO> resultDTOs =
                accountingTransactionProvider.findTransactionAllByBookId(transaction.getBook().getId(), transaction.getBook().getAppUser().getId());
        assertTrue("Incorrect result.", resultDTOs.contains(expectedDTO));

    }

    @Test(expected = PocketBalanceIllegalAccessException.class)
    public void test_findTransactionAllByBookId_AccessDenied() {
        when(coaService.findBalanceBookById(transaction.getBook().getId())).thenReturn(transaction.getBook());
        accountingTransactionProvider.findTransactionAllByBookId(transaction.getBook().getId(), transaction.getBook().getAppUser().getId() + 1);
    }

}
