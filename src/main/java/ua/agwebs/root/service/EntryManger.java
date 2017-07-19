package ua.agwebs.root.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.entity.Currency;
import ua.agwebs.root.entity.EntryHeader;
import ua.agwebs.root.entity.EntryLine;
import ua.agwebs.root.repo.BalanceLine;
import ua.agwebs.root.repo.CurrencyRepository;
import ua.agwebs.root.repo.EntryHeaderRepository;
import ua.agwebs.root.repo.ShortBalanceLine;
import ua.agwebs.root.validator.EnabledBalanceBook;
import ua.agwebs.root.validator.EntryAmountBalancing;

import javax.persistence.criteria.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
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
        return this.createEntry(book, entryLines, "", LocalDate.now());
    }

    @Transactional
    @Override
    public EntryHeader createEntry(@EnabledBalanceBook @Valid BalanceBook book,
                                   @EntryAmountBalancing @Valid Set<EntryLine> entryLines,
                                   String desc,
                                   LocalDate valueDate) {
        logger.info("Create new entry.");
        logger.debug("Passed parameters: book = {}, entry lines = {}, desc = {}", book, entryLines, desc);

        Assert.isTrue(desc == null || desc.length() <= 60, "Entry description cannot have more than 60 characters.");

        Assert.notNull(valueDate, "Entry value date required.");

        Assert.notNull(book, "Balance book can't be null.");

        Assert.notNull(entryLines, "Entry lines can't be null.");
        Assert.notEmpty(entryLines, "Can't create entry without lines.");

        for (EntryLine e : entryLines) {
            Assert.isTrue(e.getAccount().getBook().getId() == book.getId(), "accounts is not in the balance book.");
        }

        EntryHeader entryHeader = headerRepo.save(new EntryHeader(book, desc, valueDate));
        for (EntryLine e : entryLines) {
            e.setHeader(entryHeader);
            entryHeader.addLines(e);
        }
        entryHeader = headerRepo.save(entryHeader);

        logger.debug("Created entry: {}", entryHeader);
        return entryHeader;
    }

    @Override
    public EntryHeader setStorno(long entryHeaderId, boolean value) {
        logger.info("Change storno flag.");
        logger.debug("Passed parameters: entry header id = {}, new storno flag = {}", entryHeaderId, value);

        EntryHeader header = headerRepo.findOne(entryHeaderId);
        Assert.notNull(header, "Entry doesn't exist.");

        header.setStorno(value);
        EntryHeader updatedEntry = headerRepo.save(header);

        logger.debug("Changed storno flag: {}", updatedEntry);
        return updatedEntry;
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

    @Override
    public Page<Currency> findAllCurrency(Pageable pageable) {
        logger.info("Select currency");
        logger.debug("Passed parameters: pageable = {}", pageable);

        return currencyRepo.findAll(pageable);
    }

    @Override
    public List<ShortBalanceLine> getShortBookBalance(long bookId, LocalDate reportDate) {
        logger.info("Calculate book balance");
        logger.debug("Passed parameters: bookId = {}, reportDate = {}", bookId, reportDate);

        Assert.notNull(reportDate);

        return headerRepo.calcShortBookBalance(bookId, reportDate);
    }

    @Override
    public List<BalanceLine> getBookBalance(long bookId, LocalDate reportDate) {
        logger.info("Calculate book balance");
        logger.debug("Passed parameters: bookId = {}, reportDate = {}", bookId, reportDate);

        Assert.notNull(reportDate);

        return headerRepo.calcBookBalance(bookId, reportDate);
    }
}
