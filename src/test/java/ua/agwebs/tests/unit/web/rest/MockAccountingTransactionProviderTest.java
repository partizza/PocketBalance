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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockAccountingTransactionProviderTest {

    @Mock
    private CoaService coaService;

    @Mock
    private BusinessTransactionService transactionService;

    private AccountingTransactionProvider accountingTransactionProvider;

    private PermissionService permissionService;

    private ModelMapper mapper;

    private Transaction transaction;

    private BalanceAccount accCt;

    private BalanceAccount accDt;

    @Before
    public void setUpMock() {
        AppUser appUser = new AppUser("b@u.cn");
        appUser.setId(777L);
        BalanceBook book = new BalanceBook("test", "Mockito test", appUser);
        book.setId(10L);

        accCt = new BalanceAccount(BSCategory.PROFIT, 700L, book, "test account");
        accDt = new BalanceAccount(BSCategory.ASSET, 100L, book, "test account");

        transaction = new Transaction("Testing transaction", book);
        transaction.setId(7L);

        TransactionDetail detailCt = new TransactionDetail(transaction, accCt, EntrySide.C);
        TransactionDetail detailDt = new TransactionDetail(transaction, accDt, EntrySide.D);
        transaction.addDetails(detailCt);
        transaction.addDetails(detailDt);

        mapper = new ModelMapper();
        permissionService = new PermissionProvider(coaService);
        accountingTransactionProvider = new AccountingTransactionProvider(transactionService, mapper, permissionService, coaService);

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

    @Test
    public void test_deleteTransaction() {
        when(transactionService.findTransactionById(transaction.getId())).thenReturn(transaction);
        when(coaService.findBalanceBookById(transaction.getBook().getId())).thenReturn(transaction.getBook());
        doNothing().when(transactionService).deleteTransaction(transaction.getId());

        accountingTransactionProvider.deleteTransactionById(transaction.getId(), transaction.getBook().getAppUser().getId());

        verify(transactionService, times(1)).findTransactionById(transaction.getId());
        verify(coaService, times(1)).findBalanceBookById(transaction.getBook().getId());
        verify(transactionService, times(1)).deleteTransaction(transaction.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_deleteTranstaction_NonExisting() {
        when(transactionService.findTransactionById(1)).thenReturn(null);
        accountingTransactionProvider.deleteTransactionById(1, 2);
    }

    @Test(expected = PocketBalanceIllegalAccessException.class)
    public void test_deleteTransaction_AccessDenied() {
        when(transactionService.findTransactionById(transaction.getId())).thenReturn(transaction);
        when(coaService.findBalanceBookById(transaction.getBook().getId())).thenReturn(transaction.getBook());

        accountingTransactionProvider.deleteTransactionById(transaction.getId(), transaction.getBook().getAppUser().getId() + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_CreateTransaction_NullDto() {
        accountingTransactionProvider.createTransaction(null, transaction.getBook().getAppUser().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_CreateTransaction_NullBookId() {
        TransactionDTO dto = new TransactionDTO();
        accountingTransactionProvider.createTransaction(dto, transaction.getBook().getAppUser().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_CreateTransaction_NullDetails() {
        TransactionDTO dto = new TransactionDTO();
        dto.setBookId(transaction.getBook().getId());
        dto.setDetails(null);
        accountingTransactionProvider.createTransaction(dto, transaction.getBook().getAppUser().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_CreateTransaction_EmptyDetails() {
        TransactionDTO dto = new TransactionDTO();
        dto.setBookId(transaction.getBook().getId());
        accountingTransactionProvider.createTransaction(dto, transaction.getBook().getAppUser().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_CreateTransaction_MissedDetails() {
        TransactionDTO dto = new TransactionDTO();
        dto.setBookId(transaction.getBook().getId());
        transaction.getDetails().stream().limit(1).forEach(e -> dto.addDetails(mapper.map(e, TransactionDetailDTO.class)));
        accountingTransactionProvider.createTransaction(dto, transaction.getBook().getAppUser().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_CreateTransaction_NullType(){
        TransactionDTO dto = new TransactionDTO();
        dto.setBookId(transaction.getBook().getId());
        transaction.getDetails().stream().forEach(e -> dto.addDetails(mapper.map(e, TransactionDetailDTO.class)));
        dto.setType(null);

        accountingTransactionProvider.createTransaction(dto, transaction.getBook().getAppUser().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_CreateTransaction_IncorrectType(){
        TransactionDTO dto = new TransactionDTO();
        dto.setBookId(transaction.getBook().getId());
        transaction.getDetails().stream().forEach(e -> dto.addDetails(mapper.map(e, TransactionDetailDTO.class)));
        dto.setType("blabla");

        accountingTransactionProvider.createTransaction(dto, transaction.getBook().getAppUser().getId());
    }

    @Test(expected = PocketBalanceIllegalAccessException.class)
    public void test_CreateTransaction_AccessDenied() {
        TransactionDTO dto = new TransactionDTO();
        dto.setBookId(transaction.getBook().getId());
        transaction.getDetails().stream().forEach(e -> dto.addDetails(mapper.map(e, TransactionDetailDTO.class)));
        dto.setType("OTHER");

        when(coaService.findBalanceBookById(transaction.getBook().getId())).thenReturn(transaction.getBook());
        accountingTransactionProvider.createTransaction(dto, transaction.getBook().getAppUser().getId() + 1);
    }

    @Test
    public void test_CreateTransaction() {
        TransactionDTO dto = new TransactionDTO();
        dto.setName(transaction.getName());
        dto.setDesc("testing example");
        dto.setBookId(transaction.getBook().getId());
        transaction.getDetails().stream().forEach(e -> dto.addDetails(mapper.map(e, TransactionDetailDTO.class)));
        dto.setType("OTHER");

        when(coaService.findBalanceBookById(transaction.getBook().getId())).thenReturn(transaction.getBook());
        when(transactionService.createTransaction(new Transaction(dto.getName(), transaction.getBook(), dto.getDesc()))).thenReturn(transaction);
        when(coaService.findBalanceAccountById(transaction.getBook().getId(), accCt.getAccId())).thenReturn(accCt);
        when(coaService.findBalanceAccountById(transaction.getBook().getId(), accDt.getAccId())).thenReturn(accDt);
        when(transactionService.setTransactionDetail(any(TransactionDetail.class))).thenReturn(null);

        accountingTransactionProvider.createTransaction(dto, transaction.getBook().getAppUser().getId());
        verify(transactionService, times(1)).createTransaction(any());
        verify(transactionService, times(2)).setTransactionDetail(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_updateTransaction_NullDto() {
        accountingTransactionProvider.updateTransaction(null, transaction.getBook().getAppUser().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_updateTransaction_NullDetails() {
        TransactionDTO dto = new TransactionDTO();
        dto.setBookId(transaction.getBook().getId());
        dto.setDetails(null);
        accountingTransactionProvider.updateTransaction(dto, transaction.getBook().getAppUser().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_updateTransaction_EmptyDetails() {
        TransactionDTO dto = new TransactionDTO();
        dto.setBookId(transaction.getBook().getId());
        accountingTransactionProvider.updateTransaction(dto, transaction.getBook().getAppUser().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_updateTransaction_MissedDetails() {
        TransactionDTO dto = new TransactionDTO();
        dto.setBookId(transaction.getBook().getId());
        transaction.getDetails().stream().limit(1).forEach(e -> dto.addDetails(mapper.map(e, TransactionDetailDTO.class)));
        accountingTransactionProvider.updateTransaction(dto, transaction.getBook().getAppUser().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_updateTransaction_MissedType() {
        TransactionDTO dto = new TransactionDTO();
        dto.setBookId(transaction.getBook().getId());
        transaction.getDetails().stream().forEach(e -> dto.addDetails(mapper.map(e, TransactionDetailDTO.class)));
        dto.setType(null);
        accountingTransactionProvider.updateTransaction(dto, transaction.getBook().getAppUser().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_updateTransaction_IncorectType() {
        TransactionDTO dto = new TransactionDTO();
        dto.setBookId(transaction.getBook().getId());
        transaction.getDetails().stream().forEach(e -> dto.addDetails(mapper.map(e, TransactionDetailDTO.class)));
        dto.setType("blabla");
        accountingTransactionProvider.updateTransaction(dto, transaction.getBook().getAppUser().getId());
    }

    @Test(expected = PocketBalanceIllegalAccessException.class)
    public void test_updateTransaction_AccessDenied() {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        transaction.getDetails().stream().forEach(e -> dto.addDetails(mapper.map(e, TransactionDetailDTO.class)));
        dto.setType("OTHER");

        when(transactionService.findTransactionById(dto.getId())).thenReturn(transaction);
        when(coaService.findBalanceBookById(transaction.getBook().getId())).thenReturn(transaction.getBook());
        accountingTransactionProvider.updateTransaction(dto, transaction.getBook().getAppUser().getId() + 1);
    }

    @Test
    public void test_updateTransaction() {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setName(transaction.getName());
        dto.setDesc("testing example");
        dto.setBookId(transaction.getBook().getId());
        transaction.getDetails().stream().forEach(e -> dto.addDetails(mapper.map(e, TransactionDetailDTO.class)));
        dto.setType("OTHER");

        when(coaService.findBalanceBookById(transaction.getBook().getId())).thenReturn(transaction.getBook());
        when(transactionService.findTransactionById(dto.getId())).thenReturn(transaction);
        when(transactionService.updateTransaction(any(Transaction.class))).thenReturn(transaction);
        doNothing().when(transactionService).deleteTransactionDetail(transaction.getId(), accCt.getAccId(), transaction.getBook().getId());
        doNothing().when(transactionService).deleteTransactionDetail(transaction.getId(), accDt.getAccId(), transaction.getBook().getId());
        when(coaService.findBalanceAccountById(transaction.getBook().getId(), accCt.getAccId())).thenReturn(accCt);
        when(coaService.findBalanceAccountById(transaction.getBook().getId(), accDt.getAccId())).thenReturn(accDt);
        when(transactionService.setTransactionDetail(any(TransactionDetail.class))).thenReturn(null);

        accountingTransactionProvider.updateTransaction(dto, transaction.getBook().getAppUser().getId());

        verify(transactionService, times(1)).findTransactionById(anyLong());
        verify(transactionService, times(2)).deleteTransactionDetail(anyLong(), anyLong(), anyLong());
        verify(transactionService, times(2)).setTransactionDetail(any());
    }

    @Test(expected = PocketBalanceIllegalAccessException.class)
    public void test_getGroupedBalanceAccounts_AccessDenied() {
        when(coaService.findBalanceBookById(transaction.getBook().getId())).thenReturn(transaction.getBook());
        accountingTransactionProvider.getGroupedBalanceAccountsByBookId(transaction.getBook().getId(), transaction.getBook().getAppUser().getId() + 1);
    }

    @Test
    public void test_getGroupedBalanceAccounts() {
        when(coaService.findBalanceBookById(transaction.getBook().getId())).thenReturn(transaction.getBook());
        accountingTransactionProvider.getGroupedBalanceAccountsByBookId(transaction.getBook().getId(), transaction.getBook().getAppUser().getId());
    }
}
