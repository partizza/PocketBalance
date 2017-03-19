package ua.agwebs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ua.agwebs.root.entity.BSCategory;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.entity.Transaction;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.root.service.TranService;

import static org.junit.Assert.assertEquals;

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
            delBook = coaService.createBalanceBook(new BalanceBook("del-book", "for testing"));

            dt = coaService.createBalanceAccount(new BalanceAccount(BSCategory.ASSET, 1002L, book, "Cash"));
            ct = coaService.createBalanceAccount(new BalanceAccount(BSCategory.PROFIT, 6000L, book, "Income"));

            setUpIsDone = true;
        }
    }

    // Create transaction
    // ** successfully
    @Test
    public void create_Transaction(){
        Transaction transaction = new Transaction("1234567890123456789012345", book);
        transaction.setDesc("123456789012345678901234567890123456789012345678901234567890");

        Transaction createdTransaction = tranService.createTransaction(transaction);

        assertEquals("Incorrect created transaction", transaction.getName(), createdTransaction.getName());
        assertEquals("Incorrect created transaction", transaction.getDesc(), createdTransaction.getDesc());
        assertEquals("Incorrect created transaction", transaction.getBook(), createdTransaction.getBook());
    }
}
