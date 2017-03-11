package ua.agwebs.root.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.entity.Currency;
import ua.agwebs.root.entity.EntryHeader;
import ua.agwebs.root.entity.EntryLine;
import ua.agwebs.root.repo.CurrencyRepository;
import ua.agwebs.root.repo.EntryHeaderRepository;
import ua.agwebs.root.repo.EntryLineRepository;
import ua.agwebs.root.validator.EntryAmountBalancing;

import javax.validation.Valid;
import java.util.Set;

@Service
public class EntryManger implements EntryService {

    private static final Logger logger = LoggerFactory.getLogger(EntryManger.class);

    private EntryHeaderRepository headerRepo;

    private CurrencyRepository currencyRepo;

    @Autowired
    public EntryManger(EntryHeaderRepository entryHeaderRepository, CurrencyRepository currencyRepository) {
        this.headerRepo = entryHeaderRepository;
        this.currencyRepo = currencyRepository;
    }

    @Transactional
    @Override
    public EntryHeader createEntry(BalanceBook book, Set<EntryLine> entryLines) {
        logger.info("Create new entry.");
        logger.debug("Passed parameters: book = {}, entry lines = {}", book, entryLines);

        Assert.notNull(book, "Balance book can't be null.");

        Assert.notNull(entryLines, "Entry lines can't be null.");
        Assert.notEmpty(entryLines, "Can't create entry without lines.");

        for (EntryLine e : entryLines ) {
            Assert.isTrue(e.getAccount().getBook().getId() == book.getId(), "Accounts is not in the balance book.");
        }

        EntryHeader entryHeader = headerRepo.save(new EntryHeader(book));
        for (EntryLine e : entryLines ) {
            e.setHeader(entryHeader);
            entryHeader.addLines(e);
        }
        entryHeader = headerRepo.save(entryHeader);

        logger.debug("Created entry: {}", entryHeader);
        return entryHeader;
    }

    @Override
    public void setStorno(long entryHeaderId, boolean value) {

    }

    @Override
    public EntryHeader findEntryHeaderById(long id) {
        logger.info("Select entry by id: {}", id);
        return headerRepo.findOne(id);
    }

    @Override
    public Currency findCurrencyById(long id) {
        logger.info("Select currency by id: {}", id);
        return currencyRepo.findOne(id);
    }
}
