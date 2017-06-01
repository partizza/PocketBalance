package ua.agwebs.tests.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.agwebs.root.RootConfig;
import ua.agwebs.root.entity.*;
import ua.agwebs.web.rest.accounts.BalanceAccountDTO;
import ua.agwebs.web.rest.transactions.TransactionDTO;
import ua.agwebs.web.rest.transactions.TransactionDetailDTO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class DtoTests {

    @Autowired
    private ModelMapper mapper;

    @Test
    public void test_ModelMapper_BalanceAccountDTO() {
        AppUser appUser = new AppUser("bf@u.com", "An", "Xe");
        appUser.setId(999L);
        BalanceBook book = new BalanceBook("book", "some book for testing", appUser);
        book.setId(12L);
        BalanceAccount account = new BalanceAccount(BSCategory.PROFIT, 1002L, book, "Cash", "Cash in walet");

        BalanceAccountDTO accountDTO = mapper.map(account, BalanceAccountDTO.class);

        assertEquals("Incorrect DTO object.", account.getAccId(), accountDTO.getAccId());
        assertEquals("Incorrect DTO object.", account.getName(), accountDTO.getName());
        assertEquals("Incorrect DTO object.", account.getDesc(), accountDTO.getDesc());
        assertEquals("Incorrect DTO object.", account.isEnable(), accountDTO.getEnable());
        assertEquals("Incorrect DTO object.", account.getBook().getId(), accountDTO.getBookId());
        assertEquals("Incorrect DTO object.", account.getBsCategory().name(), accountDTO.getBsCategory());
    }

    @Test
    public void test_ModelMapper_TransactionDTO() {
        AppUser appUser = new AppUser("bf@u.com", "An", "Xe");
        appUser.setId(999L);

        BalanceBook book = new BalanceBook("book", "some book for testing", appUser);
        book.setId(12L);

        BalanceAccount accCr = new BalanceAccount(BSCategory.ASSET, 1001L, book, "Cash", "Cash in walet");
        BalanceAccount accDr = new BalanceAccount(BSCategory.LOSS, 3002L, book, "Food", "Cost of food");

        Transaction transaction = new Transaction("Buying food", book);
        transaction.setId(7L);

        TransactionDetail detailCr = new TransactionDetail(transaction, accCr, EntrySide.C);
        TransactionDetail detailDr = new TransactionDetail(transaction, accDr, EntrySide.D);
        transaction.addDetails(detailCr);
        transaction.addDetails(detailDr);

        TransactionDTO transactionDTO = mapper.map(transaction, TransactionDTO.class);

        assertEquals("Incorrect DTO object.", transaction.getId(), transactionDTO.getId());
        assertEquals("Incorrect DTO object.", transaction.getName(), transactionDTO.getName());
        assertEquals("Incorrect DTO object.", transaction.getDesc(), transactionDTO.getDesc());
        assertEquals("Incorrect DTO object.", transaction.getBook().getId(), transactionDTO.getBookId());
        assertEquals("Incorrect DTO object.", 2, transactionDTO.getDetails().size());

        TransactionDetailDTO detailCrDTO = mapper.map(detailCr, TransactionDetailDTO.class);
        assertTrue("Incorrect DTO object.", transactionDTO.getDetails().contains(detailCrDTO));

        TransactionDetailDTO detailDrDTO = mapper.map(detailDr, TransactionDetailDTO.class);
        assertTrue("Incorrect DTO object.", transactionDTO.getDetails().contains(detailDrDTO));
    }
}
