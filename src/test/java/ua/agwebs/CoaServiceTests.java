package ua.agwebs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.service.CoaService;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CoaServiceTests {

    @Autowired
    private CoaService coaService;

    //Create Balance book tests
    // ** reject
    @Test(expected = IllegalArgumentException.class)
    public void rejectCreationOf_BalanceBook_NullEntity(){
        coaService.createBalanceBook(null);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreationOf_BalanceBook_BlankName(){
        BalanceBook balanceBook = new BalanceBook(" ","59");
        coaService.createBalanceBook(balanceBook);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreationOf_BalanceBook_NullName(){
        BalanceBook balanceBook = new BalanceBook(null,"59");
        coaService.createBalanceBook(balanceBook);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreationOf_BalanceBook_NameMaxLength(){
        BalanceBook balanceBook = new BalanceBook("12345678901234567890123456","59");
        coaService.createBalanceBook(balanceBook);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreationOf_BalanceBook_DescMaxLength(){
        BalanceBook balanceBook = new BalanceBook("balance book","1234567890123456789012345123456789012345678901234512345678901");
        coaService.createBalanceBook(balanceBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectCreationOf_BalanceBook_WithNotNullId(){
        BalanceBook balanceBook = new BalanceBook("my balance","59");
        balanceBook.setId(10L);
        coaService.createBalanceBook(balanceBook);
    }

    // ** successfully
    @Test
    public void creationOf_BalanceBook_succ(){
        BalanceBook balanceBook = new BalanceBook("1234567890123456789012345","123456789012345678901234512345678901234567890123451234567890");
        BalanceBook savedBalanceBook = coaService.createBalanceBook(balanceBook);

        assertEquals("Incorrect balance book name.", balanceBook.getName(), savedBalanceBook.getName());
        assertEquals("Incorrect balance book desc.", balanceBook.getDesc(), savedBalanceBook.getDesc());
        assertEquals("Incorrect deleted status.", false, savedBalanceBook.isDeleted());
        assertNotNull("Balance book id missed.", savedBalanceBook.getId());
    }

}
