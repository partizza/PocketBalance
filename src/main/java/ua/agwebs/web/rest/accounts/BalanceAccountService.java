package ua.agwebs.web.rest.accounts;


import org.springframework.data.domain.Pageable;
import ua.agwebs.web.PageDTO;

public interface BalanceAccountService {

    public BalanceAccountDTO findById(long bookId, long accountId);

    public PageDTO<BalanceAccountDTO> findAll(Pageable pageable);

    public void createBalanceAccount(BalanceAccountDTO dto);

}
