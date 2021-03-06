package ua.agwebs.web.rest.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.agwebs.security.AppUserDetails;
import ua.agwebs.web.PageDTO;

import java.util.List;

@RestController
@RequestMapping(value = {"/data/account"})
public class BalanceAccountController {

    private BalanceAccountService accountService;

    @Autowired
    public BalanceAccountController(BalanceAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/book/{bookId}/{accId}")
    public ResponseEntity<BalanceAccountDTO> getBalanceAccountById(@PathVariable("accId") long accId,
                                                                   @PathVariable("bookId") long bookId,
                                                                   @AuthenticationPrincipal AppUserDetails appUserDetails) {
        BalanceAccountDTO accountDTO = accountService.findBalanceAccountById(bookId, accId, appUserDetails.getId());
        ResponseEntity<BalanceAccountDTO> responseEntity;
        if (accountDTO == null) {
            responseEntity = new ResponseEntity<BalanceAccountDTO>(HttpStatus.NOT_FOUND);
        } else {
            responseEntity = new ResponseEntity<BalanceAccountDTO>(accountDTO, HttpStatus.OK);
        }
        return responseEntity;
    }

    @GetMapping(value = "/book/{bookId}/all")
    public ResponseEntity<List<BalanceAccountDTO>> findAllBalanceAccounts(@PathVariable("bookId") long bookId,
                                                                          @AuthenticationPrincipal AppUserDetails appUserDetails) {
        List<BalanceAccountDTO> dtoList = accountService.findBalanceAccountAllByBookId(bookId, appUserDetails.getId());
        return new ResponseEntity<List<BalanceAccountDTO>>(dtoList, HttpStatus.OK);
    }

    @PostMapping(value = "/book/{bookId}")
    public ResponseEntity<String> createBalanceAccount(@RequestBody BalanceAccountDTO accountDTO,
                                                       @PathVariable("bookId") long bookId,
                                                       @AuthenticationPrincipal AppUserDetails appUserDetails) {
        accountDTO.setBookId(bookId);
        accountService.createBalanceAccount(accountDTO, appUserDetails.getId());
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @PutMapping(value = "/book/{bookId}/{accId}")
    public ResponseEntity<String> updateBalanceAccount(@RequestBody BalanceAccountDTO accountDTO,
                                                       @PathVariable("accId") long accId,
                                                       @PathVariable("bookId") long bookId,
                                                       @AuthenticationPrincipal AppUserDetails appUserDetails) {

        accountDTO.setAccId(accId);
        accountDTO.setBookId(bookId);
        accountService.updateBalanceAccount(accountDTO, appUserDetails.getId());
        return new ResponseEntity<String>(HttpStatus.OK);
    }


}
