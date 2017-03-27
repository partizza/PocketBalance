package ua.agwebs.web.rest.Accounts;


public interface BalanceAccountService {

    public BalanceAccountDTO findById(long bookId, long accountId);
}
