package ua.agwebs.web.rest.accounting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.agwebs.security.AppUserDetails;

import java.util.List;

@Controller
@RequestMapping(value = {"/data/accounting"})
public class AccountingController {

    private AccountingService accountingService;

    @Autowired
    public AccountingController(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @GetMapping("/currency/all")
    public ResponseEntity<List<CurrencyDTO>> getAllCurrency() {
        List<CurrencyDTO> currencyDTOs = accountingService.findAllCurrency();
        return new ResponseEntity<List<CurrencyDTO>>(currencyDTOs, HttpStatus.OK);
    }

    @GetMapping("/book/{bookId}/transaction/type/{typeName}")
    public ResponseEntity<List<AccountingTransactionDTO>> getAllBookTransactionByType(@PathVariable("bookId") long bookId,
                                                                                      @PathVariable("typeName") String type,
                                                                                      @AuthenticationPrincipal AppUserDetails appUserDetails) {
        List<AccountingTransactionDTO> transactionDTOs = accountingService.findAllBookTransactionByType(bookId, type, appUserDetails.getId());
        return new ResponseEntity<List<AccountingTransactionDTO>>(transactionDTOs, HttpStatus.OK);
    }

    @PostMapping("/entity/book/{bookId}")
    public ResponseEntity<String> createEntry(@PathVariable("bookId") long bookId,
                                              @AuthenticationPrincipal AppUserDetails appUserDetails,
                                              @RequestBody AccountingDTO dto) {

        dto.setBookId(bookId);
        accountingService.createEntry(dto, appUserDetails.getId());
        return new ResponseEntity<String>(HttpStatus.OK);

    }
}
