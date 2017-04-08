package ua.agwebs.tests.integration.root;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ua.agwebs.root.entity.*;
import ua.agwebs.root.service.AppUserService;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.root.service.BusinessTransactionService;

import javax.validation.ConstraintViolationException;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode= DirtiesContext.ClassMode.BEFORE_CLASS)
public class BusinessTransactionServiceTests {

    private static boolean setUpIsDone = false;

    private static BalanceBook book;

    private static BalanceBook delBook;

    private static BalanceAccount dt;

    private static BalanceAccount ct;

    private static AppUser appUser;

    @Autowired
    private BusinessTransactionService businessTransactionService;

    @Autowired
    private AppUserService userService;

    @Autowired
    private CoaService coaService;

    @Before
    public void init() {
        if (!setUpIsDone) {
            appUser = userService.createAppUser(new AppUser("bf@u.com", "An", "Xe"));

            book = coaService.createBalanceBook(new BalanceBook("t-book", "for testing", appUser));

            dt = coaService.createBalanceAccount(new BalanceAccount(BSCategory.ASSET, 1002L, book, "Cash"));
            ct = coaService.createBalanceAccount(new BalanceAccount(BSCategory.PROFIT, 6000L, book, "Income"));

            delBook = coaService.createBalanceBook(new BalanceBook("del-book", "disabled book", appUser));
            delBook.setDeleted(true);
            coaService.deleteBalanceBook(delBook.getId());


            setUpIsDone = true;
        }
    }

    // Create transaction
    // ** successfully
    @Test
    public void create_Transaction() {
        Transaction transaction = new Transaction("1234567890123456789012345", book);
        transaction.setDesc("123456789012345678901234567890123456789012345678901234567890");

        Transaction createdTransaction = businessTransactionService.createTransaction(transaction);

        assertEquals("Incorrect created transaction", transaction.getName(), createdTransaction.getName());
        assertEquals("Incorrect created transaction", transaction.getDesc(), createdTransaction.getDesc());
        assertEquals("Incorrect created transaction", transaction.getBook(), createdTransaction.getBook());
    }

    // Create transaction
    // ** reject
    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Transaction_NullName() {
        businessTransactionService.createTransaction(new Transaction(null, book));
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Transaction_BlankName() {
        businessTransactionService.createTransaction(new Transaction("", book));
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Transaction_DescLength() {
        Transaction transaction = new Transaction("a tran", book);
        transaction.setDesc("1234567890123456789012345678901234567890123456789012345678901");
        businessTransactionService.createTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Transaction_NullDeletedFlag() {
        Transaction transaction = new Transaction("a tran", book);
        transaction.setDeleted(null);
        businessTransactionService.createTransaction(transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectCreate_Transaction_IsDeleted() {
        Transaction transaction = new Transaction("a tran", book);
        transaction.setDeleted(true);
        businessTransactionService.createTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Transaction_DisabledBook() {
        Transaction transaction = new Transaction("a tran", delBook);
        businessTransactionService.createTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Transaction_NullBook() {
        Transaction transaction = new Transaction("a tran", null);
        businessTransactionService.createTransaction(transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectCreate_Transaction_WithId() {
        Transaction transaction = new Transaction("a tran", book);
        transaction.setId(1L);
        businessTransactionService.createTransaction(transaction);
    }

    // Update transaction
    // ** successfully
    @Test
    public void update_Transaction() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("a tran", book));

        transaction.setName("1234567890123456789012345");
        transaction.setDesc("123456789012345678901234567890123456789012345678901234567890");
        transaction.setDeleted(true);

        Transaction updatedTran = businessTransactionService.updateTransaction(transaction);

        assertEquals("Incorrect created transaction", transaction.getName(), updatedTran.getName());
        assertEquals("Incorrect created transaction", transaction.getDesc(), updatedTran.getDesc());
        assertEquals("Incorrect created transaction", transaction.isDeleted(), updatedTran.isDeleted());
    }

    // Update transaction
    // ** reject
    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_Transaction_NullName() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("a tran", book));
        transaction.setName(null);
        Transaction updatedTran = businessTransactionService.updateTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_Transaction_BlankName() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("a tran", book));
        transaction.setName("");
        Transaction updatedTran = businessTransactionService.updateTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_Transaction_NameLength() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("a tran", book));
        transaction.setName("12345678901234567890123456");
        Transaction updatedTran = businessTransactionService.updateTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_Transaction_DescLength() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("a tran", book));
        transaction.setDesc("1234567890123456789012345678901234567890123456789012345678901");
        Transaction updatedTran = businessTransactionService.updateTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_Transaction_NullDeletedFlag() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("a tran", book));
        transaction.setDeleted(null);
        Transaction updatedTran = businessTransactionService.updateTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_Transaction_DisabledBook() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("a tran", book));
        transaction.setBook(delBook);
        Transaction updatedTran = businessTransactionService.updateTransaction(transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectUpdate_Transaction_NullId() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("a tran", book));
        transaction.setId(null);
        Transaction updatedTran = businessTransactionService.updateTransaction(transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectUpdate_Transaction_NonExistingId() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("a tran", book));
        transaction.setId(-1L);
        Transaction updatedTran = businessTransactionService.updateTransaction(transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectUpdate_Transaction_Deleted() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("a tran", book));
        transaction.setDeleted(true);
        transaction = businessTransactionService.updateTransaction(transaction);

        transaction.setName("new name");
        Transaction updatedTran = businessTransactionService.updateTransaction(transaction);
    }

