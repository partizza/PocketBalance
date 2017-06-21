package ua.agwebs.web.rest.accounting;


public interface AccountingService {

    void createEntry(AccountingDTO dto, long userId);

}
