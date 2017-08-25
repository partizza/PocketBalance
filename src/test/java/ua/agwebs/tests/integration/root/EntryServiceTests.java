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
import ua.agwebs.root.entity.Currency;
import ua.agwebs.root.repo.BalanceLine;
import ua.agwebs.root.repo.EntryLineRepository;
import ua.agwebs.root.repo.ShortBalanceLine;
import ua.agwebs.root.service.AppUserService;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.root.service.EntryService;
import ua.agwebs.root.service.specifications.SearchCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class EntryServiceTests {

    private static boolean setUpIsDone = false;

    private static BalanceBook book;

    private static BalanceAccount dt;

    private static BalanceAccount ct;

    private static Currency uah;

    private static Currency usd;

    private static AppUser appUser;

    @Autowired
    private AppUserService userService;

    @Autowired
    private EntryService entryService;

    @Autowired
    private CoaService coaService;

    @Autowired
    private EntryLineRepository lineRepo;

    @Before
    public void init() {
        if (!setUpIsDone) {
            appUser = userService.createAppUser(new AppUser("bf@u.com", "An", "Xe"));

            book = coaService.createBalanceBook(new BalanceBook("t-book", "for testing", appUser));
            dt = coaService.createBalanceAccount(new BalanceAccount(BSCategory.ASSET, 1002L, book, "Cash"));
            ct = coaService.createBalanceAccount(new BalanceAccount(BSCategory.PROFIT, 6000L, book, "Income"));
            uah = entryService.findCurrencyById(980);
            usd = entryService.findCurrencyById(840);

            setUpIsDone = true;
        }
    }

    // calculate book balance
    @Test
    public void calc_BookBalance() {
        BalanceBook book2 = coaService.createBalanceBook(new BalanceBook("t-book", "balance book test", appUser));
        BalanceAccount dt2 = coaService.createBalanceAccount(new BalanceAccount(BSCategory.ASSET, 1002L, book2, "Cash"));
        BalanceAccount ct2 = coaService.createBalanceAccount(new BalanceAccount(BSCategory.PROFIT, 6000L, book2, "Income"));

        BalanceAccount dt3 = coaService.createBalanceAccount(new BalanceAccount(BSCategory.LIABILITY, 3001L, book2, "Other"));

        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt2, 1000L, EntrySide.D, uah));
        lines.add(new EntryLine(2, ct2, -1000L, EntrySide.C, uah));

        lines.add(new EntryLine(3, dt2, 2000L, EntrySide.D, usd));
        lines.add(new EntryLine(4, ct2, -2000L, EntrySide.C, usd));

        lines.add(new EntryLine(5, dt3, 1000L, EntrySide.D, uah));
        lines.add(new EntryLine(6, dt2, -1000L, EntrySide.C, uah));

        EntryHeader header = entryService.createEntry(book2, lines, "book balance test", LocalDate.parse("2020-12-31"));

        // short balance check
        List<ShortBalanceLine> shBalance = entryService.getShortBookBalance(book2.getId(), LocalDate.parse("2020-12-31"));

        assertFalse("Incorrect short balance: only non-zero amount should be selected", shBalance.contains(new ShortBalanceLine(book2.getId(), dt2.getBsCategory(), uah.getCode(), 0L)));
        assertTrue("Incorrect short balance in UAH on Cr", shBalance.contains(new ShortBalanceLine(book2.getId(), ct2.getBsCategory(), uah.getCode(), -1_000L)));
        assertTrue("Incorrect short balance in USD on Dt", shBalance.contains(new ShortBalanceLine(book2.getId(), dt2.getBsCategory(), usd.getCode(), 2_000L)));
        assertTrue("Incorrect short balance in USD on Cr", shBalance.contains(new ShortBalanceLine(book2.getId(), ct2.getBsCategory(), usd.getCode(), -2_000L)));
        assertTrue("Incorrect short balance in UAH on Dt", shBalance.contains(new ShortBalanceLine(book2.getId(), dt3.getBsCategory(), uah.getCode(), 1_000L)));

        // balance check
        List<BalanceLine> fBalance = entryService.getBookBalance(book2.getId(), LocalDate.parse("2020-12-31"));
        assertFalse("Incorrect balance: only non-zero amount should be selected", fBalance.contains(new BalanceLine(book2.getId(), dt2.getBsCategory(), dt2.getName(), dt2.getAccId(), uah.getCode(), 0L)));
        assertTrue("Incorrect balance in UAH on Cr", fBalance.contains(new BalanceLine(book2.getId(), ct2.getBsCategory(), ct2.getName(), ct2.getAccId(), uah.getCode(), -1_000L)));
        assertTrue("Incorrect balance in USD on Dt", fBalance.contains(new BalanceLine(book2.getId(), dt2.getBsCategory(), dt2.getName(), dt2.getAccId(), usd.getCode(), 2_000L)));
        assertTrue("Incorrect balance in USD on Cr", fBalance.contains(new BalanceLine(book2.getId(), ct2.getBsCategory(), ct2.getName(), ct2.getAccId(), usd.getCode(), -2_000L)));
        assertTrue("Incorrect balance in UAH on Dt", fBalance.contains(new BalanceLine(book2.getId(), dt3.getBsCategory(), dt3.getName(), dt3.getAccId(), uah.getCode(), 1_000L)));
    }

    // Create Entry
    // ** successfully
    @Test
    public void create_Entry() {
        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));
        lines.add(new EntryLine(2, ct, -1000L, EntrySide.C, uah));
        lines.add(new EntryLine(3, dt, 2000L, EntrySide.D, usd));
        lines.add(new EntryLine(4, ct, -2000L, EntrySide.C, usd));

        EntryHeader header = entryService.createEntry(book, lines);

        assertEquals("Incorrect created entry.", book, header.getBook());
        assertEquals("Incorrect created entry.", lines.size(), header.getLines().size());

        for (EntryLine e : lines) {
            EntryLine ln = lineRepo.findOne(new EntryLineId(e.getLineId(), header.getId()));
            assertNotNull(ln);
            assertEquals("Incorrect created entry.", e.getCurrency().getId(), ln.getCurrency().getId());
            assertEquals("Incorrect created entry.", e.getType(), ln.getType());
            assertEquals("Incorrect created entry.", e.getTrnAmount(), ln.getTrnAmount());
        }

    }

    @Test
    public void create_Entry_WithDesc() {
        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));
        lines.add(new EntryLine(2, ct, -1000L, EntrySide.C, uah));
        lines.add(new EntryLine(3, dt, 2000L, EntrySide.D, usd));
        lines.add(new EntryLine(4, ct, -2000L, EntrySide.C, usd));

        String desc = "some desc";
        LocalDate valueDate = LocalDate.parse("2017-01-15");
        EntryHeader header = entryService.createEntry(book, lines, desc, valueDate);

        assertEquals("Incorrect created entry.", book.getId(), header.getBook().getId());
        assertEquals("Incorrect created entry.", lines.size(), header.getLines().size());
        assertEquals("Incorrect created entry.", desc, header.getDesc());
        assertEquals("Incorrect created entry", valueDate, header.getValueDate());

        for (EntryLine e : lines) {
            EntryLine ln = lineRepo.findOne(new EntryLineId(e.getLineId(), header.getId()));
            assertNotNull(ln);
            assertEquals("Incorrect created entry.", e.getCurrency().getId(), ln.getCurrency().getId());
            assertEquals("Incorrect created entry.", e.getType(), ln.getType());
            assertEquals("Incorrect created entry.", e.getTrnAmount(), ln.getTrnAmount());
        }

    }

    // Create Entry
    // ** rejected
    @Test(expected = IllegalArgumentException.class)
    public void rejectCreate_Entry_NullValueDate() {
        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));
        lines.add(new EntryLine(2, ct, -1000L, EntrySide.C, uah));
        lines.add(new EntryLine(3, dt, 2000L, EntrySide.D, usd));
        lines.add(new EntryLine(4, ct, -2000L, EntrySide.C, usd));

        EntryHeader header = entryService.createEntry(book, lines, "some comment", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectCreate_Entry_DescLength() {
        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));
        lines.add(new EntryLine(2, ct, -1000L, EntrySide.C, uah));
        lines.add(new EntryLine(3, dt, 2000L, EntrySide.D, usd));
        lines.add(new EntryLine(4, ct, -2000L, EntrySide.C, usd));

        EntryHeader header = entryService.createEntry(book, lines, "0123456789012345678901234567890123456789012345678901234567891", LocalDate.now());
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Entry_NullBook() {
        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));
        lines.add(new EntryLine(2, ct, -1000L, EntrySide.C, uah));

        EntryHeader header = entryService.createEntry(null, lines);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Entry_NonExistentBook() {
        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));
        lines.add(new EntryLine(2, ct, -1000L, EntrySide.C, uah));

        BalanceBook failedBook = new BalanceBook("f-book", "for testing", appUser);
        failedBook.setId(-1L);
        EntryHeader header = entryService.createEntry(failedBook, lines);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Entry_NullLines() {
        EntryHeader header = entryService.createEntry(book, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectCreate_Entry_EmptyLines() {
        Set<EntryLine> lines = new HashSet<>();
        EntryHeader header = entryService.createEntry(book, lines);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Entry_NonBalancingLines() {
        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));
        lines.add(new EntryLine(2, ct, -999L, EntrySide.C, uah));
        lines.add(new EntryLine(3, dt, 999L, EntrySide.D, usd));
        lines.add(new EntryLine(4, ct, -1000L, EntrySide.C, usd));

        EntryHeader header = entryService.createEntry(book, lines);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Entry_NoLineId() {
        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));

        EntryLine ln = new EntryLine();
        ln.setAccount(ct);
        ln.setTrnAmount(-1000L);
        ln.setType(EntrySide.C);
        ln.setCurrency(uah);
        lines.add(ln);

        EntryHeader header = entryService.createEntry(book, lines);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Entry_NegativeLineId() {
        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));

        EntryLine ln = new EntryLine();
        ln.setLineId(-1L);
        ln.setAccount(ct);
        ln.setTrnAmount(-1000L);
        ln.setType(EntrySide.C);
        ln.setCurrency(uah);
        lines.add(ln);

        EntryHeader header = entryService.createEntry(book, lines);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Entry_NullAccount() {
        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));
        lines.add(new EntryLine(2, null, -1000L, EntrySide.C, uah));

        EntryHeader header = entryService.createEntry(book, lines);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Entry_WrongAccount() {

        BalanceBook wBook = coaService.createBalanceBook(new BalanceBook("w-book", "for testing", appUser));
        BalanceAccount wAcc = coaService.createBalanceAccount(new BalanceAccount(BSCategory.ASSET, 1002L, wBook, "wrong acc"));

        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));

        EntryLine ln = new EntryLine();
        ln.setLineId(2L);
        ln.setAccount(wAcc);
        ln.setTrnAmount(-1000L);
        ln.setType(EntrySide.C);
        ln.setCurrency(uah);
        lines.add(ln);

        EntryHeader header = entryService.createEntry(book, lines);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Entry_DisabledAccount() {

        BalanceAccount wAcc = new BalanceAccount(BSCategory.ASSET, 8002L, book, "wrong acc");
        wAcc.setEnable(false);
        wAcc = coaService.createBalanceAccount(wAcc);


        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));

        EntryLine ln = new EntryLine();
        ln.setLineId(2L);
        ln.setAccount(wAcc);
        ln.setTrnAmount(-1000L);
        ln.setType(EntrySide.C);
        ln.setCurrency(uah);
        lines.add(ln);

        EntryHeader header = entryService.createEntry(book, lines);
    }

    @Test(expected = ConstraintViolationException.class)
    public void rejectCreate_Entry_NullAmount() {
        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));

        EntryLine ln = new EntryLine();
        ln.setLineId(2L);
        ln.setAccount(ct);
        ln.setTrnAmount(-1000L);
        ln.setType(null);
        ln.setCurrency(uah);
        lines.add(ln);

        EntryHeader header = entryService.createEntry(book, lines);
    }

    // Storno entry
    // ** successfully
    @Test
    public void storno_Entry() {
        Set<EntryLine> lines = new HashSet<>();
        lines.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));
        lines.add(new EntryLine(2, ct, -1000L, EntrySide.C, uah));

        EntryHeader header = entryService.createEntry(book, lines);
        assertEquals("Incorrect created test example.", header.isStorno(), false);

        header = entryService.setStorno(header.getId(), true);
        assertEquals("Incorrect storno.", header.isStorno(), true);
    }

    // Storno entry
    // ** reject
    @Test(expected = IllegalArgumentException.class)
    public void rejectStorno_NonExistingEntry() {
        entryService.setStorno(-1L, true);
    }

    // Find entry lines by specifications
    // ** reject
    @Test(expected = IllegalArgumentException.class)
    public void test_findEntryLines_NullCriteria() {
        entryService.findEntryLines(null, new PageRequest(0, 10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_findEntityLines_NullPageable() {
        entryService.findEntryLines(Arrays.asList(new SearchCriteria("id", SearchCriteria.CriteriaType.EQUALS, 1)), null);
    }

    // Find entry lines by specifications
    // ** successfully
    @Test
    public void test_findEntityLynes() {
        // preparing data
        Set<EntryLine> entryLinesFstPack = new HashSet<>();
        entryLinesFstPack.add(new EntryLine(1, dt, 1000L, EntrySide.D, uah));
        entryLinesFstPack.add(new EntryLine(2, ct, -1000L, EntrySide.C, uah));
        EntryHeader headerFst = entryService.createEntry(book, entryLinesFstPack, "entries for tests", LocalDate.parse("2013-12-31"));

        Set<EntryLine> entryLinesSecPack = new HashSet<>();
        entryLinesSecPack.add(new EntryLine(1, dt, 500L, EntrySide.D, uah));
        entryLinesSecPack.add(new EntryLine(2, ct, -500L, EntrySide.C, uah));
        entryLinesSecPack.add(new EntryLine(3, dt, 1000L, EntrySide.D, usd));
        entryLinesSecPack.add(new EntryLine(4, ct, -1000L, EntrySide.C, usd));
        EntryHeader headerSec = entryService.createEntry(book, entryLinesSecPack, "entries for tests", LocalDate.parse("2017-12-31"));

        // criteria and select
        List<SearchCriteria> criterias = new ArrayList<>();
        criterias.add(new SearchCriteria("currency.code", SearchCriteria.CriteriaType.EQUALS, uah.getCode()));
        criterias.add(new SearchCriteria("account.bsCategory", SearchCriteria.CriteriaType.EQUALS, BSCategory.ASSET));
        criterias.add(new SearchCriteria("account.accId", SearchCriteria.CriteriaType.EQUALS, dt.getAccId()));
        criterias.add(new SearchCriteria("account.book.id", SearchCriteria.CriteriaType.EQUALS, dt.getBook().getId()));
        criterias.add(new SearchCriteria("header.valueDate", SearchCriteria.CriteriaType.LESS_OR_EQUAL, LocalDate.parse("2013-12-31")));
        Page<EntryLine> rslPage = entryService.findEntryLines(criterias, new PageRequest(0, Integer.MAX_VALUE));

        // check result
        assertTrue(rslPage.hasContent());
        rslPage.getContent().forEach(e -> {
            assertEquals("Incorrect currency", uah.getCode(), e.getCurrency().getCode());
            assertEquals("Incorrect BSCategory", BSCategory.ASSET, e.getAccount().getBsCategory());
            assertEquals("Incorrect book", book.getId(), e.getAccount().getBook().getId());
            assertEquals("Incorrect account", dt.getAccId(), e.getAccount().getAccId());
            assertTrue("Incorrect date", LocalDate.parse("2013-12-31").isAfter(e.getHeader().getValueDate()) || LocalDate.parse("2013-12-31").isEqual(e.getHeader().getValueDate()));
        });

    }
}
