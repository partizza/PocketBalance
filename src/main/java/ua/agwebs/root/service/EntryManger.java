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
import ua.agwebs.root.repo.*;
import ua.agwebs.root.service.specifications.PocketBalanceSpecification;
import ua.agwebs.root.service.specifications.SearchCriteria;
import ua.agwebs.root.service.specifications.SpecificationBuilder;
import ua.agwebs.root.validator.EnabledBalanceBook;
import ua.agwebs.root.validator.EntryAmountBalancing;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class EntryManger implements EntryService {

    private static final Logger logger = LoggerFactory.getLogger(EntryManger.class);

    private EntryHeaderRepository headerRepo;

    private CurrencyRepository currencyRepo;

    private EntryLineRepository lineRepository;

    @Autowired
    public EntryManger(EntryHeaderRepository entryHeaderRepository, CurrencyRepository currencyRepository, EntryLineRepository entryLineRepository) {
        this.headerRepo = entryHeaderRepository;
        this.currencyRepo = currencyRepository;
        this.lineRepository = entryLineRepository;
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
        logger.trace("Creating new entry: book = {}, entry lines = {}, desc = {}", book, entryLines, desc);

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

        logger.info("New entry has been added into book: {}", book);
        logger.debug("Created entry: {}", entryHeader);
        return entryHeader;
    }

    @Override
    public EntryHeader setStorno(long entryHeaderId, boolean value) {
        logger.trace("Change storno flag: entry header id = {}, new storno flag = {}", entryHeaderId, value);

        EntryHeader header = headerRepo.findOne(entryHeaderId);
        Assert.notNull(header, "Entry doesn't exist.");

        header.setStorno(value);
        EntryHeader updatedEntry = headerRepo.save(header);

        logger.info("Storno flag has been changed: entry header id = {}", entryHeaderId);
        logger.debug("Changed storno flag: {}", updatedEntry);

        return updatedEntry;
    }

    @Override
    public EntryHeader findEntryHeaderById(long id) {
        logger.trace("Selecting entry by id: {}", id);
        return headerRepo.findOne(id);
    }

    @Override
    public Currency findCurrencyById(long id) {
        logger.trace("Selecting currency by id: {}", id);
        return currencyRepo.findOne(id);
    }

    @Override
    public Page<Currency> findAllCurrency(Pageable pageable) {
        logger.trace("Selecting all currency: pageable = {}", pageable);
        return currencyRepo.findAll(pageable);
    }

    @Override
    public List<ShortBalanceLine> getShortBookBalance(long bookId, LocalDate reportDate) {
        logger.trace("Calculating short book balance: bookId = {}, reportDate = {}", bookId, reportDate);

        Assert.notNull(reportDate);

        List<ShortBalanceLine> balanceLines = headerRepo.calcShortBookBalance(bookId, reportDate);

        logger.info("Short book balance has been calculated: bookId = {}, reportDate = {}", bookId, reportDate);
        logger.debug("Short book balance lines: {}", balanceLines);

        return balanceLines;
    }

    @Override
    public List<BalanceLine> getBookBalance(long bookId, LocalDate reportDate) {
        logger.trace("Calculating book balance: bookId = {}, reportDate = {}", bookId, reportDate);

        Assert.notNull(reportDate);

        List<BalanceLine> balanceLines = headerRepo.calcBookBalance(bookId, reportDate);

        logger.info("Book balance has been calculated: bookId = {}, reportDate = {}", bookId, reportDate);
        logger.debug("Book balance lines: {}", balanceLines);

        return balanceLines;
    }

    @Override
    public Page<EntryLine> findEntryLines(List<SearchCriteria> criteria, Pageable pageable) {
        logger.trace("Selecting entry lines with conditions: " +
                "List<SearchCriteria> = {}, Pageable = {}", criteria, pageable);

        Assert.notNull(criteria);
        Assert.notNull(pageable);

        SpecificationBuilder<EntryLine> specificationBuilder = new SpecificationBuilder<>();
        criteria.stream()
                .map(PocketBalanceSpecification<EntryLine>::new)
                .forEach(e -> specificationBuilder.addSpecification(e, SpecificationBuilder.SpecificationCompositionType.AND));

        Specification<EntryLine> spec = specificationBuilder.build();
        Page<EntryLine> rslPage = lineRepository.findAll(spec, pageable);

        logger.info("Entry lines have been selected: current page = {}, elements on current page = {}", rslPage.getNumber(), rslPage.getNumberOfElements());
        logger.debug("Selected entry lines: " +
                "total elements = {}, total pages = {}, page size = {}, current page = {}, elements on current page = {}",
                rslPage.getTotalElements(), rslPage.getTotalPages(), rslPage.getSize(), rslPage.getNumber(), rslPage.getNumberOfElements());

        return rslPage;
    }
}
