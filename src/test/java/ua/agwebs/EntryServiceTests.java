package ua.agwebs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ua.agwebs.root.entity.*;
import ua.agwebs.root.repo.EntryLineRepository;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.root.service.EntryService;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EntryServiceTests {

    @Autowired
    private EntryService entryService;

    @Autowired
    private CoaService coaService;

    @Autowired
    private EntryLineRepository lineRepo;

    private static boolean setUpIsDone = false;

    private static BalanceBook book;

    private static BalanceAccount dt;

    private static BalanceAccount ct;

    private static Currency uah;

    private static Currency usd;

    @Before
    public void init() {
        if (!setUpIsDone) {
            book = coaService.createBalanceBook(new BalanceBook("t-book", "for testing"));
            dt = coaService.createBalanceAccount(new BalanceAccount(BSCategory.ASSET, 1002L, book, "Cash"));
            ct = coaService.createBalanceAccount(new BalanceAccount(BSCategory.PROFIT, 6000L, book, "Income"));
            uah = entryService.findCurrencyById(980);
            usd = entryService.findCurrencyById(840);

            setUpIsDone = true;
        }
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

    // Create Entry
    // ** rejected
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

        BalanceBook failedBook = new BalanceBook("f-book", "for testing");
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

        BalanceBook wBook = coaService.createBalanceBook(new BalanceBook("w-book", "for testing"));
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
    public void storno_Entry(){
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
    public void rejectStorno_NonExistingEntry(){
        entryService.setStorno(-1L, true);
    }
}
