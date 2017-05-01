package ua.agwebs.web.rest.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.agwebs.security.AppUserDetails;

import java.util.List;

@RestController
@RequestMapping(value = {"/data/transaction"})
public class TransactionController {

    private AccountingTransactionService transactionProvider;

    @Autowired
    public TransactionController(AccountingTransactionService accountingTransactionService) {
        this.transactionProvider = accountingTransactionService;
    }

    @GetMapping(value = "/book/{bookId}/all")
    public ResponseEntity<List<TransactionDTO>> getAllTransaction(@PathVariable("bookId") long bookId,
                                                                  @AuthenticationPrincipal AppUserDetails appUserDetails) {
        List<TransactionDTO> dtoList = transactionProvider.findTransactionAllByBookId(bookId, appUserDetails.getId());
        return new ResponseEntity<List<TransactionDTO>>(dtoList, HttpStatus.OK);
    }
}
