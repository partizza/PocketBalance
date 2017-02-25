package ua.agwebs.root.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.annotation.Validated;
import ua.agwebs.root.entity.BalanceBook;

import javax.validation.Valid;

@Validated
public interface CoaService {

    public BalanceBook createBalanceBook(@Valid BalanceBook balanceBook);

    public BalanceBook updateBalanceBook(@Valid BalanceBook balanceBook);

    public BalanceBook mergeBalanceBook(@Valid BalanceBook balanceBook);

    public void deleteBalanceBook(long id);

    public BalanceBook findBalanceBookById(long id);

    public Page<BalanceBook> findAllBalanceBook(Specification<BalanceBook> specification, Pageable pageable);
}
