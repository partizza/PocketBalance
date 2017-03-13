package ua.agwebs;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import ua.agwebs.root.service.CoaService;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class CoaServiceTests {

    @Autowired
    private CoaService coaService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // Create Balance book tests
    // ** reject
    @Test(expected = IllegalArgumentException.class)
    public void rejectCreationOf_BalanceBook_NullEntity() {
        coaService.createBalanceBook(null);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreationOf_BalanceBook_BlankName() {
        BalanceBook balanceBook = new BalanceBook(" ", "59");
        coaService.createBalanceBook(balanceBook);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreationOf_BalanceBook_NullName() {
        BalanceBook balanceBook = new BalanceBook(null, "59");
        coaService.createBalanceBook(balanceBook);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreationOf_BalanceBook_NameMaxLength() {
        BalanceBook balanceBook = new BalanceBook("12345678901234567890123456", "59");
        coaService.createBalanceBook(balanceBook);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreationOf_BalanceBook_DescMaxLength() {
        BalanceBook balanceBook = new BalanceBook("balance book", "1234567890123456789012345123456789012345678901234512345678901");
        coaService.createBalanceBook(balanceBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectCreationOf_BalanceBook_WithNotNullId() {
        BalanceBook balanceBook = new BalanceBook("my balance", "59");
        balanceBook.setId(10L);
        coaService.createBalanceBook(balanceBook);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreationOf_BalanceBook_NullDeleted() {
        BalanceBook balanceBook = new BalanceBook("my balance", "59");
        balanceBook.setDeleted(null);
        coaService.createBalanceBook(balanceBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectCreationOf_BalanceBook_DeletedStatus() {
        BalanceBook balanceBook = new BalanceBook("my balance", "59");
        balanceBook.setDeleted(true);
        coaService.createBalanceBook(balanceBook);
    }

    // Create Balance book tests
    // ** successfully
    @Test
    public void creationOf_BalanceBook() {
        BalanceBook balanceBook = new BalanceBook("1234567890123456789012345", "123456789012345678901234512345678901234567890123451234567890");
        BalanceBook savedBalanceBook = coaService.createBalanceBook(balanceBook);

        assertEquals("Incorrect balance book name.", balanceBook.getName(), savedBalanceBook.getName());
        assertEquals("Incorrect balance book desc.", balanceBook.getDesc(), savedBalanceBook.getDesc());
        assertEquals("Incorrect deleted status.", false, savedBalanceBook.isDeleted());
        assertNotNull("Balance book id missed.", savedBalanceBook.getId());
    }

    // Update balance book
    // ** reject
    @Test(expected = IllegalArgumentException.class)
    public void rejectUpdate_BalanceBook_NullEntity() {
        coaService.updateBalanceBook(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectUpdate_BalanceBook_WithoutId() {
        BalanceBook balanceBook = new BalanceBook("some book", "no desc");
        coaService.updateBalanceBook(balanceBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectUpdate_BalanceBook_NonExistingId() {
        BalanceBook balanceBook = new BalanceBook("some book", "no desc");
        balanceBook.setId(89L);
        coaService.updateBalanceBook(balanceBook);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_BalanceBook_NullName() {
        BalanceBook balanceBook = new BalanceBook("some book", "no desc");
        BalanceBook savedBook = coaService.createBalanceBook(balanceBook);
        savedBook.setName(null);
        coaService.updateBalanceBook(savedBook);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_BalanceBook_BlankName() {
        BalanceBook balanceBook = new BalanceBook("some book", "no desc");
        BalanceBook savedBook = coaService.createBalanceBook(balanceBook);
        savedBook.setName("");
        coaService.updateBalanceBook(savedBook);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_BalanceBook_NameMaxLength() {
        BalanceBook balanceBook = new BalanceBook("some book", "no desc");
        BalanceBook savedBook = coaService.createBalanceBook(balanceBook);
        savedBook.setName("12345678901234567890123456");
        coaService.updateBalanceBook(savedBook);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_BalanceBook_DescMaxLength() {
        BalanceBook balanceBook = new BalanceBook("some book", "no desc");
        BalanceBook savedBook = coaService.createBalanceBook(balanceBook);
        savedBook.setDesc("1234567890123456789012345123456789012345678901234512345678901");
        coaService.updateBalanceBook(savedBook);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_BalanceBook_NullDeleted() {
        BalanceBook balanceBook = new BalanceBook("some book", "no desc");
        BalanceBook savedBook = coaService.createBalanceBook(balanceBook);
        savedBook.setDeleted(null);
        coaService.updateBalanceBook(savedBook);
    }

    // Update balance book
    // ** successfully
    @Test
    public void update_BalanceBook() {
        BalanceBook balanceBook = new BalanceBook("some book", "no desc");
        balanceBook = coaService.createBalanceBook(balanceBook);
        balanceBook.setName("1234567890123456789012345");
        balanceBook.setDesc("123456789012345678901234512345678901234567890123451234567890");

        BalanceBook result = coaService.updateBalanceBook(balanceBook);
        assertEquals("Incorrect update balance book.", balanceBook, result);
    }

    // Find balance books
    @Test
    public void findBalanceBookById() {
        BalanceBook searchResult = coaService.findBalanceBookById(-1);
        assertNull("Incorrect returned object for nonexistent id.", searchResult);

        BalanceBook book = new BalanceBook("my book", "tested book");
        book = coaService.createBalanceBook(book);

        searchResult = coaService.findBalanceBookById(book.getId());
        assertEquals("Incorrect returned balance book.", book, searchResult);

        book.setDeleted(true);
        coaService.updateBalanceBook(book);
        searchResult = coaService.findBalanceBookById(book.getId());
        assertNull("Incorrect returned object for deleted book's id.", searchResult);

    }

    @Test
    public void findBalanceBookPage() {
        BalanceBook book = coaService.createBalanceBook(new BalanceBook("book", "tested book"));
        Page<BalanceBook> page = coaService.findAllBalanceBook(new PageRequest(0, 100000000));
        assertTrue("Incorrect result - balance book missed.", page.getContent().contains(book));

        book.setDeleted(true);
        page = coaService.findAllBalanceBook(new PageRequest(0, 100000000));
        assertFalse("Incorrect result - deleted balance book returned.", page.getContent().contains(book));
    }

    // Delete balance book
    // ** reject
    @Test(expected = IllegalArgumentException.class)
    public void rejectDelete_BalanceBook_NonexistentId() {
        coaService.deleteBalanceBook(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectDelete_BalanceBook_DeletedBookId() {
        BalanceBook book = coaService.createBalanceBook(new BalanceBook("book", "tested book"));
        coaService.deleteBalanceBook(book.getId());
        coaService.deleteBalanceBook(book.getId());
    }

    // Delete balance book
    // ** successfully
    @Test
    public void delete_BalanceBook() {
        BalanceBook book = coaService.createBalanceBook(new BalanceBook("book", "tested book"));
        coaService.deleteBalanceBook(book.getId());
        BalanceBook result = coaService.findBalanceBookById(book.getId());
        assertNull("Book is not deleted.", result);
    }

    // Create balance account
    // ** reject
    @Test(expected = IllegalArgumentException.class)
    public void rejectCreate_BalanceAccount_Null() {
        coaService.createBalanceAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectCreate_BalanceAccount_NullId() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));

        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, null, balanceBook, "cash");
        coaService.createBalanceAccount(balanceAccount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectCreate_BalanceAccount_NegativeId() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));

        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, -1L, balanceBook, "cash");
        coaService.createBalanceAccount(balanceAccount);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_BalanceAccount_NullBalanceBookId() {
        BalanceBook balanceBook = new BalanceBook("my book", "for testing");

        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash");
        coaService.createBalanceAccount(balanceAccount);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_BalanceAccount_NonExistedBalanceBook() {
        BalanceBook balanceBook = new BalanceBook("my book", "for testing");
        balanceBook.setId(-1L);

        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash");
        coaService.createBalanceAccount(balanceAccount);

    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_BalanceAccount_BlankName() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));

        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "");
        BalanceAccount resultedAccount = coaService.createBalanceAccount(balanceAccount);

    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_BalanceAccount_MaxSizeName() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));

        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "12345678901234567890123456");
        BalanceAccount resultedAccount = coaService.createBalanceAccount(balanceAccount);

    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_BalanceAccount_MaxSizeDesc() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));

        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash", "1234567890123456789012345678901234567890123456789012345678901");
        BalanceAccount resultedAccount = coaService.createBalanceAccount(balanceAccount);

    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_BalanceAccount_NullEnable() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));

        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash");
        balanceAccount.setEnable(null);
        BalanceAccount resultedAccount = coaService.createBalanceAccount(balanceAccount);

    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectCreate_BalanceAccount_ExistingAccount() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));

        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash");
        BalanceAccount resultedAccount = coaService.createBalanceAccount(balanceAccount);
        coaService.createBalanceAccount(balanceAccount);

    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_BalanceAccount_NullCategory() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));

        BalanceAccount balanceAccount = new BalanceAccount(null, 1002L, balanceBook, "cash");
        BalanceAccount resultedAccount = coaService.createBalanceAccount(balanceAccount);
        coaService.createBalanceAccount(balanceAccount);

    }

    // Create balance account
    // ** successfully
    @Test
    public void Create_BalanceAccount() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));

        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "1234567890123456789012345", "123456789012345678901234567890123456789012345678901234567890");
        BalanceAccount resultedAccount = coaService.createBalanceAccount(balanceAccount);

        assertEquals("Incorrect created balance account.", balanceAccount.getAccId(), resultedAccount.getAccId());
        assertEquals("Incorrect created balance account.", balanceAccount.getBook().getId(), resultedAccount.getBook().getId());
        assertEquals("Incorrect created balance account.", balanceAccount.getName(), resultedAccount.getName());
        assertEquals("Incorrect created balance account.", balanceAccount.getDesc(), resultedAccount.getDesc());
        assertEquals("Incorrect created balance account.", balanceAccount.isEnable(), resultedAccount.isEnable());

    }

    // Update balance account
    // ** reject
    @Test(expected = IllegalArgumentException.class)
    public void rejectUpdate_BalanceAccount_Null() {
        coaService.updateBalanceAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectUpdate_BalanceAccount_NullAccId() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));
        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash", "testing");
        BalanceAccount testedAccount = coaService.createBalanceAccount(balanceAccount);

        testedAccount.setAccId(null);
        coaService.updateBalanceAccount(testedAccount);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_BalanceAccount_NullCategory() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));
        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash", "testing");
        BalanceAccount testedAccount = coaService.createBalanceAccount(balanceAccount);

        testedAccount.setBsCategory(null);
        coaService.updateBalanceAccount(testedAccount);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_BalanceAccount_NonExistentBook() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));
        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash", "testing");
        BalanceAccount testedAccount = coaService.createBalanceAccount(balanceAccount);

        coaService.deleteBalanceBook(testedAccount.getBook().getId());
        coaService.updateBalanceAccount(testedAccount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectUpdate_BalanceAccount_IncorrectAccId() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));
        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash", "testing");
        BalanceAccount testedAccount = coaService.createBalanceAccount(balanceAccount);

        testedAccount.setAccId(-1L);
        coaService.updateBalanceAccount(testedAccount);

    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_BalanceAccount_NullName() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));
        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash", "testing");
        BalanceAccount testedAccount = coaService.createBalanceAccount(balanceAccount);

        testedAccount.setName(null);
        coaService.updateBalanceAccount(testedAccount);

    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_BalanceAccount_BlankName() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));
        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash", "testing");
        BalanceAccount testedAccount = coaService.createBalanceAccount(balanceAccount);

        testedAccount.setName("");
        coaService.updateBalanceAccount(testedAccount);

    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_BalanceAccount_MaxNameLength() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));
        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash", "testing");
        BalanceAccount testedAccount = coaService.createBalanceAccount(balanceAccount);

        testedAccount.setName("12345678901234567890123456");
        coaService.updateBalanceAccount(testedAccount);

    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_BalanceAccount_MaxDescLength() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));
        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash", "testing");
        BalanceAccount testedAccount = coaService.createBalanceAccount(balanceAccount);

        testedAccount.setDesc("1234567890123456789012345678901234567890123456789012345678901");
        coaService.updateBalanceAccount(testedAccount);

    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectUpdate_BalanceAccount_NullEnable() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));
        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash", "testing");
        BalanceAccount testedAccount = coaService.createBalanceAccount(balanceAccount);

        testedAccount.setEnable(null);
        coaService.updateBalanceAccount(testedAccount);

    }

    // Update balance account
    // ** successfully
    @Test
    public void update_BalanceAccout() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));
        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash", "testing");
        BalanceAccount testedAccount = coaService.createBalanceAccount(balanceAccount);

        testedAccount.setName("updated name");
        testedAccount.setDesc("updated desc");
        testedAccount.setEnable(false);
        BalanceAccount updatedAccount = coaService.updateBalanceAccount(testedAccount);

        assertEquals("Incorrect balance account update.", testedAccount.getAccId(), updatedAccount.getAccId());
        assertEquals("Incorrect balance account update.", testedAccount.getBook().getId(), updatedAccount.getBook().getId());
        assertEquals("Incorrect balance account update.", testedAccount.getName(), updatedAccount.getName());
        assertEquals("Incorrect balance account update.", testedAccount.getDesc(), updatedAccount.getDesc());
        assertEquals("Incorrect balance account update.", testedAccount.isEnable(), updatedAccount.isEnable());
    }

    // Find balance account
    @Test
    public void findBalanceAccountById() {
        BalanceAccount searchResult = coaService.findBalanceAccountById(-1, -1);
        assertNull("Incorrect returned object for nonexistent id.", searchResult);

        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));
        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash", "testing");
        BalanceAccount testedAccount = coaService.createBalanceAccount(balanceAccount);

        searchResult = coaService.findBalanceAccountById(testedAccount.getBook().getId(), testedAccount.getAccId());
        assertEquals("Incorrect returned balance book.", testedAccount, searchResult);
    }

    @Test
    public void findBalanceAccountPage() {
        BalanceBook balanceBook = coaService.createBalanceBook(new BalanceBook("my book", "for testing"));
        BalanceAccount balanceAccount = new BalanceAccount(BSCategory.ASSET, 1002L, balanceBook, "cash", "testing");
        BalanceAccount testedAccount = coaService.createBalanceAccount(balanceAccount);
        Page<BalanceAccount> page = coaService.findAllBalanceAccount(new PageRequest(0, 100000000));
        assertTrue("Incorrect result - balance book missed.", page.getContent().contains(testedAccount));
    }

}
