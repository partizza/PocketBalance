package ua.agwebs.web.rest.balance;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class BalanceSheetDTO {

    private Long bookId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportDate;

}
