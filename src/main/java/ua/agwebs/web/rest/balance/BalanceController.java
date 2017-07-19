package ua.agwebs.web.rest.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.agwebs.security.AppUserDetails;

import java.time.LocalDate;

@Controller
@RequestMapping(value = "/data/balance")
public class BalanceController {

    private BookBalanceService balanceService;

    @Autowired
    public BalanceController(BookBalanceService bookBalanceService) {
        this.balanceService = bookBalanceService;
    }

    @RequestMapping("/book/{bookId}/short")
    public ResponseEntity<BalanceSheetDTO> getShortBalance(@PathVariable("bookId") long bookId,
                                                           @RequestParam(value = "date", required = false, defaultValue = "9999-12-31")
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate,
                                                           @AuthenticationPrincipal AppUserDetails appUserDetails) {

        BalanceSheetDTO balanceSheetDTO = balanceService.getShortBookBalance(bookId, reportDate, appUserDetails.getId());
        return new ResponseEntity<BalanceSheetDTO>(balanceSheetDTO, HttpStatus.OK);
    }

    @RequestMapping("/book/{bookId}")
    public ResponseEntity<BalanceSheetDTO> getBalance(@PathVariable("bookId") long bookId,
                                                      @RequestParam(value = "date", required = false, defaultValue = "9999-12-31")
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate,
                                                      @AuthenticationPrincipal AppUserDetails appUserDetails) {

        BalanceSheetDTO balanceSheetDTO = balanceService.getBookBalance(bookId, reportDate, appUserDetails.getId());
        return new ResponseEntity<BalanceSheetDTO>(balanceSheetDTO, HttpStatus.OK);
    }
}
