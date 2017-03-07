package ua.agwebs.root.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.annotation.Validated;
import ua.agwebs.root.entity.BalanceAccount;
import ua.agwebs.root.entity.BalanceBook;

import javax.validation.Valid;

@Validated
public interface CoaService {

    public BalanceBook createBalanceBook(@Valid BalanceBook balanceBook);

    public BalanceBook updateBalanceBook(@Valid BalanceBook balanceBook);

    public void deleteBalanceBook(long id);

    public BalanceBook findBalanceBookById(long id);

    public Page<BalanceBook> findAllBalanceBook(Pageable pageable);

    public BalanceAccount createBalanceAccount(@Valid BalanceAccount balanceAccount);

    public BalanceAccount updateBalanceAccount(@Valid BalanceAccount balanceAccount);

    public Page<BalanceAccount> findAllBalanceAccount(Pageable pageable);

    public BalanceAccount findBalanceAccountById(long bookId, long accId);
}
