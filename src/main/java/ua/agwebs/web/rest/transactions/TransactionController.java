package ua.agwebs.web.rest.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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

    @DeleteMapping(value = "/{tranId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable("tranId") long tranId,
                                                    @AuthenticationPrincipal AppUserDetails appUserDetails){
        transactionProvider.deleteTransactionById(tranId, appUserDetails.getId());
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
