package ua.agwebs.root.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.entity.Currency;
import ua.agwebs.root.entity.EntryHeader;
import ua.agwebs.root.entity.EntryLine;
import ua.agwebs.root.repo.BalanceLine;
import ua.agwebs.root.repo.ShortBalanceLine;
import ua.agwebs.root.service.specifications.SearchCriteria;
import ua.agwebs.root.validator.EnabledBalanceBook;
import ua.agwebs.root.validator.EntryAmountBalancing;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Validated
public interface EntryService {

    public EntryHeader createEntry(@EnabledBalanceBook @Valid BalanceBook book,
                                   @EntryAmountBalancing @Valid Set<EntryLine> entryLines);

    public EntryHeader createEntry(@EnabledBalanceBook @Valid BalanceBook book,
                                   @EntryAmountBalancing @Valid Set<EntryLine> entryLines,
                                   String desc,
                                   LocalDate valueDate);

    public EntryHeader setStorno(long entryHeaderId, boolean value);

    public EntryHeader findEntryHeaderById(long id);

    public Currency findCurrencyById(long id);

    public Page<Currency> findAllCurrency(Pageable pageable);

    public List<ShortBalanceLine> getShortBookBalance(long bookId, LocalDate reportDate);

    public List<BalanceLine> getBookBalance(long bookId, LocalDate reportDate);

    public Page<EntryLine> findEntryLines(List<SearchCriteria> criteria, Pageable pageable);

}
