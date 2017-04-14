package ua.agwebs.web.rest.accounts;


import org.springframework.data.domain.Pageable;
import ua.agwebs.web.PageDTO;

import java.util.List;

public interface BalanceAccountService {

    public BalanceAccountDTO findBalanceAccountById(long bookId, long accountId);

    public PageDTO<BalanceAccountDTO> findBalanceAccountAll(Pageable pageable);

    public void createBalanceAccount(BalanceAccountDTO dto);

    public List<BalanceAccountDTO> findBalanceAccountAllByBookId(long bookId, long userId);

}
