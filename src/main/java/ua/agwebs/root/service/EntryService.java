package ua.agwebs.root.service;


import org.springframework.validation.annotation.Validated;
import ua.agwebs.root.entity.BalanceBook;
import ua.agwebs.root.entity.Currency;
import ua.agwebs.root.entity.EntryHeader;
import ua.agwebs.root.entity.EntryLine;
import ua.agwebs.root.validator.EnabledBalanceBook;
import ua.agwebs.root.validator.EntryAmountBalancing;

import javax.validation.Valid;
import java.util.Set;

@Validated
public interface EntryService {

    public EntryHeader createEntry(@EnabledBalanceBook @Valid BalanceBook book,
                                   @EntryAmountBalancing @Valid Set<EntryLine> entryLines);

    public EntryHeader setStorno(long entryHeaderId, boolean value);

    public EntryHeader findEntryHeaderById(long id);

    public Currency findCurrencyById(long id);

}