    // Delete transaction
    // ** successfully
    @Test
    public void delete_Transaction() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("a tran", book));

        Transaction result = businessTransactionService.findTransactionById(transaction.getId());
        assertNotNull("Doesn't saved transaction before delete.", result);

        businessTransactionService.deleteTransaction(transaction.getId());

        result = businessTransactionService.findTransactionById(transaction.getId());
        assertNull("Doesn't deleted transaction.", result);

    }

    // Delete transaction
    // ** reject
    @Test(expected = IllegalArgumentException.class)
    public void rejectDelete_Transaction_AlreadyDeleted() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("a tran", book));

        businessTransactionService.deleteTransaction(transaction.getId());
        businessTransactionService.deleteTransaction(transaction.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectDelete_Transaction_NonExisting() {
        businessTransactionService.deleteTransaction(-1L);
    }

    // Select transaction
    @Test
    public void select_Transaction_ById() {
        Transaction first = businessTransactionService.createTransaction(new Transaction("first", book));
        Transaction second = businessTransactionService.createTransaction(new Transaction("second", book));

        businessTransactionService.deleteTransaction(second.getId());

        Transaction result = businessTransactionService.findTransactionById(first.getId());
        assertEquals("Incorrect select.", first.getId(), result.getId());

        result = businessTransactionService.findTransactionById(second.getId());
        assertNull("Incorrect select for deleted transaction.", result);
    }

    @Test
    public void select_Transaction_All() {
        Transaction first = businessTransactionService.createTransaction(new Transaction("first", book));
        Transaction second = businessTransactionService.createTransaction(new Transaction("second", book));

        businessTransactionService.deleteTransaction(second.getId());

        Page<Transaction> page = businessTransactionService.findAllTransaction(new PageRequest(0, 1000000));
        assertEquals("Incorrect select.", 0, page.getNumber());
        assertEquals("Incorrect select.", 1000000, page.getSize());
        assertTrue("Incorrect select.", page.getContent().contains(first));
        assertFalse("Incorrect select.", page.getContent().contains(second));

    }

    // Set transaction detail
    // ** successfully
    @Test
    public void test_setTransactionDetail() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("test", book));

        TransactionDetail detail = new TransactionDetail(transaction, ct, EntrySide.D);
        TransactionDetail result = businessTransactionService.setTransactionDetail(detail);

        assertEquals("Incorrect saved transaction detail.", detail.getTransaction().getId(), result.getTransaction().getId());
        assertEquals("Incorrect saved transaction detail.", detail.getAccount().getAccId(), result.getAccount().getAccId());
        assertEquals("Incorrect saved transaction detail.", detail.getEntrySide(), result.getEntrySide());
        assertEquals("Incorrect saved transaction detail.", detail.isEnable(), result.isEnable());
    }

    // Set transaction detail
    // ** reject
    @Test(expected = IllegalArgumentException.class)
    public void test_setTransactionDetail_null() {
        TransactionDetail result = businessTransactionService.setTransactionDetail(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setTransactionDetail_DiffBooks() {
        BalanceBook dBook = coaService.createBalanceBook(new BalanceBook("d-book", "testing",appUser));
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("test", dBook));

        TransactionDetail detail = new TransactionDetail(transaction, ct, EntrySide.D);
        TransactionDetail result = businessTransactionService.setTransactionDetail(detail);
    }

    @Test(expected = ConstraintViolationException.class)
    public void test_setTransactionDetail_disabledAccount() {
        BalanceAccount acc = new BalanceAccount(BSCategory.PROFIT, 123456789L, book, "Income");
        acc.setEnable(false);
        BalanceAccount disabledAccount = coaService.createBalanceAccount(acc);

        Transaction transaction = businessTransactionService.createTransaction(new Transaction("test", book));

        TransactionDetail detail = new TransactionDetail(transaction, disabledAccount, EntrySide.D);
        TransactionDetail result = businessTransactionService.setTransactionDetail(detail);
    }

    @Test(expected = ConstraintViolationException.class)
    public void test_setTransactionDetail_NonExistingAccount() {
        BalanceAccount nonExistingAccount = new BalanceAccount(BSCategory.PROFIT, 123456789L, book, "Income");

        Transaction transaction = businessTransactionService.createTransaction(new Transaction("test", book));

        TransactionDetail detail = new TransactionDetail(transaction, nonExistingAccount, EntrySide.D);
        TransactionDetail result = businessTransactionService.setTransactionDetail(detail);
    }

    @Test(expected = ConstraintViolationException.class)
    public void test_setTransactionDetail_NonExistingTransaction() {
        Transaction nonExistingTransaction = new Transaction("test", book);

        TransactionDetail detail = new TransactionDetail(nonExistingTransaction, ct, EntrySide.D);
        TransactionDetail result = businessTransactionService.setTransactionDetail(detail);
    }

    @Test(expected = ConstraintViolationException.class)
    public void test_setTransactionDetail_NullEntrySide() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("test", book));

        TransactionDetail detail = new TransactionDetail(transaction, ct, null);
        TransactionDetail result = businessTransactionService.setTransactionDetail(detail);
    }

    @Test(expected = ConstraintViolationException.class)
    public void test_setTransactionDetail_NullEnable() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("test", book));

        TransactionDetail detail = new TransactionDetail(transaction, ct, EntrySide.D);
        detail.setEnable(null);
        TransactionDetail result = businessTransactionService.setTransactionDetail(detail);
    }

    // Select transaction details
    @Test
    public void test_selectTransactionDetails() {
        Transaction transaction = businessTransactionService.createTransaction(new Transaction("test", book));

        TransactionDetail first = businessTransactionService.setTransactionDetail(new TransactionDetail(transaction, ct, EntrySide.D));
        TransactionDetail second = businessTransactionService.setTransactionDetail(new TransactionDetail(transaction, dt, EntrySide.D));

        second.setEnable(false);
        businessTransactionService.setTransactionDetail(second);

        List<TransactionDetail> result = businessTransactionService.findAllTransactionDetail(transaction.getId());

        assertTrue("Missed transaction detail.", result.contains(first));
        assertFalse("Unexpected transaction detail.", result.contains(second));

    }

}
