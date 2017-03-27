package ua.agwebs.web.rest.Accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/data/account"})
public class BalanceAccountController {

    private BalanceAccountService accountService;

    @Autowired
    public BalanceAccountController(BalanceAccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping(value = "/{accId}/book/{bookId}")
    public ResponseEntity<BalanceAccountDTO> getBalanceAccountById(@RequestParam(value = "accId") long accId,
                                                                   @RequestParam(value = "bookId") long bookId){
        BalanceAccountDTO accountDTO = accountService.findById(bookId,accId);
        ResponseEntity<BalanceAccountDTO> responseEntity;
        if(accountDTO ==  null){
            responseEntity = new ResponseEntity<BalanceAccountDTO>(HttpStatus.NOT_FOUND);
        }else{
            responseEntity = new ResponseEntity<BalanceAccountDTO>(accountDTO, HttpStatus.OK);
        }
        return responseEntity;
    }
}
