package ua.agwebs.web.rest.accounting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
