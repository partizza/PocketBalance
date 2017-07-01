package ua.agwebs.web.rest.balance;


import java.time.LocalDate;

public interface BookBalanceService {

    BalanceSheetDTO getShortBookBalance(long bookId, LocalDate reportDate, long userId);
}
