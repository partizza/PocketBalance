package ua.agwebs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ua.agwebs.root.entity.BSCategory;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.entity.Transaction;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.root.service.TranService;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransactionServiceTests {

    @Autowired
    private TranService tranService;

    @Autowired
    private CoaService coaService;

    private static boolean setUpIsDone = false;

    private static BalanceBook book;

    private static BalanceBook delBook;

    private static BalanceAccount dt;

    private static BalanceAccount ct;

    @Before
    public void init() {
        if (!setUpIsDone) {
            book = coaService.createBalanceBook(new BalanceBook("t-book", "for testing"));

            delBook = coaService.createBalanceBook(new BalanceBook("del-book", "disabled book"));
            delBook.setDeleted(true);
            coaService.deleteBalanceBook(delBook.getId());

            dt = coaService.createBalanceAccount(new BalanceAccount(BSCategory.ASSET, 1002L, book, "Cash"));
            ct = coaService.createBalanceAccount(new BalanceAccount(BSCategory.PROFIT, 6000L, book, "Income"));

            setUpIsDone = true;
        }
    }

    // Create transaction
    // ** successfully
    @Test
    public void create_Transaction() {
        Transaction transaction = new Transaction("1234567890123456789012345", book);
        transaction.setDesc("123456789012345678901234567890123456789012345678901234567890");

        Transaction createdTransaction = tranService.createTransaction(transaction);

        assertEquals("Incorrect created transaction", transaction.getName(), createdTransaction.getName());
        assertEquals("Incorrect created transaction", transaction.getDesc(), createdTransaction.getDesc());
        assertEquals("Incorrect created transaction", transaction.getBook(), createdTransaction.getBook());
    }

    // Create transaction
    // ** reject
    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Transaction_NullName() {
        tranService.createTransaction(new Transaction(null, book));
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Transaction_BlankName() {
        tranService.createTransaction(new Transaction("", book));
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Transaction_DescLength() {
        Transaction transaction = new Transaction("a tran", book);
        transaction.setDesc("1234567890123456789012345678901234567890123456789012345678901");
        tranService.createTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Transaction_NullDeletedFlag() {
        Transaction transaction = new Transaction("a tran", book);
        transaction.setDeleted(null);
        tranService.createTransaction(transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectCreate_Transaction_IsDeleted() {
        Transaction transaction = new Transaction("a tran", book);
        transaction.setDeleted(true);
        tranService.createTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Transaction_DisabledBook() {
        Transaction transaction = new Transaction("a tran", delBook);
        tranService.createTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Transaction_NullBook() {
        Transaction transaction = new Transaction("a tran", null);
        tranService.createTransaction(transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectCreate_Transaction_WithId() {
        Transaction transaction = new Transaction("a tran", book);
        transaction.setId(1L);
        tranService.createTransaction(transaction);
    }

    // Update transaction
    // ** successfully
    @Test
    public void update_Transaction() {
        Transaction transaction = tranService.createTransaction(new Transaction("a tran", book));

        transaction.setName("1234567890123456789012345");
        transaction.setDesc("123456789012345678901234567890123456789012345678901234567890");
        transaction.setDeleted(true);

        Transaction updatedTran = tranService.updateTransaction(transaction);

        assertEquals("Incorrect created transaction", transaction.getName(), updatedTran.getName());
        assertEquals("Incorrect created transaction", transaction.getDesc(), updatedTran.getDesc());
        assertEquals("Incorrect created transaction", transaction.isDeleted(), updatedTran.isDeleted());
    }

    // Update transaction
    // ** reject
    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_Transaction_NullName() {
        Transaction transaction = tranService.createTransaction(new Transaction("a tran", book));
        transaction.setName(null);
        Transaction updatedTran = tranService.updateTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_Transaction_BlankName() {
        Transaction transaction = tranService.createTransaction(new Transaction("a tran", book));
        transaction.setName("");
        Transaction updatedTran = tranService.updateTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_Transaction_NameLength() {
        Transaction transaction = tranService.createTransaction(new Transaction("a tran", book));
        transaction.setName("12345678901234567890123456");
        Transaction updatedTran = tranService.updateTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_Transaction_DescLength() {
        Transaction transaction = tranService.createTransaction(new Transaction("a tran", book));
        transaction.setDesc("1234567890123456789012345678901234567890123456789012345678901");
        Transaction updatedTran = tranService.updateTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_Transaction_NullDeletedFlag() {
        Transaction transaction = tranService.createTransaction(new Transaction("a tran", book));
        transaction.setDeleted(null);
        Transaction updatedTran = tranService.updateTransaction(transaction);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_Transaction_DisabledBook() {
        Transaction transaction = tranService.createTransaction(new Transaction("a tran", book));
        transaction.setBook(delBook);
        Transaction updatedTran = tranService.updateTransaction(transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectUpdate_Transaction_NullId() {
        Transaction transaction = tranService.createTransaction(new Transaction("a tran", book));
        transaction.setId(null);
        Transaction updatedTran = tranService.updateTransaction(transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectUpdate_Transaction_NonExistingId() {
        Transaction transaction = tranService.createTransaction(new Transaction("a tran", book));
        transaction.setId(-1L);
        Transaction updatedTran = tranService.updateTransaction(transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectUpdate_Transaction_Deleted() {
        Transaction transaction = tranService.createTransaction(new Transaction("a tran", book));
        transaction.setDeleted(true);
        transaction = tranService.updateTransaction(transaction);

        transaction.setName("new name");
        Transaction updatedTran = tranService.updateTransaction(transaction);
    }

    // Delete transaction
    // ** successfully
    @Test
    public void delete_Transaction() {
        Transaction transaction = tranService.createTransaction(new Transaction("a tran", book));

        Transaction result = tranService.findTransactionById(transaction.getId());
        assertNotNull("Doesn't saved transaction before delete.", result);

        tranService.deleteTransaction(transaction.getId());

        result = tranService.findTransactionById(transaction.getId());
        assertNull("Doesn't deleted transaction.", result);

    }

    // Delete transaction
    // ** reject
    @Test(expected = IllegalArgumentException.class)
    public void rejectDelete_Transaction_AlreadyDeleted() {
        Transaction transaction = tranService.createTransaction(new Transaction("a tran", book));

        tranService.deleteTransaction(transaction.getId());
        tranService.deleteTransaction(transaction.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectDelete_Transaction_NonExisting() {
        tranService.deleteTransaction(-1L);
    }

    // Select transaction
    @Test
    public void select_Transaction_ById() {
        Transaction first = tranService.createTransaction(new Transaction("first", book));
        Transaction second = tranService.createTransaction(new Transaction("second", book));

        tranService.deleteTransaction(second.getId());

        Transaction result = tranService.findTransactionById(first.getId());
        assertEquals("Incorrect select.", first.getId(), result.getId());

        result = tranService.findTransactionById(second.getId());
        assertNull("Incorrect select for deleted transaction.", result);
    }

    @Test
    public void select_Transaction_All(){
        Transaction first = tranService.createTransaction(new Transaction("first", book));
        Transaction second = tranService.createTransaction(new Transaction("second", book));

        tranService.deleteTransaction(second.getId());

        Page<Transaction> page = tranService.findAllTransaction(new PageRequest(0, 1000000));
        assertEquals("Incorrect select.", 0, page.getNumber());
        assertEquals("Incorrect select.", 1000000, page.getSize());
        assertTrue("Incorrect select.", page.getContent().contains(first));
        assertFalse("Incorrect select.", page.getContent().contains(second));

    }
}
