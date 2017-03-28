package ua.agwebs.web.rest.Accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/data/account"})
public class BalanceAccountController {

    private BalanceAccountService accountService;

    @Autowired
    public BalanceAccountController(BalanceAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/{accId}/book/{bookId}")
    public ResponseEntity<BalanceAccountDTO> getBalanceAccountById(@PathVariable("accId") long accId,
                                                                   @PathVariable("bookId") long bookId) {
        BalanceAccountDTO accountDTO = accountService.findById(bookId, accId);
        ResponseEntity<BalanceAccountDTO> responseEntity;
        if (accountDTO == null) {
            responseEntity = new ResponseEntity<BalanceAccountDTO>(HttpStatus.NOT_FOUND);
        } else {
            responseEntity = new ResponseEntity<BalanceAccountDTO>(accountDTO, HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping(value = "/book/{bookId}")
    public ResponseEntity<String> createBalanceAccount(@RequestBody BalanceAccountDTO accountDTO,
                                                       @PathVariable("bookId") long bookId) {
        accountDTO.setBookId(bookId);
        accountService.createBalanceAccount(accountDTO);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
