package ua.agwebs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import ua.agwebs.root.entity.*;
import ua.agwebs.root.repo.EntryLineRepository;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.root.service.EntryService;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
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
            dt = coaService.createBalanceAccount(new BalanceAccount(1002L, book, "Cash"));
            ct = coaService.createBalanceAccount(new BalanceAccount(6000L, book, "Income"));
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
}
