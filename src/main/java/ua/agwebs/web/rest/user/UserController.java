package ua.agwebs.web.rest.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.agwebs.security.AppUserDetails;

import java.security.Principal;

@Controller
@RequestMapping(value = {"/data/user"})
public class UserController {

    @RequestMapping("/details")
    public ResponseEntity<AppUserDetails> getUserInfo(@AuthenticationPrincipal AppUserDetails appUserDetails){
        return new ResponseEntity<AppUserDetails>(appUserDetails, HttpStatus.OK);
    }
}
