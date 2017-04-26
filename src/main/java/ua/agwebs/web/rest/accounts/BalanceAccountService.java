package ua.agwebs.web.rest.accounts;


import org.springframework.data.domain.Pageable;
import ua.agwebs.web.PageDTO;

import java.util.List;

public interface BalanceAccountService {

    public BalanceAccountDTO findBalanceAccountById(long bookId, long accountId, long userId);

    public List<BalanceAccountDTO> findBalanceAccountAllByBookId(long bookId, long userId);

    public void createBalanceAccount(BalanceAccountDTO dto, long userId);

    public void updateBalanceAccount(BalanceAccountDTO dto, long userId);
}
